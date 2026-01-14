"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const winston_1 = __importDefault(require("winston"));
const dotenv = require('dotenv');
dotenv.config();
const morgan = require('morgan');
const { combine, timestamp, json, errors } = winston_1.default.format;
const { PORT = 3001, INTERNAL_AUTH_TOKEN = 'debug', CORE_ENDPOINT = 'http://localhost:8085', ALLOWED_ORIGIN = "https://jinear.co", } = process.env;
const logger = winston_1.default.createLogger({
    level: 'debug',
    format: combine(errors({ stack: true }), timestamp(), json()),
    defaultMeta: { service: 'jinear-message-service' },
    transports: [
        new winston_1.default.transports.Console(),
    ],
});
const morganMiddleware = morgan(':method :url :status :res[content-length] - :response-time ms', {
    stream: {
        write: (message) => logger.http(message.trim()),
    },
});
const app = (0, express_1.default)();
app.disable('etag');
app.set("port", PORT);
app.use(express_1.default.json(), morganMiddleware);
app.use((req, _res, next) => {
    logger.debug({
        method: req.method,
        path: req.path,
        traceId: req.headers['x-b3-traceid'],
        spanId: req.headers['x-b3-spanid']
    });
    next();
});
let http = require("http").Server(app);
let io = require("socket.io")(http, {
    path: '/ws',
    cors: {
        origin: ALLOWED_ORIGIN,
        methods: ["GET", "POST", "OPTIONS"],
        allowedHeaders: ["X-Token", "Cookie"],
        credentials: true
    }
});
const parseCookie = (cookie) => {
    return cookie === null || cookie === void 0 ? void 0 : cookie.split(';').map((pair) => {
        const indexOfEquals = pair.indexOf('=');
        let name;
        let value;
        if (indexOfEquals === -1) {
            name = '';
            value = pair.trim();
        }
        else {
            name = pair.substr(0, indexOfEquals).trim();
            value = pair.substr(indexOfEquals + 1).trim();
        }
        const firstQuote = value.indexOf('"');
        const lastQuote = value.lastIndexOf('"');
        if (firstQuote !== -1 && lastQuote !== -1) {
            value = value.substring(firstQuote + 1, lastQuote);
        }
        return { name, value };
    });
};
const retrieveJWT = (cookies) => {
    return cookies === null || cookies === void 0 ? void 0 : cookies.find(value => value.name == "JWT");
};
const getAccountId = (jwt) => __awaiter(void 0, void 0, void 0, function* () {
    const response = yield fetch(`${CORE_ENDPOINT}/v1/account`, {
        "headers": {
            "accept": "*/*",
            "Authorization": `Bearer ${jwt}`
        },
        "method": "GET"
    });
    const body = yield response.json();
    return body.data.accountId;
});
app.get('/', (req, resp) => {
    const cookie = req.headers.cookie;
    logger.log({ level: 'info', message: `cookie: ${cookie}` });
    return resp.status(200).send("up");
});
app.get('/info', (req, resp) => {
    let rooms = io.sockets.adapter.rooms;
    logger.info({ reqIp: req.ip, rooms: Array.from(rooms.entries()) });
    return resp.status(200).send('logs sent');
});
app.post('/emit', (req, resp) => {
    var _a, _b;
    const authToken = (_b = (_a = req.headers.authorization) === null || _a === void 0 ? void 0 : _a.split("Bearer ")) === null || _b === void 0 ? void 0 : _b[1];
    console.log({ authToken, headers: req.headers });
    let { channel, topic, message } = req.body;
    const tokenValid = authToken == INTERNAL_AUTH_TOKEN;
    logger.info({ tokenValid, channel, topic, message });
    if (!tokenValid) {
        return resp.status(401).send('Access denied');
    }
    try {
        io.to(channel).emit(topic, message);
    }
    catch (error) {
        logger.error(`emit failed. io.to failed. ${error}`);
    }
    return resp.status(200).send("ok");
});
io.on('connection', (socket) => __awaiter(void 0, void 0, void 0, function* () {
    var _a, _b;
    const cookie = parseCookie((_b = (_a = socket === null || socket === void 0 ? void 0 : socket.handshake) === null || _a === void 0 ? void 0 : _a.headers) === null || _b === void 0 ? void 0 : _b.cookie);
    const JWT = retrieveJWT(cookie);
    if (JWT) {
        try {
            const accountId = yield getAccountId(JWT.value);
            logger.info({ newConnection: accountId });
            try {
                socket.join(accountId);
            }
            catch (err) {
                logger.error(`socker.join has failed. ${err}`);
            }
            return;
        }
        catch (e) {
            logger.error(e);
        }
    }
    socket.disconnect();
}));
http.listen(PORT, function () {
    console.log(`listening on *:${PORT}`);
});
//# sourceMappingURL=index.js.map