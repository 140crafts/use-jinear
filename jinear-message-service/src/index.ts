import express from "express";
import winston from "winston";

const morgan = require('morgan')


const {combine, timestamp, json, errors} = winston.format;

const {
    PORT = 3001,
    INTERNAL_KEY = 'debug',
    // CORE_SERVICE_URL
} = process.env;

const logger = winston.createLogger({
    level: 'debug',
    format: combine(errors({stack: true}), timestamp(), json()),
    transports: [new winston.transports.Console()],
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
let io = require("socket.io")(http, {path: '/ws'});

app.get('/', (req, resp) => {
    console.log({...req.query})
    return resp.status(200).send("up");
})

app.get('/info', (req, resp) => {
    let srvSockets = io.sockets.sockets;
    let rooms = io.sockets.adapter.rooms;
    console.log({srvSockets, rooms, ...req.body})
    return resp.status(200).send({srvSockets, rooms});
})

app.post('/emit', (req, resp) => {
    console.log({...req.body, keyValid: (req?.body?.key == INTERNAL_KEY)})
    let {key, channel, topic, message} = req.body;

    if (key != INTERNAL_KEY) {
        return resp.status(404);
    }
    try {
        io.to(channel).emit(topic, message);
    } catch (error) {
        console.log({error})
    }
    return resp.status(200).send("ok");
})

io.on('connection', async (socket: any) => {
    console.log({headers: socket?.handshake?.headers})
    // const accId = await getAccId(socket?.handshake?.headers?.cookie);
    // if (!accId) {
    //     socket.disconnect();
    //     return;
    // }

    socket.join('accId');
    // socket.emit("msg", `joined room ${accId}`);
});


http.listen(PORT, function () {
    console.log(`listening on *:${PORT}`);
});
