FROM node:18-alpine as builder

WORKDIR /app
COPY ./package.json /app

# COPY {대상 파일} {복사할 위치}

RUN yarn install
COPY . .
RUN yarn build

#### nginx 

FROM nginx
EXPOSE 3000
COPY --from=builder /app/build /usr/share/nginx/html

# nginx 의 default.conf 를 삭제
RUN rm /etc/nginx/conf.d/default.conf

# default config 파일 복사 명령어
COPY default.conf /etc/nginx/conf.d

CMD ["nginx", "-g", "daemon off;"]