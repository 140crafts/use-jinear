#!/bin/bash

# Configuration
BACKUP_DIR="/backups"
POSTGRES_HOST="jinear-db"
POSTGRES_USER="db-user"
POSTGRES_PASSWORD="db-pass"
POSTGRES_DB="jinear"  # Default database to connect to
RETENTION_DAYS=7

# Install cron and tzdata
apt-get update && apt-get -y install cron tzdata

# Set timezone to GMT+3
ln -fs /usr/share/zoneinfo/Asia/Baghdad /etc/localtime  # Using Baghdad as an example for GMT+3
dpkg-reconfigure --frontend noninteractive tzdata

# Create backup script
cat > /backup.sh << 'EOL'
#!/bin/bash
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_DIR="/backups"
POSTGRES_HOST="crafts-db"
POSTGRES_USER="db-user"
POSTGRES_PASSWORD="db-pass"
POSTGRES_DB="jinear"  # Default database to connect to
RETENTION_DAYS=7

# Create backup directory if it doesn't exist
mkdir -p ${BACKUP_DIR}/${TIMESTAMP}

# Set PGPASSWORD environment variable
export PGPASSWORD="${POSTGRES_PASSWORD}"

echo "Starting backup process at $(date)"

# First get list of databases, explicitly connecting to the crafts database
DATABASES=$(psql -h ${POSTGRES_HOST} -U ${POSTGRES_USER} -d ${POSTGRES_DB} -t -c "SELECT datname FROM pg_database WHERE datname NOT IN ('template0', 'template1', 'postgres') AND datistemplate = false;")

# Backup each database in a separate file
for DB in $DATABASES; do
    DB_NAME=$(echo $DB | tr -d ' ')
    echo "Backing up database: $DB_NAME"
    
    # Create a directory for each timestamp
    pg_dump -h ${POSTGRES_HOST} -U ${POSTGRES_USER} -d $DB_NAME > "${BACKUP_DIR}/${TIMESTAMP}/${DB_NAME}.sql"
    
    # Compress each backup file
    gzip "${BACKUP_DIR}/${TIMESTAMP}/${DB_NAME}.sql"
    
    echo "Completed backup of $DB_NAME"
done

# Create a full backup of all databases
echo "Creating full backup of all databases"
pg_dumpall -h ${POSTGRES_HOST} -U ${POSTGRES_USER} > "${BACKUP_DIR}/${TIMESTAMP}/full_backup.sql"
gzip "${BACKUP_DIR}/${TIMESTAMP}/full_backup.sql"

# Delete backups older than retention days
echo "Removing backups older than ${RETENTION_DAYS} days"
find ${BACKUP_DIR} -type d -mtime +${RETENTION_DAYS} -exec rm -rf {} \; 2>/dev/null || true

echo "Backup completed at $(date)"
EOL

# Make backup script executable
chmod +x /backup.sh

# Add cron job to run backup daily at 3:00 AM
echo "0 3 * * * /backup.sh >> /var/log/cron.log 2>&1" > /etc/cron.d/postgres-backup
chmod 0644 /etc/cron.d/postgres-backup

# Apply cron job
crontab /etc/cron.d/postgres-backup

# Create log file
touch /var/log/cron.log

# Run initial backup
echo "Running initial backup..."
/backup.sh

# Start cron in foreground
echo "Starting cron service..."
cron && tail -f /var/log/cron.log