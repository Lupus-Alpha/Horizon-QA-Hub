<template>
    <div>
        <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入接口NO、名称、地址"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addApi">新增接口</el-button>
        </el-form-item>
    </el-form>
    <el-col :span="4" class="left-tree">
    <module-tree title="接口模块" :treeData="treeData" :currentModule="searchForm.moduleId" @clickModule="clickModule($event)" @appendModule="appendModule($event)"
        @removeModule="removeModule(arguments)" @dragNode="dragNode(arguments)"/>
    </el-col>
    <el-col :span="20" class="right-table">
    <el-table size="small" :data="apiListData" v-loading="loading" element-loading-text="拼命加载中">
        <el-table-column prop="num" label="NO" width="60px"/>
        <el-table-column prop="name" label="接口名称" min-width="180"/>
        <el-table-column prop="path" label="接口地址" min-width="150"/>
        <el-table-column prop="moduleName" label="所属模块"/>
        <el-table-column prop="createUser" label="创建人"/>
        <el-table-column prop="updateTime" label="更新时间" width="150"/>
        <el-table-column fixed="right" align="operation" label="操作" width="150">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="editApi(scope.row)">编辑</el-button>
                <el-button type="text" size="mini" @click="deleteApi(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <Pagination :pageparam="pageParam" @callFather="callFather"></Pagination>
    </el-col>

    <module-append :title="title" :show.sync="moduleVisible" :moduleForm="moduleForm" @closeDialog="closeDialog" @submitModule="submitModule($event)"/>
 
    </div>
</template>

<script>
import {timestampToTime} from '@/utils/util'
import Pagination from '@/components/common/pagination'
import moduleTree from './common/module/moduleTree.vue'
import ModuleAppend from './common/module/moduleAppend'
export default {
    components: {
        Pagination,
        moduleTree,
        ModuleAppend
    },
    created() {
        // 加载面包屑
        // this.$root.Bus.$emit('initBread', ["用例中心", "接口管理"])
        this.getTree()
        this.getdata(this.searchForm)
    },
    data() {
        return{
            loading:false,
            moduleVisible: false,
            moduleForm: {
                name: "",
                parentId: "",
                parentName: ""
            },
            title: '添加接口模块',
            searchForm: {
                page: 1,
                limit: 10,
                condition: "",
                moduleId: "",
            },
            apiListData: [],
            nodeID: "",
            pageParam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            treeData: [], 
        }
    },
    methods: {
        addApi() {
            this.$router.push({path: '/caseCenter/apiManage/add/'+ this.nodeID})
        },
        editApi(row){
            this.$router.push({path: '/caseCenter/apiManage/edit/' + row.id})
        },
        search() {
            this.getdata(this.searchForm)

        },
        reset() {
            this.searchForm.condition="",
            this.getdata(this.searchForm)

        },
        deleteApi(id) {
            this.$confirm('确定要删除接口吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/auto_test/api/delete/'+id;
                this.$post(url, {}, response => {
                    this.$message.success("删除成功");
                    this.getdata(this.searchForm);
                });
            })
            .catch(() => {
                this.$message.success("取消成功");
            })

        },
         getTree(){
          let url = '/auto_test/module/list/api_module/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.treeData = response.data;
            });
        },
        getdata(searchParam) {
            this.loading = true;
            let url = '/auto_test/api/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                condition: searchParam.condition,
                moduleId: searchParam.moduleId,
                projectId: this.$store.state.projectId
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i=0;i<data.list.length;i++){
                    data.list[i].updateTime = timestampToTime(data.list[i].updateTime);
                }
                this.apiListData = data.list;
                this.loading = false
                // 分页赋值
                this.pageParam.currentPage = this.searchForm.page;
                this.pageParam.pageSize = this.searchForm.limit;
                this.pageParam.total = data.total;
            });
        },
        submitModule(moduleForm) {
            moduleForm.projectId = this.$store.state.projectId;
            moduleForm.moduleType = 'api_module';
            let url = '/auto_test/module/save';
            this.$post(url, moduleForm, response =>{
                this.getTree();
                this.moduleVisible = false;
                this.moduleForm.name = "";
            });
        },
        appendModule(data){
            if (data){
                this.moduleForm.parentId = data.id;
                this.moduleForm.parentName = data.label;
            }else{
                this.moduleForm.parentId = 0;
                this.moduleForm.parentName = "根节点";
            }
            this.moduleVisible = true

        },
        clickModule(data){
            this.nodeID = data.id;
            this.searchForm.moduleId = data.id;
            this.getdata(this.searchForm);
        },

    }
}
</script>