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

# Check if we can read from TTY (required for interactive prompts)
if ! exec 3</dev/tty 2>/dev/null; then
    echo "Error: This installer requires an interactive terminal."
    echo ""
    echo "Please run it one of these ways:"
    echo "  1. Download and run:"
    echo "     curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh -o install.sh"
    echo "     chmod +x install.sh"
    echo "     ./install.sh"
    echo ""
    echo "  2. Or use bash with TTY:"
    echo "     bash -c \"\$(curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh)\""
    exit 1
fi
exec 3<&-

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

# Validate timezone format (IANA/Olson format: Region/City)
# Returns 0 if valid, 1 if invalid
validate_timezone() {
    local tz=$1

    # Check basic format: Region/City or Region/Sub/City
    if [[ ! $tz =~ ^[A-Za-z_]+/[A-Za-z_]+(/[A-Za-z_]+)?$ ]]; then
        # Also allow simple formats like UTC, GMT
        if [[ ! $tz =~ ^(UTC|GMT)$ ]]; then
            return 1
        fi
    fi

    # Check if timezone exists in the system
    if [ -f "/usr/share/zoneinfo/$tz" ]; then
        return 0
    fi

    # On macOS, check differently
    if [[ "$OSTYPE" == "darwin"* ]]; then
        if [ -f "/var/db/timezone/zoneinfo/$tz" ] || [ -f "/usr/share/zoneinfo/$tz" ]; then
            return 0
        fi
    fi

    # If we can't verify, accept it if format looks right
    if [[ $tz =~ ^[A-Za-z_]+/[A-Za-z_]+(/[A-Za-z_]+)?$ ]] || [[ $tz =~ ^(UTC|GMT)$ ]]; then
        return 0
    fi

    return 1
}

# Check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check if a TCP port is available on localhost.
# Returns 0 if available, 1 if already in use.
check_port() {
    local port=$1
    # Method 1: Try to reach the port using nc (most reliable)
    if command_exists nc; then
        if nc -z localhost "$port" 2>/dev/null; then
            return 1  # Port is in use
        else
            return 0  # Port is available
        fi
    fi
    # Method 2: Check with lsof (may need sudo for full visibility)
    if lsof -i :"$port" 2>/dev/null | grep -q LISTEN; then
        return 1  # Port is in use
    fi
    return 0  # Port is available
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

    # Note: host port availability is checked after configuration, once the
    # user has chosen HTTP_PORT / HTTPS_PORT (see prompt_configuration).

    if [ "$all_passed" = false ]; then
        echo ""
        print_error "Some prerequisites are missing. Please install them and try again."
        exit 1
    fi
}

# =============================================================================
# CONFIGURATION PROMPTS
# =============================================================================

# Helper function to read input from TTY with prompt
prompt_input() {
    local prompt="$1"
    local default="$2"
    local result=""

    if [ -n "$default" ]; then
        printf "%s [%s]: " "$prompt" "$default" > /dev/tty
    else
        printf "%s: " "$prompt" > /dev/tty
    fi

    read -r result < /dev/tty

    if [ -z "$result" ] && [ -n "$default" ]; then
        result="$default"
    fi

    echo "$result"
}

# Helper function to read password (hidden input)
prompt_password() {
    local prompt="$1"
    local result=""

    printf "%s: " "$prompt" > /dev/tty
    read -rs result < /dev/tty
    echo "" > /dev/tty

    echo "$result"
}

prompt_configuration() {
    print_section "📝 Configuration"

    # Installation directory
    echo ""
    echo -e "  ${BOLD}Installation Directory${NC}"
    INSTALL_DIR=$(prompt_input "  Where should Jinear be installed?" "${DEFAULT_INSTALL_DIR}")

    # Check if directory exists
    if [ -d "$INSTALL_DIR" ]; then
        echo ""
        print_warning "Directory '$INSTALL_DIR' already exists!"
        local confirm=$(prompt_input "  Continue and overwrite configuration? [y/N]" "")
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
        DOMAIN=$(prompt_input "  Enter your main domain (e.g., jinear.company.com)" "")
        if [ -n "$DOMAIN" ] && validate_domain "$DOMAIN"; then
            break
        else
            print_error "Invalid domain format. Please try again."
        fi
    done

    # Auto-suggest subdomains
    API_DOMAIN_DEFAULT="api.${DOMAIN}"
    FILES_DOMAIN_DEFAULT="files.${DOMAIN}"

    echo ""
    API_DOMAIN=$(prompt_input "  API domain" "${API_DOMAIN_DEFAULT}")
    FILES_DOMAIN=$(prompt_input "  Files domain" "${FILES_DOMAIN_DEFAULT}")

    # Networking & HTTPS
    echo ""
    echo -e "  ${BOLD}Networking & HTTPS${NC}"
    echo -e "  ${INFO} Caddy binds these host ports. Change them to run Jinear behind"
    echo -e "  ${INFO} another reverse proxy (e.g. HTTP port 8080)."
    echo ""

    HTTP_PORT=$(prompt_input "  HTTP port" "80")

    echo ""
    echo -e "  ${INFO} Automatic HTTPS lets Caddy obtain free Let's Encrypt certificates."
    echo -e "  ${INFO} Disable it if TLS is terminated by your own proxy, or for plain HTTP."
    local enable_auto_https=$(prompt_input "  Enable automatic HTTPS via Let's Encrypt? [Y/n]" "")

    if [[ $enable_auto_https =~ ^[Nn]$ ]]; then
        AUTO_HTTPS="false"
        HTTPS_PORT=443  # unused; the 443 mapping is removed from docker-compose
        echo ""
        echo -e "  ${INFO} Caddy will serve plain HTTP. Choose your topology:"
        local behind_tls_proxy=$(prompt_input "  Will Jinear run behind a proxy that terminates HTTPS? [Y/n]" "")
        if [[ $behind_tls_proxy =~ ^[Nn]$ ]]; then
            # Plain HTTP everywhere (no TLS) - relax secure cookies so login works.
            EXTERNAL_SCHEME="http"
            JWT_IS_SECURE="false"
            JWT_SAME_SITE="Lax"
        else
            # Behind an upstream TLS-terminating proxy - external URLs stay https.
            EXTERNAL_SCHEME="https"
            JWT_IS_SECURE="true"
            JWT_SAME_SITE="None"
        fi
    else
        AUTO_HTTPS="true"
        HTTPS_PORT=$(prompt_input "  HTTPS port" "443")
        EXTERNAL_SCHEME="https"
        JWT_IS_SECURE="true"
        JWT_SAME_SITE="None"
    fi

    # Warn if the chosen host ports are already in use
    if ! check_port "$HTTP_PORT"; then
        print_warning "Port ${HTTP_PORT} is in use (may conflict with Caddy)"
    fi
    if [ "$AUTO_HTTPS" = "true" ] && ! check_port "$HTTPS_PORT"; then
        print_warning "Port ${HTTPS_PORT} is in use (may conflict with Caddy)"
    fi

    # Timezone
    echo ""
    echo -e "  ${BOLD}Timezone${NC}"
    echo -e "  ${INFO} Format: Region/City (e.g., Europe/Istanbul, America/New_York, Asia/Tokyo)"

    while true; do
        TIMEZONE=$(prompt_input "  Enter your timezone" "${DEFAULT_TIMEZONE}")

        if validate_timezone "$TIMEZONE"; then
            break
        else
            print_error "Invalid timezone format. Use IANA format like 'Europe/Istanbul' or 'America/New_York'"
            print_info "See: https://en.wikipedia.org/wiki/List_of_tz_database_time_zones"
        fi
    done

    # Email configuration (optional)
    echo ""
    echo -e "  ${BOLD}Email Configuration (Optional)${NC}"
    echo -e "  ${INFO} Configure SMTP to enable email notifications"
    local configure_email=$(prompt_input "  Configure email now? [y/N]" "")

    if [[ $configure_email =~ ^[Yy]$ ]]; then
        MAIL_HOST=$(prompt_input "  SMTP Host" "")
        MAIL_PORT=$(prompt_input "  SMTP Port" "587")
        MAIL_USERNAME=$(prompt_input "  SMTP Username" "")
        MAIL_PASSWORD=$(prompt_password "  SMTP Password")
        MAIL_SENDER_ADDRESS=$(prompt_input "  Sender Email Address" "")
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
    local enable_backups=$(prompt_input "  Enable daily database backups? [Y/n]" "")
    if [[ $enable_backups =~ ^[Nn]$ ]]; then
        BACKUP_ENABLED="false"
        BACKUP_RETENTION_DAYS=7
    else
        BACKUP_ENABLED="true"
        BACKUP_RETENTION_DAYS=$(prompt_input "  Backup retention days" "${DEFAULT_BACKUP_RETENTION}")
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

# Networking / HTTPS
# HTTP_PORT / HTTPS_PORT: host ports Caddy binds (change to run behind another proxy).
# AUTO_HTTPS: true = Caddy issues Let's Encrypt certs (443 published);
#             false = Caddy serves plain HTTP only (443 not published).
# EXTERNAL_SCHEME + JWT_*: https/secure cookies for TLS setups; http/relaxed for plain HTTP.
HTTP_PORT=${HTTP_PORT}
HTTPS_PORT=${HTTPS_PORT}
AUTO_HTTPS=${AUTO_HTTPS}
EXTERNAL_SCHEME=${EXTERNAL_SCHEME}
JWT_IS_SECURE=${JWT_IS_SECURE}
JWT_SAME_SITE=${JWT_SAME_SITE}

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

# Sign In with Apple (backend) — Optional, disabled by default.
# Set APPLE_ENABLED=true (and fill the APPLE_* values + mount AuthKey.p8 into
# .config/) to enable it. When false, jinear-core boots without the key file.
APPLE_ENABLED=false

# Web App (jinear-app) — Optional
# Apple Sign In: fill with your Apple Service ID and its return URL to enable it.
VITE_APPLE_CLIENT_ID=
VITE_APPLE_REDIRECT_URI=
# Firebase Cloud Messaging (web push): fill from your own Firebase project to
# enable browser/PWA notifications. Leave blank to disable push entirely.
# VAPID key = Firebase Console > Project settings > Cloud Messaging > Web Push certificates.
VITE_FIREBASE_API_KEY=
VITE_FIREBASE_AUTH_DOMAIN=
VITE_FIREBASE_PROJECT_ID=
VITE_FIREBASE_STORAGE_BUCKET=
VITE_FIREBASE_MESSAGING_SENDER_ID=
VITE_FIREBASE_APP_ID=
VITE_FIREBASE_MEASUREMENT_ID=
VITE_FIREBASE_VAPID_KEY=

# Rate Limiting
RATE_LIMIT_PUBLIC_CAPACITY=25
RATE_LIMIT_AUTH_CAPACITY=100
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

    # When automatic HTTPS is off, don't publish the 443 host ports (Caddy serves
    # plain HTTP only, and something upstream — the user's proxy — owns 443).
    if [ "$AUTO_HTTPS" != "true" ]; then
        sed -i.bak '/jinear-https-ports/,+2d' "$INSTALL_DIR/docker-compose.yaml"
        rm -f "$INSTALL_DIR/docker-compose.yaml.bak"
        print_success "Configured Caddy to publish HTTP port ${HTTP_PORT} only (HTTPS disabled)"
    fi

    # Generate Caddyfile — https/ACME template for auto-HTTPS, http-only otherwise
    if [ "$AUTO_HTTPS" = "true" ]; then
        CADDY_TEMPLATE="Caddyfile.template"
    else
        CADDY_TEMPLATE="Caddyfile.http.template"
    fi
    get_template "$CADDY_TEMPLATE" | \
        sed "s/__DOMAIN__/${DOMAIN}/g" | \
        sed "s/__API_DOMAIN__/${API_DOMAIN}/g" | \
        sed "s/__FILES_DOMAIN__/${FILES_DOMAIN}/g" \
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
Application: ${EXTERNAL_SCHEME}://${DOMAIN}
API: ${EXTERNAL_SCHEME}://${API_DOMAIN}
Files: ${EXTERNAL_SCHEME}://${FILES_DOMAIN}
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
    echo -e "${GREEN}║${NC}              ${BOLD}${WHITE}✅ Installation Complete!${NC}                        ${GREEN}║${NC}"
    echo -e "${GREEN}╚═══════════════════════════════════════════════════════════════╝${NC}"
    echo ""

    echo -e "  ${BOLD}Your Jinear Instance${NC}"
    echo -e "  ${CYAN}─────────────────────────────────────────────────────────────${NC}"
    echo -e "  🌐 Application:  ${BOLD}${EXTERNAL_SCHEME}://${DOMAIN}${NC}"
    echo -e "  🔧 API:          ${BOLD}${EXTERNAL_SCHEME}://${API_DOMAIN}${NC}"
    echo -e "  📁 Files:        ${BOLD}${EXTERNAL_SCHEME}://${FILES_DOMAIN}${NC}"
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
    echo ""
    if [ "$AUTO_HTTPS" = "true" ]; then
        echo -e "  2. Wait a few minutes for SSL certificates to be issued"
        echo ""
        echo -e "  3. Visit ${BOLD}${EXTERNAL_SCHEME}://${DOMAIN}${NC} to get started!"
    else
        echo -e "  2. Point your reverse proxy at this host on HTTP port ${BOLD}${HTTP_PORT}${NC}"
        echo -e "     (Caddy serves plain HTTP; TLS is handled by your proxy)"
        echo ""
        echo -e "  3. Visit ${BOLD}${EXTERNAL_SCHEME}://${DOMAIN}${NC} to get started!"
    fi
    echo ""

    if [ "$AUTO_HTTPS" = "true" ]; then
        echo -e "  ${WARN} ${YELLOW}SSL Certificate Notes:${NC}"
        echo -e "     - Using Let's Encrypt production server"
        echo -e "     - Rate limit: 50 certificates per domain per week"
        echo -e "     - If testing, consider using staging server (see docs)"
    else
        echo -e "  ${WARN} ${YELLOW}Reverse Proxy Notes:${NC}"
        echo -e "     - Automatic HTTPS is disabled; Caddy listens on HTTP port ${HTTP_PORT}"
        echo -e "     - Forward ${DOMAIN}, ${API_DOMAIN} and ${FILES_DOMAIN} to it"
        echo -e "     - Terminate TLS on your own proxy (see docs: behind your own proxy)"
    fi
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

