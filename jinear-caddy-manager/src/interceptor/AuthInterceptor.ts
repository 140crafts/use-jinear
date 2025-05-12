import {NextFunction, Request, Response} from "express";
import {AuthenticationError} from "../error/AuthenticationError";
import jwt, {JsonWebTokenError} from "jsonwebtoken";

const {
    INTERNAL_KEY
} = process.env;

export function authInterceptor() {
    return (req: Request, res: Response, next: NextFunction) => {
        try {
            validateAuth(req);
            next();
        } catch (error) {
            console.error(error);
            if (error instanceof AuthenticationError || error instanceof JsonWebTokenError) {
                res.status(401).send({message: error.message});
            }
            res.status(422).send({message: '1001; An error occurred'});
        }
    };
}

function validateAuth(req: Request) {
    if (INTERNAL_KEY) {
        const BEARER_PREFIX: string = "Bearer ";
        const authHeader = req.headers.authorization;
        if (authHeader) {
            const token = authHeader.substring(BEARER_PREFIX.length, authHeader.length);
            jwt.verify(token, INTERNAL_KEY);
            return;
        }
        throw new AuthenticationError('Auth header is required');
    }
    throw new AuthenticationError('Internal key is not defined.');
}