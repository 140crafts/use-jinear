#!/bin/bash

# =============================================================================
#
#   ╦╦╔╗╔╔═╗╔═╗╦═╗  ╦╔╗╔╔═╗╔╦╗╔═╗╦  ╦  ╔═╗╦═╗
#   ║║║║║║╣ ╠═╣╠╦╝  ║║║║╚═╗ ║ ╠═╣║  ║  ║╣ ╠╦╝
#  ╚╝╩╝╚╝╚═╝╩ ╩╩╚═  ╩╝╚╝╚═╝ ╩ ╩ ╩╩═╝╩═╝╚═╝╩╚═
#
#   Self-Hosting Installation Script
#   https://gitlab.com/140crafts/use-jinear
#
# =============================================================================

set -e

# Version
INSTALLER_VERSION="1.0.0"
SCRIPT_URL="https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts"

# Detect script directory for local mode
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOCAL_MODE=false

# Check if templates exist locally
if [ -d "$SCRIPT_DIR/templates" ]; then
    LOCAL_MODE=true
fi

# Dry run mode (for testing)
DRY_RUN=false

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color
BOLD='\033[1m'

# Symbols
CHECK="${GREEN}✓${NC}"
CROSS="${RED}✗${NC}"
ARROW="${CYAN}➜${NC}"
INFO="${BLUE}ℹ${NC}"
WARN="${YELLOW}⚠${NC}"

# Default values
DEFAULT_INSTALL_DIR="./jinear"
DEFAULT_TIMEZONE="Europe/Istanbul"
DEFAULT_BACKUP_RETENTION=7

# =============================================================================
# UTILITY FUNCTIONS
# =============================================================================

print_banner() {
    echo ""
    echo -e "${CYAN}╔═══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${CYAN}║${NC}              ${BOLD}${WHITE}🚀 Jinear Self-Hosting Installer${NC}                 ${CYAN}║${NC}"
    echo -e "${CYAN}║${NC}                      ${MAGENTA}Version ${INSTALLER_VERSION}${NC}                            ${CYAN}║${NC}"
    echo -e "${CYAN}╚═══════════════════════════════════════════════════════════════╝${NC}"
    if [ "$LOCAL_MODE" = true ]; then
        echo -e "                    ${YELLOW}[LOCAL MODE - Using local templates]${NC}"
    fi
    if [ "$DRY_RUN" = true ]; then
        echo -e "                    ${YELLOW}[DRY RUN - Containers won't start]${NC}"
    fi
    echo ""
}

print_section() {
    echo ""
    echo -e "${BOLD}${WHITE}$1${NC}"
    echo -e "${CYAN}─────────────────────────────────────────────────────────────────${NC}"
}

print_success() {
    echo -e "  ${CHECK} $1"
}

print_error() {
    echo -e "  ${CROSS} $1"
}

print_warning() {
    echo -e "  ${WARN} $1"
}

print_info() {
    echo -e "  ${INFO} $1"
}

print_step() {
    echo -e "  ${ARROW} $1"
}

# Generate random password
generate_password() {
    local length=${1:-32}
    openssl rand -base64 $length | tr -dc 'a-zA-Z0-9' | head -c $length
}

# Generate random secret (longer, for JWT)
generate_secret() {
    openssl rand -base64 64 | tr -dc 'a-zA-Z0-9' | head -c 64
}

# Validate domain format
validate_domain() {
    local domain=$1
    if [[ $domain =~ ^[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$ ]]; then
        return 0
    else
        return 1
    fi
}

# Validate email format
validate_email() {
    local email=$1
    if [[ $email =~ ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$ ]]; then
        return 0
    else
        return 1
    fi
}

# Check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# =============================================================================
# PREREQUISITES CHECK
# =============================================================================

check_prerequisites() {
    print_section "📋 Checking Prerequisites"

    local all_passed=true

    # Check Docker
    if command_exists docker; then
        local docker_version=$(docker --version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -1)
        print_success "Docker installed (v${docker_version})"
    else
        print_error "Docker is not installed"
        print_info "Install Docker: https://docs.docker.com/get-docker/"
        all_passed=false
    fi

    # Check Docker Compose
    if command_exists docker-compose || docker compose version >/dev/null 2>&1; then
        if docker compose version >/dev/null 2>&1; then
            local compose_version=$(docker compose version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -1)
            print_success "Docker Compose installed (v${compose_version})"
        else
            local compose_version=$(docker-compose --version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -1)
            print_success "Docker Compose installed (v${compose_version})"
        fi
    else
        print_error "Docker Compose is not installed"
        print_info "Install Docker Compose: https://docs.docker.com/compose/install/"
        all_passed=false
    fi

    # Check if Docker is running
    if docker info >/dev/null 2>&1; then
        print_success "Docker daemon is running"
    else
        print_error "Docker daemon is not running"
        print_info "Start Docker and try again"
        all_passed=false
    fi

    # Check curl
    if command_exists curl; then
        print_success "curl is available"
    else
        print_error "curl is not installed"
        all_passed=false
    fi

    # Check openssl
    if command_exists openssl; then
        print_success "openssl is available"
    else
        print_error "openssl is not installed (needed for generating secrets)"
        all_passed=false
    fi

    # Check disk space (require at least 5GB)
    # Handle both Linux (df -BG) and macOS (df -g)
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS: df -g shows in GB
        available_space=$(df -g . 2>/dev/null | awk 'NR==2 {print $4}' | tr -dc '0-9')
    else
        # Linux: df -BG shows in GB
        available_space=$(df -BG . 2>/dev/null | awk 'NR==2 {print $4}' | tr -dc '0-9')
    fi

    if [ -n "$available_space" ] && [ "$available_space" -ge 5 ] 2>/dev/null; then
        print_success "Disk space available (${available_space}GB)"
    elif [ -n "$available_space" ]; then
        print_warning "Low disk space (${available_space}GB available, 5GB+ recommended)"
    else
        print_warning "Could not determine available disk space"
    fi

    # Check ports 80 and 443
    if ! lsof -i :80 >/dev/null 2>&1; then
        print_success "Port 80 is available"
    else
        print_warning "Port 80 is in use (may conflict with Caddy)"
    fi

    if ! lsof -i :443 >/dev/null 2>&1; then
        print_success "Port 443 is available"
    else
        print_warning "Port 443 is in use (may conflict with Caddy)"
    fi

    if [ "$all_passed" = false ]; then
        echo ""
        print_error "Some prerequisites are missing. Please install them and try again."
        exit 1
    fi
}

# =============================================================================
# CONFIGURATION PROMPTS
# =============================================================================

prompt_configuration() {
    print_section "📝 Configuration"

    # Installation directory
    echo ""
    echo -e "  ${BOLD}Installation Directory${NC}"
    read -p "  Where should Jinear be installed? [${DEFAULT_INSTALL_DIR}]: " INSTALL_DIR
    INSTALL_DIR=${INSTALL_DIR:-$DEFAULT_INSTALL_DIR}

    # Check if directory exists
    if [ -d "$INSTALL_DIR" ]; then
        echo ""
        print_warning "Directory '$INSTALL_DIR' already exists!"
        read -p "  Continue and overwrite configuration? [y/N]: " confirm
        if [[ ! $confirm =~ ^[Yy]$ ]]; then
            echo "Installation cancelled."
            exit 0
        fi
    fi

    # Domain configuration
    echo ""
    echo -e "  ${BOLD}Domain Configuration${NC}"
    echo -e "  ${INFO} Your domain should be pointed to this server's IP address"
    echo ""

    while true; do
        read -p "  Enter your main domain (e.g., jinear.company.com): " DOMAIN
        if validate_domain "$DOMAIN"; then
            break
        else
            print_error "Invalid domain format. Please try again."
        fi
    done

    # Auto-suggest subdomains
    # Auto-suggest subdomains
    API_DOMAIN_DEFAULT="api.${DOMAIN}"
    FILES_DOMAIN_DEFAULT="files.${DOMAIN}"
    PAGES_DOMAIN_DEFAULT="pages.${DOMAIN}"
    MESSAGE_DOMAIN_DEFAULT="message.${DOMAIN}"

    echo ""
    read -p "  API domain [${API_DOMAIN_DEFAULT}]: " API_DOMAIN
    API_DOMAIN=${API_DOMAIN:-$API_DOMAIN_DEFAULT}

    read -p "  Files domain [${FILES_DOMAIN_DEFAULT}]: " FILES_DOMAIN
    FILES_DOMAIN=${FILES_DOMAIN:-$FILES_DOMAIN_DEFAULT}

    read -p "  Pages domain [${PAGES_DOMAIN_DEFAULT}]: " PAGES_DOMAIN
    PAGES_DOMAIN=${PAGES_DOMAIN:-$PAGES_DOMAIN_DEFAULT}

    read -p "  Message domain [${MESSAGE_DOMAIN_DEFAULT}]: " MESSAGE_DOMAIN
    MESSAGE_DOMAIN=${MESSAGE_DOMAIN:-$MESSAGE_DOMAIN_DEFAULT}

    # Timezone
    echo ""
    echo -e "  ${BOLD}Timezone${NC}"
    read -p "  Enter your timezone [${DEFAULT_TIMEZONE}]: " TIMEZONE
    TIMEZONE=${TIMEZONE:-$DEFAULT_TIMEZONE}

    # Email configuration (optional)
    echo ""
    echo -e "  ${BOLD}Email Configuration (Optional)${NC}"
    echo -e "  ${INFO} Configure SMTP to enable email notifications"
    read -p "  Configure email now? [y/N]: " configure_email

    if [[ $configure_email =~ ^[Yy]$ ]]; then
        read -p "  SMTP Host: " MAIL_HOST
        read -p "  SMTP Port [587]: " MAIL_PORT
        MAIL_PORT=${MAIL_PORT:-587}
        read -p "  SMTP Username: " MAIL_USERNAME
        read -sp "  SMTP Password: " MAIL_PASSWORD
        echo ""
        read -p "  Sender Email Address: " MAIL_SENDER_ADDRESS
    else
        MAIL_HOST="smtp.example.com"
        MAIL_PORT="587"
        MAIL_USERNAME=""
        MAIL_PASSWORD=""
        MAIL_SENDER_ADDRESS="noreply@example.com"
    fi

    # Backup configuration
    echo ""
    echo -e "  ${BOLD}Backup Configuration${NC}"
    read -p "  Enable daily database backups? [Y/n]: " enable_backups
    if [[ $enable_backups =~ ^[Nn]$ ]]; then
        BACKUP_ENABLED="false"
        BACKUP_RETENTION_DAYS=7
    else
        BACKUP_ENABLED="true"
        read -p "  Backup retention days [${DEFAULT_BACKUP_RETENTION}]: " BACKUP_RETENTION_DAYS
        BACKUP_RETENTION_DAYS=${BACKUP_RETENTION_DAYS:-$DEFAULT_BACKUP_RETENTION}
    fi
}

# =============================================================================
# GENERATE SECRETS
# =============================================================================

generate_secrets() {
    print_section "🔐 Generating Secure Credentials"

    POSTGRES_PASSWORD=$(generate_password 32)
    print_success "Database password generated"

    REDIS_PASSWORD=$(generate_password 32)
    print_success "Redis password generated"

    JWT_SECRET=$(generate_secret)
    print_success "JWT secret generated"

    MINIO_ROOT_USER="jinear_minio_$(generate_password 8)"
    MINIO_ROOT_PASSWORD=$(generate_password 32)
    print_success "MinIO credentials generated"

    ENCRYPTOR_PASSWORD=$(generate_password 32)
    print_success "Encryptor password generated"

    INTERNAL_AUTH_TOKEN=$(generate_password 32)
    print_success "Internal auth token generated"

    CADDY_VALIDATION_TOKEN=$(generate_secret)
    print_success "Caddy validation token generated"
}

# =============================================================================
# CREATE DIRECTORY STRUCTURE
# =============================================================================

create_directories() {
    print_section "📁 Creating Directory Structure"

    mkdir -p "$INSTALL_DIR"
    print_success "Created $INSTALL_DIR"

    mkdir -p "$INSTALL_DIR/.config"
    mkdir -p "$INSTALL_DIR/.data/postgres"
    mkdir -p "$INSTALL_DIR/.data/redis"
    mkdir -p "$INSTALL_DIR/.data/minio"
    mkdir -p "$INSTALL_DIR/.data/caddy/conf"
    mkdir -p "$INSTALL_DIR/.data/caddy/data"
    mkdir -p "$INSTALL_DIR/.data/caddy/config"
    mkdir -p "$INSTALL_DIR/.data/caddy/site"
    print_success "Created data directories"

    mkdir -p "$INSTALL_DIR/.logs/jinear-core"
    mkdir -p "$INSTALL_DIR/.logs/jinear-web"
    mkdir -p "$INSTALL_DIR/.logs/jinear-pages"
    mkdir -p "$INSTALL_DIR/.logs/jinear-message"
    print_success "Created log directories"

    mkdir -p "$INSTALL_DIR/.backups"
    print_success "Created backup directory"
}

# =============================================================================
# GENERATE CONFIGURATION FILES
# =============================================================================

generate_config_files() {
    print_section "⚙️  Generating Configuration Files"

    # Generate .env file
    cat > "$INSTALL_DIR/.env" << EOF
# =============================================================================
# JINEAR CONFIGURATION
# Generated on $(date)
# =============================================================================

# Domain Configuration
DOMAIN=${DOMAIN}
API_DOMAIN=${API_DOMAIN}
FILES_DOMAIN=${FILES_DOMAIN}
PAGES_DOMAIN=${PAGES_DOMAIN}
MESSAGE_DOMAIN=${MESSAGE_DOMAIN}

# Timezone
TIMEZONE=${TIMEZONE}

# Database
POSTGRES_DB=jinear
POSTGRES_USER=jinear_user
POSTGRES_PASSWORD=${POSTGRES_PASSWORD}

# Redis
REDIS_PASSWORD=${REDIS_PASSWORD}

# MinIO
MINIO_ROOT_USER=${MINIO_ROOT_USER}
MINIO_ROOT_PASSWORD=${MINIO_ROOT_PASSWORD}

# Security
JWT_SECRET=${JWT_SECRET}
ENCRYPTOR_PASSWORD=${ENCRYPTOR_PASSWORD}
INTERNAL_AUTH_TOKEN=${INTERNAL_AUTH_TOKEN}

# Email
MAIL_HOST=${MAIL_HOST}
MAIL_PORT=${MAIL_PORT}
MAIL_USERNAME=${MAIL_USERNAME}
MAIL_PASSWORD=${MAIL_PASSWORD}
MAIL_SENDER_ADDRESS=${MAIL_SENDER_ADDRESS}

# Backup
BACKUP_ENABLED=${BACKUP_ENABLED}
BACKUP_RETENTION_DAYS=${BACKUP_RETENTION_DAYS}

# Analytics (Optional)
POSTHOG_KEY=
POSTHOG_HOST=https://us.i.posthog.com

# Rate Limiting
RATE_LIMIT_PUBLIC_CAPACITY=25
RATE_LIMIT_AUTH_CAPACITY=100

# Internal
CADDY_VALIDATION_TOKEN=${CADDY_VALIDATION_TOKEN}
EOF
    chmod 600 "$INSTALL_DIR/.env"
    print_success "Generated .env file"

    # Helper function to get template content
    get_template() {
        local template_name=$1
        if [ "$LOCAL_MODE" = true ]; then
            cat "$SCRIPT_DIR/templates/$template_name"
        else
            curl -sSL "${SCRIPT_URL}/templates/$template_name"
        fi
    }

    # Download/copy docker-compose.yaml
    if [ "$LOCAL_MODE" = true ]; then
        cp "$SCRIPT_DIR/templates/docker-compose.yaml" "$INSTALL_DIR/docker-compose.yaml"
        print_success "Generated docker-compose.yaml (from local templates)"
    else
        curl -sSL "${SCRIPT_URL}/templates/docker-compose.yaml" -o "$INSTALL_DIR/docker-compose.yaml"
        print_success "Generated docker-compose.yaml"
    fi

    # Generate Caddyfile
    get_template "Caddyfile.template" | \
        sed "s/__DOMAIN__/${DOMAIN}/g" | \
        sed "s/__API_DOMAIN__/${API_DOMAIN}/g" | \
        sed "s/__FILES_DOMAIN__/${FILES_DOMAIN}/g" | \
        sed "s/__PAGES_DOMAIN__/${PAGES_DOMAIN}/g" | \
        sed "s/__MESSAGE_DOMAIN__/${MESSAGE_DOMAIN}/g" | \
        sed "s/__CADDY_VALIDATION_TOKEN__/${CADDY_VALIDATION_TOKEN}/g" \
        > "$INSTALL_DIR/.data/caddy/conf/Caddyfile"
    print_success "Generated Caddyfile"

    # Copy application.properties (uses environment variables from docker-compose)
    if [ "$LOCAL_MODE" = true ]; then
        cp "$SCRIPT_DIR/templates/application.properties.template" "$INSTALL_DIR/.config/application.properties"
        print_success "Generated application.properties (from local templates)"
    else
        curl -sSL "${SCRIPT_URL}/templates/application.properties.template" -o "$INSTALL_DIR/.config/application.properties"
        print_success "Generated application.properties"
    fi

    # Download/copy db-backup.sh
    if [ "$LOCAL_MODE" = true ]; then
        cp "$SCRIPT_DIR/templates/db-backup.sh" "$INSTALL_DIR/.config/db-backup.sh"
        print_success "Generated db-backup.sh (from local templates)"
    else
        curl -sSL "${SCRIPT_URL}/templates/db-backup.sh" -o "$INSTALL_DIR/.config/db-backup.sh"
        print_success "Generated db-backup.sh"
    fi
    chmod +x "$INSTALL_DIR/.config/db-backup.sh"

    # Set permissions for data directory
    if [ "$(id -u)" = "0" ]; then
        chown -R 1001:1001 "$INSTALL_DIR/.data/"
        print_success "Set directory permissions"
    else
        print_warning "Run 'sudo chown -R 1001:1001 ${INSTALL_DIR}/.data/' to set proper permissions"
    fi
}

# =============================================================================
# SAVE CREDENTIALS
# =============================================================================

save_credentials() {
    print_section "💾 Saving Credentials"

    cat > "$INSTALL_DIR/.secrets" << EOF
# =============================================================================
# JINEAR CREDENTIALS - KEEP THIS FILE SECURE!
# Generated on $(date)
# =============================================================================

DATABASE
--------
Host: jinear-db
Port: 5432
Database: jinear
Username: jinear_user
Password: ${POSTGRES_PASSWORD}

REDIS
-----
Host: jinear-redis
Port: 6379
Password: ${REDIS_PASSWORD}

MINIO (S3-Compatible Storage)
-----------------------------
Console: http://localhost:9001
Username: ${MINIO_ROOT_USER}
Password: ${MINIO_ROOT_PASSWORD}

SECURITY SECRETS
----------------
JWT Secret: ${JWT_SECRET}
Encryptor Password: ${ENCRYPTOR_PASSWORD}
Internal Auth Token: ${INTERNAL_AUTH_TOKEN}

URLS
----
Application: https://${DOMAIN}
API: https://${API_DOMAIN}
Files: https://${FILES_DOMAIN}
Pages: https://${PAGES_DOMAIN}
Message (WebSocket): https://${MESSAGE_DOMAIN}
EOF
    chmod 600 "$INSTALL_DIR/.secrets"
    print_success "Credentials saved to ${INSTALL_DIR}/.secrets"
    print_warning "Keep this file secure and backed up!"
}

# =============================================================================
# START SERVICES
# =============================================================================

start_services() {
    print_section "🐳 Starting Services"

    if [ "$DRY_RUN" = true ]; then
        print_warning "Dry run mode - skipping container startup"
        print_info "To start containers manually, run:"
        print_info "  cd ${INSTALL_DIR} && docker compose up -d"
        return
    fi

    cd "$INSTALL_DIR"

    print_step "Pulling Docker images (this may take a few minutes)..."
    if docker compose pull 2>/dev/null || docker-compose pull 2>/dev/null; then
        print_success "Docker images pulled"
    else
        print_error "Failed to pull Docker images"
        exit 1
    fi

    print_step "Starting containers..."
    if docker compose up -d 2>/dev/null || docker-compose up -d 2>/dev/null; then
        print_success "Containers started"
    else
        print_error "Failed to start containers"
        exit 1
    fi

    # Wait for services to be healthy
    print_step "Waiting for services to be ready..."
    sleep 10

    # Check container status
    local running=$(docker ps --filter "name=jinear" --format "{{.Names}}" | wc -l | tr -d ' ')
    print_success "${running} containers running"
}

# =============================================================================
# PRINT SUMMARY
# =============================================================================

print_summary() {
    echo ""
    echo -e "${GREEN}╔═══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║${NC}              ${BOLD}${WHITE}✅ Installation Complete!${NC}                         ${GREEN}║${NC}"
    echo -e "${GREEN}╚═══════════════════════════════════════════════════════════════╝${NC}"
    echo ""

    echo -e "  ${BOLD}Your Jinear Instance${NC}"
    echo -e "  ${CYAN}─────────────────────────────────────────────────────────────${NC}"
    echo -e "  🌐 Application:  ${BOLD}https://${DOMAIN}${NC}"
    echo -e "  🔧 API:          ${BOLD}https://${API_DOMAIN}${NC}"
    echo -e "  📁 Files:        ${BOLD}https://${FILES_DOMAIN}${NC}"
    echo -e "  📄 Pages:        ${BOLD}https://${PAGES_DOMAIN}${NC}"
    echo -e "  💬 Message:      ${BOLD}https://${MESSAGE_DOMAIN}${NC}"
    echo ""

    echo -e "  ${BOLD}Important Files${NC}"
    echo -e "  ${CYAN}─────────────────────────────────────────────────────────────${NC}"
    echo -e "  📋 Configuration: ${INSTALL_DIR}/.env"
    echo -e "  🔐 Credentials:   ${INSTALL_DIR}/.secrets"
    echo -e "  📊 Logs:          ${INSTALL_DIR}/.logs/"
    echo -e "  💾 Backups:       ${INSTALL_DIR}/.backups/"
    echo ""

    echo -e "  ${BOLD}Next Steps${NC}"
    echo -e "  ${CYAN}─────────────────────────────────────────────────────────────${NC}"
    echo -e "  1. Point your DNS records to this server's IP address:"
    echo -e "     - ${DOMAIN}"
    echo -e "     - ${API_DOMAIN}"
    echo -e "     - ${FILES_DOMAIN}"
    echo -e "     - ${PAGES_DOMAIN}"
    echo -e "     - ${MESSAGE_DOMAIN}"
    echo ""
    echo -e "  2. Wait a few minutes for SSL certificates to be issued"
    echo ""
    echo -e "  3. Visit ${BOLD}https://${DOMAIN}${NC} to get started!"
    echo ""

    echo -e "  ${BOLD}Useful Commands${NC}"
    echo -e "  ${CYAN}─────────────────────────────────────────────────────────────${NC}"
    echo -e "  View logs:       cd ${INSTALL_DIR} && docker compose logs -f"
    echo -e "  Restart:         cd ${INSTALL_DIR} && docker compose restart"
    echo -e "  Stop:            cd ${INSTALL_DIR} && docker compose down"
    echo -e "  Update:          cd ${INSTALL_DIR} && docker compose pull && docker compose up -d"
    echo ""

    echo -e "  ${WARN} ${YELLOW}Backup your ${INSTALL_DIR}/.secrets file securely!${NC}"
    echo ""
}

# =============================================================================
# MAIN
# =============================================================================

main() {
    print_banner
    check_prerequisites
    prompt_configuration
    generate_secrets
    create_directories
    generate_config_files
    save_credentials
    start_services
    print_summary
}

# =============================================================================
# COMMAND LINE OPTIONS
# =============================================================================

show_help() {
    echo "Jinear Self-Hosting Installer v${INSTALLER_VERSION}"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  -h, --help      Show this help message"
    echo "  -v, --version   Show version"
    echo "  --status        Check status of running services"
    echo "  --dry-run       Run without starting containers (for testing)"
    echo ""
    echo "Examples:"
    echo "  $0              Interactive installation"
    echo "  $0 --dry-run    Test installation without starting containers"
    echo "  $0 --status     Check service status"
    echo ""
}

show_status() {
    echo -e "${BOLD}Jinear Service Status${NC}"
    echo ""
    docker ps --filter "name=jinear" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
}

# Parse arguments
case "${1:-}" in
    -h|--help)
        show_help
        exit 0
        ;;
    -v|--version)
        echo "Jinear Installer v${INSTALLER_VERSION}"
        exit 0
        ;;
    --status)
        show_status
        exit 0
        ;;
    --dry-run)
        DRY_RUN=true
        main
        ;;
    "")
        main
        ;;
    *)
        echo "Unknown option: $1"
        show_help
        exit 1
        ;;
esac

