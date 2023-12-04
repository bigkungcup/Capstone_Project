import { createRouter, createWebHashHistory } from "vue-router";
// import NotFound from "../view/NotFound.vue";
import Home from "../views/Home.vue";
import Library from "../views/Library.vue"


const history = createWebHashHistory('');
const routes = [
  // {
  //   path: "/:catchNotMatchPath(.*)",
  //   name: "NotFound",
  //   component: NotFound,
  // },
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/library",
    name: "Library",
    component: Library,
  },

];

const router = createRouter({ history, routes });
export default router;