<template>
<div>
     <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入项目名称"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addProject">新增项目</el-button>
        </el-form-item>
    </el-form>
    <el-table size="small" :data="projectData" v-loading="loading">
        <el-table-column prop="index" label="序号" width="50px" align="center"/>
        <el-table-column prop="name" label="项目名称" min-width="150px"/>
        <el-table-column prop="description" label="项目描述" min-width="200px"/>
        <el-table-column prop="status" label="项目状态">
            <template slot-scope="scope">
                <span v-if="scope.row.status === 0"> 禁用</span>
                <span v-else> 启用中</span>
            </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="100px">
            <template slot-scope="scope">
                <el-button v-if="scope.row.status === 0" type="text" size="mini" @click="recoverProject(scope.row.id)">恢复</el-button>
                <el-button v-else type="text" size="mini" @click="deleteProject(scope.row.id)">停用</el-button>
            </template>
        </el-table-column>
    </el-table>
    <pagination @callFather="callFather" :pageparam="pageparam"></pagination>
    <el-dialog title="新增项目" :visible.sync="projectVisible" width="600px" destroy-on-close>
        <el-form label-width="120px" style="padding-right: 30px;" :model="projectForm" label-position="top" :rules="rules" ref="projectForm">
            <el-form-item label="项目名称" prop="name">
                <el-input size="small" style="width: 100%" v-model="projectForm.name" placeholder="请输入项目名称"/>
            </el-form-item>
            <el-form-item label="项目描述" prop="description">
                <el-input size="small" style="width: 100%" :autosize="{ minRows: 4}" type="textarea" clearable placeholder="请输入项目描述" 
                        v-model="projectForm.description" maxlength="200" show-word-limit/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="projectVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="submitProjectForm('projectForm', projectForm)">确定</el-button>
        </div>
    </el-dialog>
</div>
</template>
<script>
import pagination from '@/components/common/pagination' 
export default {
    components: {
        pagination
    },
   
    data() {
        return {
            searchForm: {
                page: 1,
                limit: 10,
                condition: ""

            },
            projectData: [{
            }],
            projectForm: {
                name:"",
                description:""
            },
            projectVisible: false,
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
        }
    },
    
    methods: {
        getdata(searchForm){
            let param = {
                condition: searchForm.condition
            };
            this.$post("/auto_test/project/list/"+searchForm.page+"/"+searchForm.limit,param,res=>{
                this.projectData = res.data.list
                this.pageparam.currentPage=res.data.page;
                this.pageparam.pageSize=res.data.size;
                this.pageparam.total=res.data.total;


            }
            )


        },
        reset(){

        },
        search(){
            this.getdata(this.searchForm)

        },
        addProject(){ 
            this.projectForm = {};
            this.projectVisible = true;
        },
        submitProjectForm(projectForm){
            this.$post("/auto_test/project/add",this.projectForm,res=>{

            })

        },
        callFather(parm){
            this.searchForm.page = parm.currentPage;
            this.searchForm.limit = parm.pageSize;
            this.getdata(this.searchForm);
        }
    },
    created(){
        this.getdata(this.searchForm)

    },
}
</script>