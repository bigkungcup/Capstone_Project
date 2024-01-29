// https://nuxt.com/docs/api/configuration/nuxt-config
import vuetify, { transformAssetUrls } from "vite-plugin-vuetify";
export default defineNuxtConfig({
  //...
  build: {
    transpile: ["vuetify"],
  },
  css: ["~/assets/css/main.css", "~/assets/css/global.css"],
  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},
    },
  },
  modules: [
    (_options, nuxt) => {
      nuxt.hooks.hook("vite:extendConfig", (config) => {
        // @ts-expect-error
        config.plugins.push(vuetify({ autoImport: true }));
      });
    },
    "@pinia/nuxt",
    //...
  ],
  hooks: {
    close: () => {},
  },
  vite: {
    server: {
      fs: {
          allow: ["C:/Files/Uploads"]
      },
      headers: {
        'Access-Control-Allow-Origin': '*',
      }
  },
    vue: {
      template: {
        transformAssetUrls,
      },
    },
  },
  nitro: {
    // devProxy: {
    //     '/api/': {
    //       target: "https://capstone23.sit.kmutt.ac.th/ej2/",
    //       target: `http://localhost:8080`,
    //        changeOrigin: true,
    //        secure: false,
    //     }
    // },
    routeRules: {
      "/api/**": {
        // proxy: "https://capstone23.sit.kmutt.ac.th/ej2/**",
        proxy: { to: "http://localhost:8080/api/**", },
      },
    },
    prerender: {
      autoSubfolderIndex: true,
    },
  },
  app: {
    baseURL: "/ej2/",
    head:{
      title:'Bannarug',
      link: [{ rel: 'icon', type: 'image/png', href: '/ej2/image/logo.png' }]
    },
  },
  runtimeConfig: {
    public: {
      API_BASE_URL: process.env.API_BASE_URL,
    },
  },
  ssr: false,
  devServer: {
    port: 3000,
  },
  webpack: {
    devMiddleware: {
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    },
  },
});
