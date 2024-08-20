/**
 * 配置中心  系统设置
 */
<template>
  <div>
    <el-tabs v-model="activeName" tab-position="left">
      <el-tab-pane label="服务标识" name="serverSign">
        <server-sign-setting :showOpt="showOpt" :activeName="activeName"/>
      </el-tab-pane>
      <el-tab-pane label="应用信息" name="application">
        <application-setting :showOpt="showOpt" :activeName="activeName"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ServerSignSetting from './common/serverSignSetting'
import ApplicationSetting from './common/applicationSetting'

export default {
    components: {
      ServerSignSetting, ApplicationSetting
    },
    data() {
        return{
          activeName: "serverSign",
          showOpt: false
        }
    },
    created(){
      // this.$root.Bus.$emit('initBread', ["设置中心", "系统设置"]);
      this.getOptPerm();
    },
    methods: {
        getOptPerm() {
            if(this.$store.state.permissions.includes("SETTING_OPT")){
                this.showOpt = true;
            }else{
                this.showOpt = false;
            }
        },
    }
}
</script>

<style scoped>

</style>
