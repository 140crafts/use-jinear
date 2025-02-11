const {
    JINEAR_PAGES_APP_URL,
    CF_ACCESS_KEY
} = process.env;

const GENERIC_JINEAR_PAGES_CONFIG =
    `
{
    debug
    admin 0.0.0.0:2019
}
    
https://*.projects.jinear.co {
    tls {
        dns cloudflare ${CF_ACCESS_KEY}
    }
    reverse_proxy {
        to ${JINEAR_PAGES_APP_URL}
        header_up Host {http.reverse_proxy.upstream.host}
        header_up X-Real-IP {http.reverse-proxy.upstream.address}
    }
}

https://files.jinear.co {
    reverse_proxy {
        to https://storage.googleapis.com
        header_up Host {http.reverse_proxy.upstream.host}
        header_up X-Real-IP {http.reverse-proxy.upstream.address}
    }
}

`;

const PROJECT_SERVER_CONFIG =
    `
https://$_{hostname} {
    tls {
      on_demand
    }
    reverse_proxy {
        to ${JINEAR_PAGES_APP_URL}
        header_up Host {http.reverse_proxy.upstream.host}
        header_up X-Real-IP {http.reverse-proxy.upstream.address}
    }
}
`;

const generateServerConfig = (hostname: string) => {
    return PROJECT_SERVER_CONFIG.replace("$_{hostname}", hostname);
}

export const getConfig = (hostnameList?: string[]) => {
    const hostConfig = hostnameList?.map(h => generateServerConfig(h)).join("") || "";
    return GENERIC_JINEAR_PAGES_CONFIG + hostConfig;
}