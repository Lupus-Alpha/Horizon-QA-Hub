// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import store from './vuex/store';
import 'element-ui/lib/theme-chalk/index.css';
import ajax from './utils/ajax';
import '../static/index.css'
Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(ajax);
/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})

router.beforeEach((to, from, next) => {
  if(to.matched.length !== 0){
    if(to.meta.requireAuth){
      if(store.state.token !=null){
          next();
      }else{
        next({
          path: '/login',
          query: { redirect: to.fullPath } 
      });
      }
    }else {
      next();
    }
  }else{
    next({
      path: '/login',
      query: { redirect: to.fullPath } 
    })
  }
})


