<template>
  <div>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入计划名称"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addplan">新增计划</el-button>
        </el-form-item>
    </el-form>
    <!--计划列表-->
    <el-table size="small" :data="planData" v-loading="loading">
        <el-table-column label="序号" prop="index" width="50px" align="center"/>
        <el-table-column label="计划名称" prop="name" min-width="160px"/>
        <el-table-column label="计划描述" prop="description" min-width="180px"/>
        <el-table-column label="创建人" prop="username"/>
        <el-table-column label="更新时间" prop="updateTime" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="150px">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="runPlan(scope.row)">执行</el-button>
                <el-button type="text" size="mini" @click="editPlan(scope.row)">编辑</el-button>
                <el-button type="text" size="mini" @click="deletePlan(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination :pageparam="pageparam" @callFather="planCallFather"/>
    <!-- 计划执行选择引擎和环境 -->
    <run-form :runForm="runForm" :runVisible="runVisible" :showEnvironment="showEnvironment" @closeRun="runVisible=false" @run="run($event)"/>
  </div>
</template>

<script>
import {timestampToTime} from '@/utils/util'
import Pagination from '@/components/common/pagination'
import RunForm from '@/components/common/runForm'
export default {
    components: {
        Pagination,RunForm
    },
    data() {
        return{
            loading:false,
            searchForm: {
                page: 1,
                limit: 10,
                condition: ""
            },
            planData: [],
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            runVisible: false,
            showEnvironment: false,
            runForm: {
                engineId: "",
                environmentId: null,
                deviceId: null
            }
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["计划中心", "测试计划"]);
        this.getPlanData(this.searchForm)
    },
    methods: {
        // 获取列表数据方法
        getPlanData(searchParam) {
            this.loading = true
            let url = '/auto_test/plan/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                condition: searchParam.condition,
                projectId: this.$store.state.projectId
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i =0; i<data.list.length; i++){
                    data.list[i].updateTime = timestampToTime(data.list[i].updateTime);
                    data.list[i].index = (searchParam.page-1) * searchParam.limit + i+1;
                }
                this.planData = data.list;
                this.loading = false;
                // 分页赋值
                this.pageparam.currentPage = this.searchForm.page;
                this.pageparam.pageSize = this.searchForm.limit;
                this.pageparam.total = data.total;
            });
        },
        planCallFather(param) {
            this.searchForm.page = param.currentPage;
            this.searchForm.limit = param.pageSize;
            this.getPlanData(this.searchForm);
        },
        // 搜索按钮
        search() {
            this.getPlanData(this.searchForm);
        },
        // 重置按钮
        reset() {
            this.searchForm.condition = "";
            this.getPlanData(this.searchForm);
        },
        // 新增计划
        addplan(){
            this.$router.push({path: '/planManage/testplan/add'});
        },
        // 编辑计划
        editPlan(row){
            this.$router.push({path: '/planManage/testplan/edit/' + row.id});
        },
        runPlan(row){
            if(row.environmentId !== null){
                this.showEnvironment = true;
            }else{
                this.showEnvironment = false;
            }
            this.runForm.engineId = row.engineId;
            this.runForm.environmentId = row.environmentId;
            this.runForm.deviceId = null;
            this.runForm.sourceType = 3;
            this.runForm.sourceId = row.id;
            this.runForm.sourceName = row.name;
            this.runForm.projectId = this.$store.state.projectId;
            this.runVisible = true;
        },
        run(runForm){
            let url = '/autotest/run';
            this.$post(url, runForm, response =>{
                this.$message.success("执行成功 执行结果请查看报告");
            });
            this.runVisible = false;
        },
        // 删除计划
        deletePlan(id){
            this.$confirm('确定要删除计划吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/autotest/plan/delete/'+id;
                this.$post(url, {}, response => {
                    this.$message.success("删除成功");
                    this.getPlanData(this.searchForm);
                });
            })
            .catch(() => {
                this.$message.success("取消成功");
            })
        },
    }
}
</script>

<style scoped>
</style>