#!/bin/bash

# Configuration from environment variables
BACKUP_DIR="/backups"
POSTGRES_HOST="${POSTGRES_HOST:-jinear-db}"
POSTGRES_USER="${POSTGRES_USER:-postgres}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD}"
POSTGRES_DB="${POSTGRES_DB:-postgres}"
RETENTION_DAYS="${BACKUP_RETENTION_DAYS:-7}"
BACKUP_SCHEDULE="${BACKUP_SCHEDULE:-0 3 * * *}"
TZ="${TZ:-Europe/Istanbul}"

# Install cron and tzdata
apt-get update && apt-get -y install cron tzdata postgresql-client -qq

# Set timezone
ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime
dpkg-reconfigure --frontend noninteractive tzdata

# Write environment variables to file for cron
cat > /etc/backup.env << EOF
POSTGRES_HOST=${POSTGRES_HOST}
POSTGRES_USER=${POSTGRES_USER}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
POSTGRES_DB=${POSTGRES_DB}
RETENTION_DAYS=${RETENTION_DAYS}
EOF
chmod 600 /etc/backup.env

# Create backup script
cat > /backup.sh << 'EOL'
#!/bin/bash
source /etc/backup.env

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_DIR="/backups"

# Create backup directory if it doesn't exist
mkdir -p ${BACKUP_DIR}/${TIMESTAMP}

# Set PGPASSWORD environment variable
export PGPASSWORD="${POSTGRES_PASSWORD}"

echo "[$(date)] Starting backup process"

# Get list of databases
DATABASES=$(psql -h ${POSTGRES_HOST} -U ${POSTGRES_USER} -d ${POSTGRES_DB} -t -c "SELECT datname FROM pg_database WHERE datname NOT IN ('template0', 'template1', 'postgres') AND datistemplate = false;")

# Backup each database
for DB in $DATABASES; do
    DB_NAME=$(echo $DB | tr -d ' ')
    if [ -n "$DB_NAME" ]; then
        echo "[$(date)] Backing up database: $DB_NAME"
        pg_dump -h ${POSTGRES_HOST} -U ${POSTGRES_USER} -d $DB_NAME > "${BACKUP_DIR}/${TIMESTAMP}/${DB_NAME}.sql"
        gzip "${BACKUP_DIR}/${TIMESTAMP}/${DB_NAME}.sql"
        echo "[$(date)] Completed backup of $DB_NAME"
    fi
done

# Create full backup
echo "[$(date)] Creating full backup of all databases"
pg_dumpall -h ${POSTGRES_HOST} -U ${POSTGRES_USER} > "${BACKUP_DIR}/${TIMESTAMP}/full_backup.sql"
gzip "${BACKUP_DIR}/${TIMESTAMP}/full_backup.sql"

# Delete old backups
echo "[$(date)] Removing backups older than ${RETENTION_DAYS} days"
find ${BACKUP_DIR} -type d -mtime +${RETENTION_DAYS} -exec rm -rf {} \; 2>/dev/null || true

echo "[$(date)] Backup completed"
EOL

# Make backup script executable
chmod +x /backup.sh

# Add cron job
echo "${BACKUP_SCHEDULE} /backup.sh >> /var/log/cron.log 2>&1" > /etc/cron.d/postgres-backup
chmod 0644 /etc/cron.d/postgres-backup
crontab /etc/cron.d/postgres-backup

# Create log file
touch /var/log/cron.log

# Run initial backup
echo "Running initial backup..."
/backup.sh

# Start cron in foreground
echo "Starting cron service..."
cron && tail -f /var/log/cron.log
