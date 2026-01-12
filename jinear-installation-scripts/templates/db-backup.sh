#!/bin/bash

# =============================================================================
# Jinear Database Backup Script
# =============================================================================
# This script runs inside the backup container and performs daily backups
# of all PostgreSQL databases.
# =============================================================================

set -e

# Configuration from environment variables
BACKUP_DIR="/backups"
POSTGRES_HOST="${POSTGRES_HOST:-jinear-db}"
POSTGRES_USER="${POSTGRES_USER:-jinear_user}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD}"
POSTGRES_DB="${POSTGRES_DB:-jinear}"
RETENTION_DAYS="${BACKUP_RETENTION_DAYS:-7}"

# Install required packages
apt-get update && apt-get -y install cron tzdata > /dev/null 2>&1

# Set timezone from environment
if [ -n "$TZ" ]; then
    ln -fs /usr/share/zoneinfo/$TZ /etc/localtime
    dpkg-reconfigure --frontend noninteractive tzdata > /dev/null 2>&1
fi

# Create backup script
cat > /backup.sh << 'BACKUP_SCRIPT'
#!/bin/bash
set -e

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_DIR="/backups"
POSTGRES_HOST="${POSTGRES_HOST:-jinear-db}"
POSTGRES_USER="${POSTGRES_USER:-jinear_user}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD}"
POSTGRES_DB="${POSTGRES_DB:-jinear}"
RETENTION_DAYS="${BACKUP_RETENTION_DAYS:-7}"

# Create backup directory
mkdir -p "${BACKUP_DIR}/${TIMESTAMP}"

# Set PGPASSWORD
export PGPASSWORD="${POSTGRES_PASSWORD}"

echo "[$(date)] Starting backup process..."

# Get list of databases
DATABASES=$(psql -h ${POSTGRES_HOST} -U ${POSTGRES_USER} -d ${POSTGRES_DB} -t -c \
    "SELECT datname FROM pg_database WHERE datname NOT IN ('template0', 'template1', 'postgres') AND datistemplate = false;")

# Backup each database
for DB in $DATABASES; do
    DB_NAME=$(echo $DB | tr -d ' ')
    echo "[$(date)] Backing up database: $DB_NAME"

    pg_dump -h ${POSTGRES_HOST} -U ${POSTGRES_USER} -d $DB_NAME > "${BACKUP_DIR}/${TIMESTAMP}/${DB_NAME}.sql"
    gzip "${BACKUP_DIR}/${TIMESTAMP}/${DB_NAME}.sql"

    echo "[$(date)] Completed backup of $DB_NAME"
done

# Create full backup
echo "[$(date)] Creating full backup..."
pg_dumpall -h ${POSTGRES_HOST} -U ${POSTGRES_USER} > "${BACKUP_DIR}/${TIMESTAMP}/full_backup.sql"
gzip "${BACKUP_DIR}/${TIMESTAMP}/full_backup.sql"

# Remove old backups
echo "[$(date)] Removing backups older than ${RETENTION_DAYS} days..."
find ${BACKUP_DIR} -type d -mtime +${RETENTION_DAYS} -exec rm -rf {} \; 2>/dev/null || true

echo "[$(date)] Backup completed successfully!"
echo "---"
BACKUP_SCRIPT

# Make backup script executable
chmod +x /backup.sh

# Add cron job to run backup daily at 3:00 AM
echo "0 3 * * * /backup.sh >> /var/log/backup.log 2>&1" > /etc/cron.d/postgres-backup
chmod 0644 /etc/cron.d/postgres-backup
crontab /etc/cron.d/postgres-backup

# Create log file
touch /var/log/backup.log

# Run initial backup
echo "[$(date)] Running initial backup..."
/backup.sh

# Start cron in foreground
echo "[$(date)] Starting backup scheduler..."
cron && tail -f /var/log/backup.log

