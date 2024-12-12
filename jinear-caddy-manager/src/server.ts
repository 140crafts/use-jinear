import express, {Request, response, Response} from 'express';
import {authInterceptor} from "./interceptor/AuthInterceptor";
import {getConfig} from "./helper/CaddyFileGenerator";
import fs from 'fs';

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
    if (response.status == 200) {
        console.log("Load config successful writing config to file.")
        fs.writeFile('/conf/Caddyfile', config, err => {
            if (err) {
                console.error("Writing config file failed.")
                console.error(err);
            } else {
                console.error("Writing config file has completed.")
            }
        });
    }
    console.log(response.status);
}