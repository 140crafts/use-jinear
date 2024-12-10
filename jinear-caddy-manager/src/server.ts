import express, {Request, Response} from 'express';
import {authInterceptor} from "./interceptor/AuthInterceptor";

const app = express();
app.use(express.json())

const {
    PORT = 3001
} = process.env;

app.listen(PORT, () => {
    console.log({JINEAR_PAGES_APP_URL: process.env.JINEAR_PAGES_APP_URL});
    console.log(`Server is running on ${PORT}`);
});

app.get('/', (req: Request, res: Response) => {
    res.send("ok");
});

app.put('/config', [
    authInterceptor(),
    (req: Request, res: Response) => {
        const {hosts} = req.body;
        console.log({hosts})
        const conf = getConfig(hosts);
        // res.status(200).send({message: "ok"});
        return res.status(200).send({message: conf});
    }
]);