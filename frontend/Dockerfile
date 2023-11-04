### STAGE 1: Build ###
FROM node:latest as build
RUN mkdir /usr/src/app
WORKDIR /usr/src/app
ENV PATH /usr/src/app/node_modules/.bin:$PATH
COPY package.json /usr/src/app/package.json
RUN npm install
COPY . /usr/src/app
RUN npm run build

### STAGE 2: NGINX ###
FROM nginx
WORKDIR /usr/share/nginx/html
COPY --from=build /usr/src/app/dist/ .
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]