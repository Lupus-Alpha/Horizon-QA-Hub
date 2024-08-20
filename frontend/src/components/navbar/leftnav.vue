<template>
  <div>
     <el-menu :collapse="isCollapse" collapse-transition router unique-opened class="el-menu-vertical-demo">
       <div>
         <img src="@/assets/logo.png" class="logoimg">
         <span :class="pftitle">全能测试平台</span>
       </div>
      <el-submenu v-for="menu in allmenu" :index="menu.name" :key="menu.id" class="el-menu-item-demo">
        <template slot="title"><i :class="menu.icon"></i><span>{{menu.name}}</span></template>
       <el-menu-item-group>
          <el-menu-item v-for="chmenu in menu.menus" :index="chmenu.path" :key="chmenu.id">
            <template slot="title">
              <i class="iconfont" :class="chmenu.icon"></i>
              <span>{{chmenu.name}}</span>
            </template>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      
     
    </el-menu>
  </div>
</template>
<script>
export default {
  name: 'leftnav',
  data () {
    return {
      isCollapse: false,
      pftitle: 'title-show',
      allmenu: [
      ]

    }

  },
   created() {
    // let user = JSON.parse(localStorage.getItem('user'));
    this.getMenus();
  },
  methods: {
    getMenus() {
      let url = "/auto_test/menu/list?projectId=132594b8-d7df-11ee-91b7-836d9bc2fba2"
      this.$get(url,res=>{
        this.allmenu = res.data;
      }
      );

    },
    // toggleCollapse() {
    //   if(this.isCollapse) {
    //     this.isCollapse = false
    //     this.pftitle = 'title-show'
    //   } else {
    //     this.isCollapse = true
    //     this.pftitle = 'title-hidden'
    //   }
    //   this.$emit('toggleCollapse', this.isCollapse)
    // }

  }
}
</script>
<style scoped>
.logoimg {
  color: #ffffff;
  height: 28px;
  border-radius: 50%;
  vertical-align: middle;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 140px;
  min-height: 400px;
}
.el-menu-vertical-demo{
  width: 45px;
}
.el-menu-item-demo .el-submenu__title{
  height: 48px;
  line-height: 48px;
  margin-left: 0%;
  /* padding-left: 0px !important; */
}

/* .title-hidden {
  vertical-align: middle;
  height: 0;
  width: 0;
  display: none;

} */
.title-show {
  vertical-align: middle;
  font-size: 13px;
}
</style>
