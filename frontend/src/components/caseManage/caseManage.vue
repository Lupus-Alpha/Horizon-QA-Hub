<template>
  <div>
    <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入用例NO、名称"/>
        </el-form-item>
        <el-form-item label="">
            <el-select size="small" clearable style="width:120px" v-model="searchForm.caseType" placeholder="用例类型">
                <el-option v-for="item in caseTypes" :key="item" :label="item" :value="item"/>
            </el-select>
        </el-form-item>
        <el-form-item label="" v-if="searchForm.caseType==='APP'">
            <el-select size="small" clearable style="width:120px" v-model="searchForm.system" placeholder="操作系统">
                <el-option v-for="item in systems" :key="item" :label="item" :value="item"/>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addCase">新增用例</el-button>
        </el-form-item>
    </el-form>
    <!-- 用例模块 -->
    <el-col :span="4" class="left-tree">
        <module-tree title="用例模块" :treeData="treeData" :currentModule="searchForm.moduleId" @clickModule="clickModule($event)" @appendModule="appendModule($event)"
             @removeModule="removeModule(arguments)" @dragNode="dragNode(arguments)"/>
    </el-col>
    <!-- 用例列表 -->
    <el-col :span="20" class="right-table">
        <!--列表-->
        <el-table size="small" :data="caseData" v-loading="loading" element-loading-text="拼命加载中">
            <el-table-column prop="num" label="NO" width="60px"/>
            <el-table-column prop="name" label="用例名称" min-width="200px"/>
            <el-table-column prop="type" label="用例类型">
                <template slot-scope="scope">
                    <span v-if="scope.row.type==='APP'">{{scope.row.type}}({{scope.row.system}})</span>
                    <span v-else>{{scope.row.type}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="moduleName" label="所属模块"/>
            <el-table-column prop="createUser" label="创建人"/>
            <el-table-column prop="updateTime" label="更新时间"  width="150px"/>
            <el-table-column fixed="right" align="operation" label="操作" width="140px">
                <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="runCase(scope.row)">执行</el-button>
                    <el-button type="text" size="mini" @click="editCase(scope.row)">编辑</el-button>
                    <el-button type="text" size="mini" @click="deleteCase(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <Pagination :pageparam="pageParam" @callFather="callFather"/>
    </el-col>
    <!-- 添加模块弹框 -->
    <module-append title="添加用例模块" :show.sync="moduleVisible" :moduleForm="moduleForm" @closeDialog="closeDialog" @submitModule="submitModule($event)"/>
    <!-- 添加用例弹框 -->
    <el-dialog title="选择用例类型" :visible.sync="caseVisible" width="500px" destroy-on-close>
        <el-radio-group style="margin-left:15px;" v-model="newCaseType">
            <el-radio :label="'API'">API</el-radio>
            <el-radio :label="'WEB'">WEB</el-radio>
            <el-radio :label="'ANDROID'">APP(android)</el-radio>
            <el-radio :label="'APPLE'">APP(apple)</el-radio>
        </el-radio-group>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" type="primary" @click="submitCase">确定</el-button>
        </div>
    </el-dialog>
    <!-- 用例执行选择引擎和环境 -->
    <run-form :runForm="runForm" :runVisible="runVisible" :showEnvironment="showEnvironment" :deviceSystem="deviceSystem"
         :showDevice="showDevice" @closeRun="runVisible=false" @run="run($event)"/>
    <!-- 用例执行结果展示 -->
    <result-detail :taskId="reportId" :caseType="caseType" :resultVisable="resultVisable" @closeResult="resultVisable=false"/>
  </div>
</template>

<script>
import {timestampToTime} from '@/utils/util'
import Pagination from '@/components/common/pagination'
import moduleTree from './common/module/moduleTree.vue'
import ModuleAppend from './common/module/moduleAppend'

export default {
    // 注册组件
    components: {
        Pagination, ModuleAppend, moduleTree
    },
    data() {
        return{
            loading:false,
            moduleVisible: false,
            caseVisible: false,
            moduleForm: {
                name: "",
                parentId: "",
                parentName: ""
            },
            newCaseType:"API",
            caseTypes:["API", "WEB", "APP"],
            systems: ["android", "apple"],
            searchForm: {
                page: 1,
                limit: 10,
                condition: "",
                caseType: "",
                moduleId: "",
                system: ""
            },
            caseData: [],
            pageParam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            treeData: [],
            runVisible: false,
            showEnvironment: false,
            showDevice: false,
            deviceSystem: null,
            runForm: {
                engineId: "",
                environmentId: null,
                deviceId: null
            },
            caseType: null,    // 记录当前执行的用例的类型  
            reportId: null, // 记录当前执行后生成的报告 从而轮询测试结果
            resultVisable: false
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["用例中心", "用例管理"])
        this.getTree()
        this.getdata(this.searchForm)
    },
    methods: {
        // 点击模块
        clickModule(data){
            this.searchForm.moduleId = data.id;
            this.getdata(this.searchForm);
        },
        // 添加模块
        appendModule(data) {
            if (data){
                this.moduleForm.parentId = data.id;
                this.moduleForm.parentName = data.label;
                this.moduleForm.data = data;
            }else{
                this.moduleForm.parentId = 0;
                this.moduleForm.parentName = "根节点";
                this.moduleForm.data = "";
            }
            this.moduleVisible = true;
        },
        // 删除模块
        removeModule(args) {
            let node = args[0];
            let data = args[1];
            if(data.children.length != 0){
                this.$message.warning("当前模块有子模块, 无法删除");
                return;
            }
            let url = '/autotest/module/delete';
            this.$post(url, data, response =>{
                this.getTree();
                this.$message.success("模块删除成功")
            });
        },
        // 拖拽模块
        dragNode(args){
            let dragNode = args[0];
            let newParent = args[1];
            let url = '/autotest/module/save';
            let moduleForm = dragNode.data;
            moduleForm.parentId = newParent;
            this.$post(url, moduleForm, response =>{
                this.$message.success("更改成功")
            });
        },
        // 关闭弹框
        closeDialog(){
            this.moduleVisible = false;
        },
        // 提交模块保存
        submitModule(moduleForm) {
            moduleForm.projectId = this.$store.state.projectId;
            moduleForm.moduleType = 'case_module';
            let url = '/auto_test/module/save';
            this.$post(url, moduleForm, response =>{
                this.getTree();
                this.moduleVisible = false;
                this.moduleForm.name = "";
            });
        },
        // 获取树数据
        getTree(){
            let url = '/auto_test/module/list/case/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.treeData = response.data;
            });
        },
        // 获取列表数据方法
        getdata(searchParam) {
            this.loading = true;
            let url = '/auto_test/case/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                condition: searchParam.condition,
                caseType: searchParam.caseType,
                moduleId: searchParam.moduleId,
                projectId: this.$store.state.projectId,
                system: searchParam.system
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i=0;i<data.list.length;i++){
                    data.list[i].updateTime = timestampToTime(data.list[i].updateTime);
                }
                this.caseData = data.list;
                this.loading = false
                // 分页赋值
                this.pageParam.currentPage = this.searchForm.page;
                this.pageParam.pageSize = this.searchForm.limit;
                this.pageParam.total = data.total;
            });
        },
        // 分页插件事件
        callFather(parm) {
            this.searchForm.page = parm.currentPage
            this.searchForm.limit = parm.pageSize
            this.getdata(this.searchForm)
        },
        // 搜索按钮
        search() {
            this.getdata(this.searchForm)
        },
        // 重置按钮
        reset() {
            this.searchForm.condition = "";
            this.searchForm.caseType = "";
            this.searchForm.moduleId = "";
            this.searchForm.system = "";
            this.getdata(this.searchForm);
        },
        // 新增用例
        addCase(){
            this.newCaseType = "API";
            this.caseVisible = true;
        },
        // 提交用例类型
        submitCase() {
            if (this.newCaseType == "API"){
                this.$router.push({path: '/caseCenter/caseManage/apiCase/add'});
            }else if (this.newCaseType == "WEB"){
                this.$router.push({path: '/caseCenter/caseManage/webCase/add'});
            }else if (this.newCaseType == "ANDROID"){ 
                this.$router.push({path: '/caseCenter/caseManage/appCase/android/add'});
            }else if (this.newCaseType == "APPLE"){
                this.$router.push({path: '/caseCenter/caseManage/appCase/apple/add'});
            }
        },
        // 编辑用例
        editCase(row){
            if (row.type === "API"){
                this.$router.push({path: '/caseCenter/caseManage/apiCase/edit/' + row.id});
            }else if (row.type === "WEB"){
                this.$router.push({path: '/caseCenter/caseManage/webCase/edit/' + row.id});
            }else{
                if(row.system === "android"){
                    this.$router.push({path: '/caseCenter/caseManage/appCase/android/edit/' + row.id});
                }else if (row.system === "apple"){
                    this.$router.push({path: '/caseCenter/caseManage/appCase/apple/edit/' + row.id});
                }
            }
        },
        runCase(row){
            // 记录当前执行用例的类型
            this.caseType = row.type;
            // 用例调试
            this.runForm.engineId = 'system';
            this.runForm.environmentId = null;
            this.runForm.deviceId = null;
            
            this.runForm.sourceType = 1;
            this.runForm.sourceId = row.id;
            this.runForm.sourceName = row.name;
            this.runForm.projectId = this.$store.state.projectId;
            if(row.type === 'API' || row.type === 'WEB'){
                this.showEnvironment = true;
                this.showDevice = false;
                this.deviceSystem = null;
            }else{
                this.showEnvironment = false;
                this.showDevice = true;
                this.deviceSystem = row.system;
            }
            this.runVisible = true;
        },
        run(runForm){
            let url = '/auto_test/run';
            this.$post(url, runForm, response =>{
                this.reportId = response.data;
                console.log(response.data)
                this.resultVisable = true;   // todo 打开轮询
            });
            this.runVisible = false;
        },
        // 删除用例
        deleteCase(id){
            this.$confirm('确定要删除用例吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/auto_test/case/delete/'+id;
                this.$post(url, {}, response => {
                    this.$message.success("删除成功");
                    this.getdata(this.searchForm);
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
.left-tree {
    padding-right: 5px;
    border-right:1px solid rgb(219, 219, 219);
}
.right-table {
    padding-left: 5px;
}
.case-name {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
    text-align: left;
}
</style>
