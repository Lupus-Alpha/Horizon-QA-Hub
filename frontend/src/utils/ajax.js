import { Message } from 'element-ui';
import axios from 'axios';

axios.defaults.baseURL = process.env.BASE_API;
axios.defaults.timeout = 30000;

function getUploadConfig(token, url, formData) {
  return {
    method: 'POST',
    url: url,
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
      'token': token
    }
  };
}

function getTokenConfig(token) {
  return {
    headers: {
      'token': token
    }
  };
}

function logout(store, router){
  store.commit('del_user');
  store.commit('del_token');
  if(router.currentRoute.path !== '/login'){
    router.push({ path: '/login', query:{ redirect: router.currentRoute.fullPath } });
  }
}

function then(success, response, result) {
  if (!response.data) {
    success(response);
  } else if (response.data.status === 0) {
    success(response.data);
  } else if (response.data.status>=1002 && response.data.status <=1004) {
    return false;
  } else if (response.data.status ===1000){
    Message.error(response.data.data);
  }else{
    Message.error(response.data.message);
  }
  result.loading = false;
  return true;
}

function exception(error, result) {
  result.loading = false;
  window.console.error(error);
  if (error.status && error.statusText) {
    console.log({message: error.status+error.statusText, showClose: true});
  }else{
    console.log(error.message);
  }
}

export function login(url, data, success) {
  let result = {loading: true};
  axios.post(url, data).then(response => {
    success(response);
  }).catch(error => {
    exception(error, result);
  });
  result.loading = false;
  return result;
}

export function get(url, success) {
  let result = {loading: true};
  let config = getTokenConfig(this.$store.state.token);
  if (!success) {
    return axios.get(url, config);
  } else {
    axios.get(url, config).then(response => {
      let res = then(success, response, result);
      if(res == false){
        logout(this.$store, this.$router);
      }
    }).catch(error => {
      exception(error, result, url);
    });
    return result;
  }
}

export function post(url, data, success) {
  let result = {loading: true};
  let config = getTokenConfig(this.$store.state.token);
  
  if (!success) {
    return axios.post(url, data, config);
  } else {
    axios.post(url, data, config).then(response => {
      let res = then(success, response, result);
      if(res == false){
        logout(this.$store, this.$router);
      }
    }).catch(error => {
      exception(error, result, url);
    });
    return result;
  }
}

export function request(axiosRequestConfig, success) {
  let result = {loading: true};
  if (!success) {
    return axios.request(axiosRequestConfig);
  } else {
    axios.request(axiosRequestConfig).then(response => {
      let res = then(success, response, result);
      if(res == false){
        logout(this.$store, this.$router);
      }
    }).catch(error => {
      exception(error, result);
    });
    return result;
  }
}

export function fileDownload(url) {
  let config = getTokenConfig(this.$store.state.token);
  config["responseType"] = 'blob';
  axios.get(url, config)
    .then(response => {
      let fileName = window.decodeURI(response.headers['content-disposition'].split('=')[1]);
      let link = document.createElement("a");
      link.href = window.URL.createObjectURL(new Blob([response.data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"}));
      link.download = fileName;
      link.click();
    });
}

export function fileUpload(url, file, files, param, success) {
  let formData = new FormData();
  if (file) {
    formData.append("file", file);
  }
  if (files) {
    files.forEach(f => {
      formData.append("files", f);
    });
  }
  for (var key in param) {
    formData.append(key, param[key]);
  }
  let axiosRequestConfig = getUploadConfig(this.$store.state.token, url, formData);
  return request(axiosRequestConfig, success);
}

export default {
  install(Vue) {

    if (!axios) {
      window.console.error('You have to install axios');
      return;
    }

    // axios.defaults.withCredentials = true;

    Vue.prototype.$login = login;

    Vue.prototype.$get = get;

    Vue.prototype.$post = post;

    Vue.prototype.$request = request;

    Vue.prototype.$fileDownload = fileDownload;

    Vue.prototype.$fileUpload = fileUpload;

    return axios;
  }
};
