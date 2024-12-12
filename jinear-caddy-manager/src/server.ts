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
        const config = getConfig(hosts);
        const result = loadConfig(config);
        return res.status(200).send({message: "ok"});
    }
]);


const loadConfig = async (config: string) => {
    const headers = new Headers();
    headers.append("Content-Type", "text/caddyfile");
    const requestOptions = {
        method: "POST",
        headers,
        body: config
    };
    const response = await fetch("http://jinear-caddy-custom:2019/load", requestOptions);
    console.log(response.status);
}