/**
 * 系统管理  角色管理
 */
<template>
  <div>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
    </el-form>
    <!--列表-->
    <el-table size="small" :data="roleData" v-loading="loading">
        <el-table-column prop="index" label="序号" width="50px" align="center"/>
        <el-table-column prop="name" label="角色名称" min-width="150px"/>
        <el-table-column prop="projectName" label="所属项目" min-width="200px"/>
        <el-table-column prop="createTime" label="创建时间" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="150px">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="viewUser(scope.row.id)">查看用户</el-button>
                <el-button type="text" size="mini" @click="addUser(scope.row.id)">添加用户</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination :pageparam="pageparam" @callFather="callFather"/>
    <!-- 查看用户界面 -->
    <el-dialog title="查看用户" :visible.sync="userVisible" width="600px" destroy-on-close>
        <el-table size="small" :data="userData">
            <el-table-column prop="username" label="用户名" min-width="150px"/>
            <el-table-column prop="account" label="用户账号" min-width="150px"/>
            <el-table-column prop="mobile" label="手机号" width="150px"/>
            <el-table-column fixed="right" align="operation" label="操作" width="100px">
                <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="deleteUser(scope.row)">删除用户</el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-dialog>
    <el-dialog title="新增用户" :visible.sync="addVisible" width="400px" destroy-on-close>
        <el-form label-width="120px" style="padding-right: 30px;" :model="userForm" label-position="left" :rules="rules" ref="userForm">
            <el-form-item label="选择用户">
                <el-select size="small" v-model="userForm.userIds" multiple filterable placeholder="请选择用户">
                    <el-option v-for="item in userList" :key="item.id" :label="item.username" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="addVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="submitUserForm('userForm', userForm)">确定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '../common/pagination'
import {timestampToTime} from '@/utils/util'
export default {
    components: {
        Pagination
    },
    data() {
        return{
            loading:false,
            userVisible: false,
            searchForm: {
                page: 1,
                limit: 10,
                condition: ""
            },
            roleData: [],
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            userData: [],
            roleId: null,
            userForm: {
                roleId: null,
                userIds: []
            },
            userList:[],
            addVisible: false,
            rules: {
                userIds: [{ required: true, message: '用户不能为空', trigger: 'blur' }],
            }
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["系统管理", "角色管理"]);
        this.getdata(this.searchForm);
    },
    methods: {
        // 获取列表数据方法
        getdata(searchParam) {
            this.loading = true
            let url = '/auto_test/role/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                condition: searchParam.condition,
                projectId: this.$store.state.projectId
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i =0; i<data.list.length; i++){
                    data.list[i].createTime= timestampToTime(data.list[i].createTime);
                    data.list[i].index = (searchParam.page-1) * searchParam.limit + i+1;
                }
                this.roleData = data.list;
                this.loading = false;
                // 分页赋值 保障子组件里的值与父组件的值一致
                this.pageparam.currentPage = this.searchForm.page;
                this.pageparam.pageSize = this.searchForm.limit;
                this.pageparam.total = data.total;
            });
        },
        // 分页插件事件
        callFather(parm) {
            this.searchForm.page = parm.currentPage;
            this.searchForm.limit = parm.pageSize;
            this.getdata(this.searchForm);
        },
        // 搜索按钮
        search() {
            this.getdata(this.searchForm);
        },
        // 重置按钮
        reset() {
            this.searchForm.condition = "";
            this.getdata(this.searchForm);
        },
        viewUser(id){
            // 请求角色用户列表数据
            this.roleId = id;
            let url = "/auto_test/role/user/list/"+ id;
            this.$get(url, response =>{
                this.userData = response.data;
                this.userVisible = true;
            })
        },
        addUser(id){
            // 给角色添加用户
            this.userForm.roleId = id;
            this.userForm.userIds = [];
            this.getUserAll();
            this.addVisible = true;
        },
        getUserAll(){
            let url = '/auto_test/user/all/list';
            this.$get(url, response => {
                this.userList = response.data;
            })
        },
        deleteUser(row){
            this.$confirm('确定要删除角色用户吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/auto_test/role/user/delete';
                let data = {
                    userId: row.id,
                    roleId: this.roleId
                }
                this.$post(url, data, response => {
                    this.$message.success("删除成功");
                    this.viewUser(this.roleId);
                });
            })
            .catch(() => {
                this.$message.success("取消成功");
            })
        },
        submitUserForm(confirm, form){
            this.$refs[confirm].validate(valid => {
                if (valid) {
                    let url = '/auto_test/role/add/user';
                    this.$post(url, form, response =>{
                        this.$message.success("保存成功");
                        this.addVisible = false;
                    });
                }else{
                    return false;
                }
            });
        }
    }
}
</script>

<style scoped>

</style>