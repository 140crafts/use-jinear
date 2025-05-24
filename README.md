# Jinear - Task Manager & Calendar

A collaborative task management and calendar application designed to streamline productivity and team coordination.

## Overview

Jinear is a comprehensive solution that combines task management with calendar functionality, enhanced with additional features for improved workflow management. Originally developed as a personal project to address specific productivity needs, it has evolved into a versatile platform suitable for both individual and team use.

**🌐 Try the hosted version:** [jinear.co](https://jinear.co)

## Features

- **Task Management**: Create, organize, and track tasks with ease
- **Integrated Calendar**: Seamlessly manage events and deadlines
- **Collaboration Tools**: Work together with team members
- **Additional Productivity Features**: Enhanced workflow capabilities

## Self-Hosting

### Prerequisites

- Docker
- Docker Compose

### Installation

1. **Create the project directory:**
   ```bash
   mkdir jinear
   cd jinear
   ```

2. **Download the Docker Compose configuration:**
   ```bash
   sudo curl --output docker-compose.yaml "https://gitlab.com/140crafts/use-jinear/-/raw/main/example/docker-compose.yaml?ref_type=heads"
   ```

3. **Create necessary directories:**
   ```bash
   mkdir .config && mkdir .data && mkdir .data/caddy && mkdir .data/caddy/conf
   ```

4. **Download configuration files:**
   ```bash
   sudo curl --output .config/Caddyfile "https://gitlab.com/140crafts/use-jinear/-/raw/main/example/.config/Caddyfile?ref_type=heads"
   sudo curl --output .config/application.properties "https://gitlab.com/140crafts/use-jinear/-/raw/main/example/.config/application.properties?ref_type=heads"
   sudo curl --output .config/db-backup.sh "https://gitlab.com/140crafts/use-jinear/-/raw/main/example/.config/db-backup.sh?ref_type=heads"
   ```

5. **Copy Caddy configuration:**
   ```bash
   cp .config/Caddyfile .data/caddy/conf
   ```

6. **Configure your installation:**

   Edit the following files according to your requirements:
    - `.config/application.properties`
    - `docker-compose.yaml`
    - `.data/caddy/conf/Caddyfile`

7. **Set proper permissions and start the application:**
   ```bash
   sudo chown -R 1001:1001 .data/
   docker-compose up -d
   ```

### Configuration

After installation, customize your Jinear instance by modifying:

- **Application Properties**: Database connections, API keys, and application-specific settings
- **Docker Compose**: Service configurations, ports, and environment variables
- **Caddy Configuration**: Reverse proxy settings, SSL certificates, and domain routing

## Getting Started

Once your Jinear instance is running, access it through your configured domain.

## Support

For questions, issues, or contributions, please visit our [GitLab repository](https://gitlab.com/140crafts/use-jinear).

## License

This repository is licensed under [AGPL-3.0](https://gitlab.com/140crafts/use-jinear/-/raw/main/LICENCE?ref_type=heads).