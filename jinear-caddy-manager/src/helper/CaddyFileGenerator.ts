const {
    JINEAR_PAGES_APP_URL,
    CF_ACCESS_KEY,
    BASE_PAGES_URL_PATTERN,
    BASE_S3_PATH,
    BASE_S3_PATH_REVERSE_PROXY_TO,
    BASE_PAGES_TLS_IS_CLOUDFLARE,
    BASE_UI_PATH,
    BASE_UI_PATH_REVERSE_PROXY_TO,
    BASE_API_PATH,
    BASE_API_PATH_REVERSE_PROXY_TO
} = process.env;

const GENERIC_JINEAR_PAGES_CONFIG =
    `
{
    debug
    admin 0.0.0.0:2019
}

${BASE_UI_PATH} {
    tls {
      on_demand
    }
    reverse_proxy {
        to ${BASE_UI_PATH_REVERSE_PROXY_TO}
        header_up Host {http.reverse_proxy.upstream.host}
        header_up X-Real-IP {http.reverse-proxy.upstream.address}
    }
}
 
${BASE_API_PATH} {
    tls {
      on_demand
    }
    reverse_proxy {
        to ${BASE_API_PATH_REVERSE_PROXY_TO}
        header_up Host {http.reverse_proxy.upstream.host}
        header_up X-Real-IP {http.reverse-proxy.upstream.address}
    }
} 
    
${BASE_PAGES_URL_PATTERN} {
    tls {
        ${BASE_PAGES_TLS_IS_CLOUDFLARE == 'true' ? `dns cloudflare ${CF_ACCESS_KEY}` : `on_demand`}
    }
    reverse_proxy {
        to ${JINEAR_PAGES_APP_URL}
        header_up Host {http.reverse_proxy.upstream.host}
        header_up X-Real-IP {http.reverse-proxy.upstream.address}
    }
}

${BASE_S3_PATH} {
    tls {
      on_demand
    }
    reverse_proxy {
        to ${BASE_S3_PATH_REVERSE_PROXY_TO}
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