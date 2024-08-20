<template>
  <div class="hello">
    <div class="title"><h1>{{ title }}</h1></div>
    <div class="login_form">
      <el-form :model="loginForm" status-icon  ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="account" style="width: 400px">
          <el-input type="text" v-model="loginForm.account" ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" style="width: 400px">
          <el-input type="password" v-model="loginForm.password" ></el-input>
        </el-form-item>
        <el-form-item style="width: 300px">
          <el-button type="primary" @click="login('loginForm')" style="width: 300px">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
    


    
  </div>
</template>

<script>
export default {
  name: 'Login',
  data () {
    return {
      title: '欢迎使用全能测试平台',
      loginForm: {
          account: '',
          password: ''
        }
      }
  },
  methods: {
    login(){
      this.$login("/auto_test/login",this.loginForm,response=>{
        if(response.data.status === 0){
          localStorage.setItem("user", JSON.stringify(response.data.data));
          localStorage.setItem("token", response.headers.token);
          this.$store.commit('set_token', response.headers.token);
          this.$store.commit('set_user', response.data.data);
          setTimeout(() => {
                this.logining = false;
                if(this.$router.currentRoute.query.redirect){
                  this.$router.push({ path: this.$router.currentRoute.query.redirect});
                }else{
                  this.$router.push({ path: '/'});
                }
              }, 1000)
          this.$router.push({ path: '/'});
        }else{
          this.$message.error(res.data.message);

        }

      })
    }

  }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
.demo-ruleForm{
  margin-left: 35%;
}
</style>
