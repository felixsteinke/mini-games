FROM node:16.14-alpine

WORKDIR /app
COPY ./ .

RUN npm run build

EXPOSE 3000
CMD [ "node", "app.js" ]
