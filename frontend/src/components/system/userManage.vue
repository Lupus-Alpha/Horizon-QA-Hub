<template>
<div>
     <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入用户名称"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button v-if="currentUser==='system_admin_user'" size="small" type="primary" icon="el-icon-plus" @click="addUser">新增用户</el-button>
        </el-form-item>
    </el-form>
    <el-table size="small" :data="userData" v-loading="loading">
        <el-table-column prop="id" label="序号" width="50px" align="center"/>
        <el-table-column prop="username" label="用户名" min-width="120px"/>
        <el-table-column prop="account" label="登录账号" width="150px"/>
        <el-table-column prop="mobile" label="手机号" width="120px"/>
        <el-table-column prop="email" label="邮箱" min-width="200px"/>
        <el-table-column prop="createTime" label="创建时间" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="150px">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="resetPwd(scope.row.id)">重置密码</el-button>
                <el-button v-if="currentUser==='system_admin_user'" type="text" size="mini" @click="deleteUser(scope.row.id)">删除用户</el-button>
            </template>
        </el-table-column>
    </el-table>
    <pagination @callFather="callFather" :pageparam="pageparam"></pagination>
    <el-dialog title="查看用户" :visible.sync="userVisible" width="600px" destroy-on-close>
        <el-form label-width="120px" style="padding-right: 30px;" :model="userForm" label-position="left" :rules="rules" ref="userForm">
            <el-form-item label="用户名" prop="username">
                <el-input size="small" style="width: 100%" v-model="userForm.username" placeholder="请输入用户姓名"/>
            </el-form-item>
            <el-form-item label="登录账号" prop="account">
                <el-input size="small" style="width: 100%" v-model="userForm.account" placeholder="请输入姓名全拼"/>
            </el-form-item>
            <el-form-item label="手机号" prop="mobile">
                <el-input size="small" style="width: 100%" v-model="userForm.mobile" placeholder="请输入手机号"/>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input size="small" style="width: 100%" v-model="userForm.email" placeholder="请输入邮箱"/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="userVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="submitUserForm('userForm', userForm)">确定</el-button>
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
            userData: [{
            }],
            userForm: {
                username:"",
                account:"",
                mobile:'',
                email: '',
            },
            currentUser: null,
            userVisible: false,
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
            this.$post("/auto_test/user/list/"+searchForm.page+"/"+searchForm.limit,param,res=>{
                this.userData = res.data.list
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
        deleteUser(id){
            this.$post("/auto_test/user/delete/"+id,'',res =>{
                this.$message.success("删除成功");
                this.getdata(this.searchForm)
            })

        },
        addUser(){ 
            this.userForm = {};
            this.userVisible = true;
        },
        submitUserForm(){
            this.$post("/auto_test/user/add",this.userForm,res=>{
                this.userVisible=false;
                this.$message.success("保存成功");
                this.getdata(this.searchForm)

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
        this.currentUser=this.$store.state.user.id;

    },
}
</script>