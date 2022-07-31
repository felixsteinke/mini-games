FROM node:16.14-alpine as builder

WORKDIR /app
COPY ./ .

RUN npm install
RUN npm run build

FROM nginx:1.21-alpine as runner

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/dist /usr/share/nginx/html

RUN chgrp -R root /var/cache/nginx /var/run /var/log/nginx && \
    chmod -R 770 /var/cache/nginx /var/run /var/log/nginx

EXPOSE 80
