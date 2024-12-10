import express, {Request, response, Response} from 'express';
import {authInterceptor} from "./interceptor/AuthInterceptor";
import {getConfig} from "./helper/CaddyFileGenerator";

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


const echoConfig = async () => {
    const resp = await fetch("http://localhost:2019/config/");
    console.log(resp.status);
    console.log(resp.body);
    console.log(await resp.body);
}