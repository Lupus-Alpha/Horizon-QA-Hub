<template>
  <div>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入文件名称"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addFile">上传文件</el-button>
        </el-form-item>
    </el-form>
    <!--列表-->
    <el-table size="small" :data="fileData" v-loading="loading">
        <el-table-column prop="id" label="UUID" width="260px" align="center"/>
        <el-table-column prop="name" label="文件名称" min-width="150px"/>
        <el-table-column prop="description" label="文件描述" min-width="200px"/>
        <el-table-column prop="createTime" label="创建时间" width="150px"/>
        <el-table-column prop="createUser" label="上传人" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="100px">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="deleteFile(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination :pageparam="pageparam" @callFather="callFather"/>
    <!-- 新增文件界面 -->
    <el-dialog title="新增文件" :visible.sync="fileVisible" width="510px" destroy-on-close>
        <el-form label-width="80px" style="padding-right: 30px;" :model="fileForm" label-position="left" :rules="rules" ref="fileForm">
            <el-form-item label="文件名称" prop="name">
                <el-input size="small" style="width: 100%" v-model="fileForm.name" placeholder="请输入文件名称"/>
            </el-form-item>
            <el-form-item label="文件描述" prop="description">
                <el-input size="small" style="width: 100%" :autosize="{ minRows: 2}" type="textarea" clearable placeholder="请输入文件描述" 
                        v-model="fileForm.description" maxlength="200" show-word-limit/>
            </el-form-item>
            <el-form-item label="文件" prop="fileList">
                <el-upload class="upload-demo" :file-list="fileForm.fileList" :before-upload="beforeUpload" :http-request="uploadFile"
                        :on-remove="removeFile" :on-exceed="handleExceed" drag action :limit="1" ref="upload">
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传单个文件，且不超过5M</div>
                </el-upload>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="fileVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="submitFileForm('fileForm', fileForm)">确定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/common/pagination'
import {timestampToTime} from '@/utils/util'
export default {
    components: {
        Pagination
    },
    data() {
        return{
            loading:false,
            fileVisible: false,
            searchForm: {
                page: 1,
                limit: 10,
                condition: ""
            },
            fileData: [],
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            fileForm: {
                name: null,
                description: null,
                fileList: []
            },
            rules: {
                name: [{ required: true, message: '文件名称不能为空', trigger: 'blur' }],
                fileList: [{ required: true, message: '文件不能为空', trigger: 'blur' }],
            }
        }
    },
    created() {
        this.$root.Bus.$emit('initBread', ["公共组件", "文件管理"]);
        this.getdata(this.searchForm);
    },
    methods: {
        // 获取列表数据方法
        getdata(searchParam) {
            this.loading = true
            let url = '/auto_test/file/list/' + searchParam.page + '/' + searchParam.limit;
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
                this.fileData = data.list;
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
        // 上传文件
        addFile(){
            this.fileForm = {
                name: null,
                description: "",
                fileList: []
            };
            this.fileVisible = true;
        },
        submitFileForm(confirm, form){
            this.$refs[confirm].validate(valid => {
                if (valid) {
                    let url = '/auto_test/file/upload';
                    let param = {
                        name: form.name,
                        description: form.description,
                        projectId: this.$store.state.projectId
                    }
                    this.$fileUpload(url, form.fileList[0], null, param,  response =>{
                        this.$message.success("上传成功");
                        this.fileVisible = false;
                        this.getdata(this.searchForm);
                    });
                }else{
                    return false;
                }
            });
        },
        deleteFile(fileId){
            this.$confirm('确定要删除文件吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/auto_test/file/delete/'+fileId;
                this.$post(url, {}, response => {
                    this.$message.success("删除成功");
                    this.getdata(this.searchForm);
                });
            })
            .catch(() => {
                this.$message.success("取消成功");
            })
        },
        // 上传前判断格式和大小
        beforeUpload(file) {
            if (file.size > 5 * 1024 * 1024) {
                this.$message.warning('文件大小超过5M 无法上传');
                return false;
            }
            return true;
        },
        handleExceed() {
            this.$message.warning('一次最多只能上传一个文件');
        },
        uploadFile(option) {
            this.fileForm.fileList.push(option.file);
            this.fileForm.name = option.file.name;
            this.$refs.fileForm.validateField('fileList');
        },
        removeFile() {
            this.fileForm.fileList = [];
        }
    }
}
</script>

<style scoped>

</style>