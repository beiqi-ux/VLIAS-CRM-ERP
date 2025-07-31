import { createApp } from "vue";
import App from "./App.vue";
import axios from "axios";

// 从localStorage恢复token
const token = localStorage.getItem("token");
if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

createApp(App).mount("#app");
