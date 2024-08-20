<template>
    <div>
        <el-form :inline="true" :model="searchForm">
      <el-form-item label="">
        <el-radio-group size="small" v-model="searchForm.uiType" @change="changeUIType">
          <el-radio-button label="web"/>
          <el-radio-button label="app"/>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="">
        <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入操作名称"/>
      </el-form-item>
      <el-form-item v-if="searchForm.uiType==='app'" label="">
        <el-select size="small" clearable style="width:120px" v-model="searchForm.system" placeholder="设备系统">
          <el-option v-for="item in systems" :key="item" :label="item" :value="item"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="search">搜索</el-button>
        <el-button size="small" @click="reset">重置</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <el-button size="small" type="primary" icon="el-icon-plus" @click="addOperation">新增操作</el-button>
      </el-form-item>
    </el-form>
  <el-tabs v-model="operationType" @tab-click="handleClick">
    <el-tab-pane :label="sysName" :name="sysValue">
      <operation-table :operationData="operationData" :loading="loading" :pageparam="pageparam" :uiType="searchForm.uiType"
          @callFather="callFather($event)" @deleteOperation="deleteOperation($event)" />
    </el-tab-pane>
    <el-tab-pane :label="pageName" :name="pageValue">
      <operation-table :operationData="operationData" :loading="loading" :pageparam="pageparam" :uiType="searchForm.uiType"
          @callFather="callFather($event)" @deleteOperation="deleteOperation($event)" />
    </el-tab-pane>
    <el-tab-pane label="关联" name="relation">
      <operation-table :operationData="operationData" :loading="loading" :pageparam="pageparam" :uiType="searchForm.uiType"
          @callFather="callFather($event)" @deleteOperation="deleteOperation($event)" />
    </el-tab-pane>
    <el-tab-pane label="断言" name="assertion">
      <operation-table :operationData="operationData" :loading="loading" :pageparam="pageparam" :uiType="searchForm.uiType"
          @callFather="callFather($event)" @deleteOperation="deleteOperation($event)" />
    </el-tab-pane>
  </el-tabs>
    </div>
</template>
<script>
import operationTable from '@/components/common/operationTable'
import {timestampToTime} from '@/utils/util'
export default {
name :'operationManage',
components:{
  operationTable
},
data() {
    return {
      operationType: "browser",
      operationData: [],
      systems: ["android", "apple"],
      loading: false,
      searchForm: {
        page: 1,
        limit: 10,
        condition: "",
        uiType: "web",
        system: ""
      },
      pageparam: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      sysName: "浏览器操作",
      sysValue: "browser",
      pageName: "网页操作",
      pageValue: "page"
    }
    
},
methods: {
    reset(){
      this.searchForm.condition="";
      this.getdata(this.searchForm);


    },
    changeUIType(val){
      if(val === "web"){
        this.sysName = "浏览器操作";
        this.sysValue = "browser";
        this.pageName = "网页操作";
        this.pageValue = "page";
      }else{
        this.sysName = "系统操作";
        this.sysValue = "system";
        this.pageName = "视图操作";
        this.pageValue = "view";
      }
      this.operationType = this.sysValue;
      this.searchForm.system = "";
      this.searchForm.page = 1;
      this.searchForm.limit = 10;
      this.pageparam.currentPage = 1;
      this.pageparam.pageSize = 10;
      this.pageparam.total = 0;
      this.getdata(this.searchForm);
    },
    search(){
      this.getdata(this.searchForm);

    },
    handleClick(){
       this.searchForm.page = 1;
      this.searchForm.limit = 10;
      this.pageparam.currentPage = 1;
      this.pageparam.pageSize = 10;
      this.pageparam.total = 0;
      this.getdata(this.searchForm);
    },
    addOperation(){
      this.$router.push({path: '/baseCenter/operation/' + this.searchForm.uiType + '/add/' + this.operationType});
    },
    getdata(searchParam){
      let url = '/auto_test/operation/list/' + searchParam.page + '/' + searchParam.limit;
      let param = {
          projectId: this.$store.state.projectId,
          condition: searchParam.condition,
          operationType: this.operationType,
          uiType: searchParam.uiType,
          system: searchParam.system
      };
      this.$post(url, param, response => {
        let data = response.data;
          for(let i =0; i<data.list.length; i++){
              data.list[i].updateTime = timestampToTime(data.list[i].updateTime);
              data.list[i].index = (searchParam.page-1) * searchParam.limit + i+1;
          }
          this.operationData = data.list;
          this.loading = false;
          // 分页赋值
          this.pageparam.currentPage = this.searchForm.page;
          this.pageparam.pageSize = this.searchForm.limit;
          this.pageparam.total = data.total;

      })

    }
}
}
</script>