#!/bin/bash

# Database backup script for Jinear
set -e

BACKUP_DIR="/backups"
RETENTION_DAYS="${BACKUP_RETENTION_DAYS:-7}"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

log() {
    echo "[$(date '+%a %b %d %I:%M:%S %p %Z %Y')] $1"
}

do_backup() {
    log "Starting backup process..."

    # Export password for pg_dump
    export PGPASSWORD="${POSTGRES_PASSWORD}"

    # Get list of databases (excluding templates)
    DATABASES=$(psql -h jinear-db -U "${POSTGRES_USER:-jinear_user}" -d postgres -t -c "SELECT datname FROM pg_database WHERE datistemplate = false AND datname != 'postgres';" 2>/dev/null | tr -d ' ')

    for DB in $DATABASES; do
        if [ -n "$DB" ]; then
            log "Backing up database: $DB"
            pg_dump -h jinear-db -U "${POSTGRES_USER:-jinear_user}" -d "$DB" -Fc -f "${BACKUP_DIR}/${DB}_${TIMESTAMP}.dump"
            log "Completed backup of $DB"
        fi
    done

    log "Creating full backup..."
    pg_dumpall -h jinear-db -U "${POSTGRES_USER:-jinear_user}" --globals-only -f "${BACKUP_DIR}/globals_${TIMESTAMP}.sql"

    log "Removing backups older than ${RETENTION_DAYS} days..."
    find "${BACKUP_DIR}" -name "*.dump" -type f -mtime +${RETENTION_DAYS} -delete
    find "${BACKUP_DIR}" -name "*.sql" -type f -mtime +${RETENTION_DAYS} -delete

    log "Backup completed successfully!"
    echo "---"
}

# Write environment variables to a file for cron
setup_cron_env() {
    echo "POSTGRES_PASSWORD=${POSTGRES_PASSWORD}" > /etc/backup.env
    echo "POSTGRES_USER=${POSTGRES_USER:-jinear_user}" >> /etc/backup.env
    echo "BACKUP_RETENTION_DAYS=${RETENTION_DAYS}" >> /etc/backup.env
    chmod 600 /etc/backup.env
}

# Main entrypoint
log "Starting backup scheduler..."

# Install PostgreSQL client if not present
apt-get update && apt-get install -y postgresql-client -qq

# Setup environment for cron
setup_cron_env

# Create cron job that sources the environment file
echo "0 3 * * * . /etc/backup.env && /backup.sh run >> /var/log/backup.log 2>&1" | crontab -

# Run initial backup
log "Running initial backup..."
do_backup

# Handle script arguments
case "${1:-}" in
    run)
        # Source environment if running from cron
        if [ -f /etc/backup.env ]; then
            source /etc/backup.env
        fi
        do_backup
        ;;
    *)
        # Start cron in foreground
        cron -f
        ;;
esac
