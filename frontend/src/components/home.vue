<template>
<el-container class="main">
  <el-aside :class="showclass">
    <leftnav></leftnav>
  </el-aside>
  <el-container class="right">
    <el-header  class="right-header">
      <topnav></topnav>
    </el-header>
    <el-main class="right-main">
      <router-view></router-view>
    </el-main>
  </el-container>
</el-container>
</template>
<script>
import leftnav from '@/components/navbar/leftnav'
import topnav from '@/components/navbar/topnav'
export default {
  name: 'Home',
  components: {
    leftnav,
    topnav
  },
  data() {
    return {
      showclass: "side"
    }

  },
  methods: {
    change(val) {
      if(val) {
        this.showclass = "sides"
      } else {
        this.showclass = "side"
      }
    }
  },
  beforeCreate() {
    this.$store.commit('set_token', localStorage.getItem('token'));
    this.$store.commit('set_user', JSON.parse(localStorage.getItem('user')));
    this.$store.commit('set_projectId', JSON.parse(localStorage.getItem('user')).lastProject);
    this.$store.commit('set_permissions', JSON.parse(localStorage.getItem('user')).permission);
  }
}
</script>

<style>
.main {
    height: 100%;
    width: 100%;
    box-sizing: border-box;
}
 .right-header {
    /* background-color: #B3C0D1;
    color: #333;
    line-height: 60px; */
    max-height: 40px;
  }

.side {
  max-width: 10%;
  height: 100%;
  /* background-color: #2c3e50; */

}
.sides {
  max-width: 2%;
  height: 100%;
}
.right {
    height: 100%;
    width: 100%;
    max-width: 100%;
}
.right-main {
    height: 80%;
    max-width: 100%;
  
}
</style>
