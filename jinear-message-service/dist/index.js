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
const { PORT = 3001, INTERNAL_KEY = 'debug', } = process.env;
const app = (0, express_1.default)();
app.set("port", PORT);
app.use(express_1.default.json());
let http = require("http").Server(app);
let io = require("socket.io")(http, { path: '/ws' });
app.get('/', (req, resp) => {
    console.log(Object.assign({}, req.query));
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
    socket.join('accId');
}));
http.listen(PORT, function () {
    console.log(`listening on *:${PORT}`);
});
//# sourceMappingURL=index.js.map