# Jinear - Task Manager, Calendar, Messaging & File Sharing

A collaborative task management and calendar application designed to streamline productivity and team coordination, now enhanced with features like real-time messaging and file sharing.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/projects-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/projects-light.png">
  <img alt="Jinear Feature Image" src="https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/projects-light.png" height="550" width="795">
</picture>

## Overview

Jinear is a comprehensive solution that combines task management with calendar functionality, enhanced with additional
features for improved workflow management. Originally developed as a personal project to address specific productivity
needs, it has evolved into a versatile platform suitable for both individual and team use.

**🌐 Try the hosted version:** [jinear.co](https://jinear.co)

## Features

- **Task Management**: Create, organize, and track tasks with ease
- **Integrated Calendar**: Seamlessly manage events and deadlines
- **Collaboration Tools**: Work together with team members
- **Additional Productivity Features**: Enhanced workflow capabilities

## Self-Hosting

### Quick Install (Recommended)

You can follow along the steps below. There's also a video walkthrough [you can watch here](https://youtu.be/a8DNaoWvK7I).

Download and run the installer on your server:

```bash
curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh -o install.sh && chmod +x install.sh && ./install.sh
```

Or as separate steps:

```bash
# Download
curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh -o install.sh

# Make executable
chmod +x install.sh

# Run
./install.sh
```

The installer will:

- ✅ Check prerequisites (Docker, Docker Compose, etc.)
- ✅ Prompt for your domain and configuration
- ✅ Generate secure passwords and secrets automatically
- ✅ Create all necessary files and directories
- ✅ Start all services

### Requirements

- **Docker** 20.10+ and **Docker Compose** 2.0+
- **5GB+ disk space** (10GB+ recommended)
- **2GB+ RAM** (4GB+ recommended)
- **Ports 80 & 443** available
- A domain name pointed to your server

### After Installation

Your Jinear instance will be available at:

- 🌐 **Application**: `https://your-domain.com`
- 🔧 **API**: `https://api.your-domain.com`
- 📁 **Files**: `https://files.your-domain.com`

### Manual Installation

For advanced users who prefer manual setup, see the [docs/manual-setup](docs/manual-setup/) folder or the detailed guide
in [jinear-installation-scripts](./jinear-installation-scripts/).

### Running Behind Traefik

Check out the docs [docs/behind-traefik](docs/behind-traefik/)

### Configuration

After installation, you can customize your instance by modifying:

- **`.env`**: Domain, passwords, and environment settings
- **`.config/application.properties`**: Application-specific settings
- **`.data/caddy/conf/Caddyfile`**: Reverse proxy and SSL configuration

### Troubleshooting

**SSL Certificate Issues:**

- If you see staging/untrusted certificates, ensure your Caddyfile has:
  `acme_ca https://acme-v02.api.letsencrypt.org/directory`
- If you hit Let's Encrypt rate limits (50 certs/week), either wait 7 days or temporarily use staging server
- Check detailed
  troubleshooting: [jinear-installation-scripts/README.md](./jinear-installation-scripts/README.md#troubleshooting)

**View Logs:**

```bash
cd <install-dir>
docker compose logs -f jinear-caddy  # SSL/proxy logs
docker compose logs -f jinear-core    # Backend logs
```

## Architecture

Jinear consists of several services that work together. These services come preconfigured with the easy installation
script. If you want to change any of the optional services, you can stop containers and change the configuration. Or you
can create your own compose file and configure from scratch.

| Service              | Required                                                                           | Description                                                                             | Default Port       |
|----------------------|------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|--------------------|
| **jinear-core**      | Required                                                                           | Main backend API handling authentication, task management, calendar, and business logic | 8008 Internal      |
| **jinear-web**       | Required                                                                           | Frontend application providing the main user interface                                  | 3000 Internal      |
| **jinear-db**        | Can be replaced with external PostgreSQL (Needs config change).                    | PostgreSQL database storing all application data                                        | 5432 Internal      |
| **jinear-redis**     | Can be replaced with external Redis (Needs config change).                         | Redis cache for session management and real-time data                                   | 6379 Internal      |
| **jinear-minio**     | Can be replaced with external MinIO or Google Cloud Storage (Needs config change). | MinIO object storage for file uploads and attachments                                   | 9000/9001 Internal |
| **jinear-caddy**     | Can be changed with other web servers. Replacing needs custom configuration.       | Caddy reverse proxy handling SSL/TLS termination and routing                            | 80/443 External    |
| **jinear-db-backup** | Optional (Recommended)                                                             | Automated database backup service with configurable retention                           | -                  |

## Getting Started

Once your Jinear instance is running, access it through your configured domain.

## Support

> **⚠️ Note** \
> I didn’t start this project with the intention of open-sourcing it or making it self-hostable. Most of it was built in
> my spare time, before or after work. As a result, the configuration process isn’t as seamless as it could be. I may
> improve it over time.

For questions, issues, or contributions, please visit our [GitLab repository](https://gitlab.com/140crafts/use-jinear).

You can also contact me from [x.com/cagdasplus](https://x.com/cagdasplus)

## License

This repository is licensed under [AGPL-3.0](https://gitlab.com/140crafts/use-jinear/-/raw/main/LICENCE?ref_type=heads).