FROM  --platform=linux/arm64 node:20.11.1

WORKDIR /usr/src/app

COPY package.json ./
COPY yarn.lock ./

RUN yarn

COPY ../.. .
#COPY .env.production .env

RUN yarn build

ENV NODE_ENV production

EXPOSE 3001
CMD [ "node", "dist/server.js" ]
USER node