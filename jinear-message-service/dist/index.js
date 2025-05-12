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
const winston_2 = require("@axiomhq/winston");
const dotenv = require('dotenv');
dotenv.config();
const morgan = require('morgan');
const { combine, timestamp, json, errors } = winston_1.default.format;
const { PORT = 3001, INTERNAL_KEY = 'debug', AXIOM_TOKEN = '', AXIOM_ORG_ID = '' } = process.env;
const logger = winston_1.default.createLogger({
    level: 'debug',
    format: combine(errors({ stack: true }), timestamp(), json()),
    defaultMeta: { service: 'jinear-message-service' },
    transports: [
        new winston_2.WinstonTransport({
            dataset: 'jinear_be',
            token: AXIOM_TOKEN,
            orgId: AXIOM_ORG_ID,
        }),
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
let http = require("http").Server(app);
let io = require("socket.io")(http, { path: '/ws' });
app.get('/', (req, resp) => {
    const cookie = req.headers.cookie;
    logger.log({ level: 'info', message: `cookie: ${cookie}` });
    return resp.status(200).send("up");
});
app.get('/info', (req, resp) => {
    let srvSockets = io.sockets.sockets;
    let rooms = io.sockets.adapter.rooms;
    console.log(Object.assign({ srvSockets, rooms }, req.body));
    return resp.status(200).send({ srvSockets, rooms });
});
app.post('/emit', (req, resp) => {
    var _a;
    console.log(Object.assign(Object.assign({}, req.body), { keyValid: (((_a = req === null || req === void 0 ? void 0 : req.body) === null || _a === void 0 ? void 0 : _a.key) == INTERNAL_KEY) }));
    let { key, channel, topic, message } = req.body;
    if (key != INTERNAL_KEY) {
        return resp.status(404);
    }
    try {
        io.to(channel).emit(topic, message);
    }
    catch (error) {
        console.log({ error });
    }
    return resp.status(200).send("ok");
});
io.on('connection', (socket) => __awaiter(void 0, void 0, void 0, function* () {
    var _a;
    console.log({ headers: (_a = socket === null || socket === void 0 ? void 0 : socket.handshake) === null || _a === void 0 ? void 0 : _a.headers });
    socket.join('accId');
}));
http.listen(PORT, function () {
    console.log(`listening on *:${PORT}`);
});
//# sourceMappingURL=index.js.map