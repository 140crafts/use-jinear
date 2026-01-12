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

### Quick Install (Recommended)

Run this one-liner on your server to start the interactive installer:

```bash
curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh | bash
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
- 💬 **Message/WebSocket**: `https://message.your-domain.com`

### Manual Installation

For advanced users who prefer manual setup, see the [example](./example/) folder or the detailed guide in [jinear-installation-scripts](./jinear-installation-scripts/).

### Configuration

After installation, you can customize your instance by modifying:

- **`.env`**: Domain, passwords, and environment settings
- **`.config/application.properties`**: Application-specific settings
- **`.data/caddy/conf/Caddyfile`**: Reverse proxy and SSL configuration

### Troubleshooting

**SSL Certificate Issues:**
- If you see staging/untrusted certificates, ensure your Caddyfile has: `acme_ca https://acme-v02.api.letsencrypt.org/directory`
- If you hit Let's Encrypt rate limits (50 certs/week), either wait 7 days or temporarily use staging server
- Check detailed troubleshooting: [jinear-installation-scripts/README.md](./jinear-installation-scripts/README.md#troubleshooting)

**View Logs:**
```bash
cd <install-dir>
docker compose logs -f jinear-caddy  # SSL/proxy logs
docker compose logs -f jinear-core    # Backend logs
```

## Getting Started

Once your Jinear instance is running, access it through your configured domain.

## Support

> **⚠️ Note** \
I didn’t start this project with the intention of open-sourcing it or making it self-hostable. Most of it was built in my spare time, before or after work. As a result, the configuration process isn’t as seamless as it could be. I may improve it over time.

For questions, issues, or contributions, please visit our [GitLab repository](https://gitlab.com/140crafts/use-jinear). 

You can also contact me from [x.com/cagdasplus](https://x.com/cagdasplus)

## License

This repository is licensed under [AGPL-3.0](https://gitlab.com/140crafts/use-jinear/-/raw/main/LICENCE?ref_type=heads).