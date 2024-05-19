import express from "express";
import winston from "winston";
import {WinstonTransport as AxiomTransport} from '@axiomhq/winston';

const dotenv = require('dotenv');
dotenv.config();

const morgan = require('morgan')


const {combine, timestamp, json, errors} = winston.format;

const {
    PORT = 3001,
    INTERNAL_AUTH_TOKEN = 'debug',
    AXIOM_TOKEN = '',
    AXIOM_ORG_ID = '',
    CORE_ENDPOINT = 'http://localhost:8085'
} = process.env;

const logger = winston.createLogger({
    level: 'debug',
    format: combine(errors({stack: true}), timestamp(), json()),
    defaultMeta: {service: 'jinear-message-service'},
    transports: [
        new AxiomTransport({
            dataset: 'jinear_be',
            token: AXIOM_TOKEN,
            orgId: AXIOM_ORG_ID,
        }),
        new winston.transports.Console(),
    ],
});

const morganMiddleware = morgan(
    ':method :url :status :res[content-length] - :response-time ms',
    {
        stream: {
            // Configure Morgan to use our custom logger with the http severity
            write: (message: string) => logger.http(message.trim()),
        },
    }
);

const app = express();
app.disable('etag');
app.set("port", PORT);
app.use(express.json(), morganMiddleware)

let http = require("http").Server(app);
let io = require("socket.io")(http, {
    path: '/ws',
    cors: {
        origin: "https://jinear.co",
        methods: ["GET", "POST"],
        allowedHeaders: ["X-Token","Cookie"],
        credentials: true
    }
});

interface IParsedCookie {
    name: string;
    value: string
}

const parseCookie = (cookie?: string) => {
    return cookie?.split(';').map((pair) => {
        const indexOfEquals = pair.indexOf('=');
        let name;
        let value;
        if (indexOfEquals === -1) {
            name = '';
            value = pair.trim();
        } else {
            name = pair.substr(0, indexOfEquals).trim();
            value = pair.substr(indexOfEquals + 1).trim();
        }
        const firstQuote = value.indexOf('"');
        const lastQuote = value.lastIndexOf('"');
        if (firstQuote !== -1 && lastQuote !== -1) {
            value = value.substring(firstQuote + 1, lastQuote);
        }
        return {name, value};
    });
}

const retrieveJWT = (cookies?: IParsedCookie[]) => {
    return cookies?.find(value => value.name == "JWT")
}

const getAccountId = async (jwt: string): Promise<string> => {
    const response = await fetch(`${CORE_ENDPOINT}/v1/account`, {
        "headers": {
            "accept": "*/*",
            "Authorization": `Bearer ${jwt}`
        },
        "method": "GET"
    });
    const body = await response.json();
    return body.data.accountId;
}

app.get('/', (req, resp) => {
    const cookie = req.headers.cookie;
    logger.log({level: 'info', message: `cookie: ${cookie}`});

    return resp.status(200).send("up");
})

app.get('/info', (req, resp) => {
    let rooms = io.sockets.adapter.rooms;
    logger.info({reqIp: req.ip, rooms: Array.from(rooms.entries())});
    return resp.status(200).send('logs sent');
})

app.post('/emit', (req, resp) => {
    const authToken = req.headers.authorization?.split("Bearer ")?.[1];
    let {channel, topic, message} = req.body;
    const tokenValid = authToken == INTERNAL_AUTH_TOKEN;
    logger.info({tokenValid, channel, topic, message});
    if (!tokenValid) {
        return resp.status(401).send('Access denied');
    }
    try {
        io.to(channel).emit(topic, message);
    } catch (error) {
        logger.error(error);
    }
    return resp.status(200).send("ok");
})

io.on('connection', async (socket: any) => {
    const cookie = parseCookie(socket?.handshake?.headers?.cookie);
    const JWT = retrieveJWT(cookie);
    if (JWT) {
        try {
            const accountId = await getAccountId(JWT.value);
            logger.info({newConnection: accountId});
            socket.join(accountId);
            return;
        } catch (e) {
            logger.error(e)
        }
    }
    socket.disconnect();
});

http.listen(PORT, function () {
    console.log(`listening on *:${PORT}`);
});