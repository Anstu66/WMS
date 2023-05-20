import Vue from 'vue';

import './assets/global.css';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from "axios";

import VueRouter from 'vue-router';
import router from './router';

import store from './store/index';

Vue.use(VueRouter);

Vue.prototype.$axios=axios;
Vue.config.productionTip = false
Vue.prototype.$httpUrl='http://localhost:8090';
Vue.use(ElementUI,{size:'small'});

new Vue({
  router,
  store,
  el: '#app',
  render: h => h(App)
});
