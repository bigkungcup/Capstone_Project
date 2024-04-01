# ARG NODE_VERSION=18.14.2
# FROM node:${NODE_VERSION}-slim as base
# ARG PORT=3000
# ENV NODE_ENV=production
# WORKDIR /src

# # Build
# FROM base as build
# COPY --link package.json package-lock.json ./
# RUN npm install --production=false
# COPY --link . .
# RUN npm run build
# RUN npm prune

# # Run
# FROM base
# ENV PORT=$PORT
# COPY --from=build /src/.output /src/.output
# # Optional, only needed if you rely on unbundled dependencies
# # COPY --from=build /src/node_modules /src/node_modules
# EXPOSE $PORT
# CMD [ "node", ".output/server/index.mjs" ]

FROM node:lts-alpine AS build
WORKDIR /frontend
COPY package*.json ./
ARG NODE_ENV
RUN if [ "$NODE_ENV" = "production" ]; then \
    cp .env.production .env; \
    else \
    cp .env .env; \
    fi
RUN npm install
COPY . .
# RUN npm run build
RUN npm run generate

FROM nginx:stable-alpine as production-stage
# COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /frontend/.output/public /usr/share/nginx/html
EXPOSE 3000