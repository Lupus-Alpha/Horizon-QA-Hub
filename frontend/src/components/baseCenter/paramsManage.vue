<template>
  <div>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入参数名称"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addParam">新增参数</el-button>
        </el-form-item>
    </el-form>
    <el-tabs v-model="groupName" @tab-click="handleClick">
        <el-tab-pane label="自定义参数" name="custom">
            <param-table :paramData="paramData" :pageparam="pageparam" :loading="loading" @editParam="editParam($event)"
                @deleteParam="deleteParam($event)" @callFather="callFather($event)"/>
        </el-tab-pane>
        <el-tab-pane label="Header参数" name="header">
            <param-table :paramData="paramData" :pageparam="pageparam" :loading="loading" @editParam="editParam($event)"
                @deleteParam="deleteParam($event)" @callFather="callFather($event)"/>
        </el-tab-pane>
        <el-tab-pane label="Proxy参数" name="proxy">
            <param-table :paramData="paramData" :pageparam="pageparam" :loading="loading" @editParam="editParam($event)"
                @deleteParam="deleteParam($event)" @callFather="callFather($event)"/>
        </el-tab-pane>
    </el-tabs>
    <!-- 添加参数界面 -->
    <el-dialog :title="title" :visible.sync="paramVisible" width="800px" destroy-on-close>
      <el-form label-width="120px" style="padding-right: 30px;" :model="paramForm" :rules="rules" ref="paramForm">
          <el-form-item label="参数名" prop="name">
            <el-input size="small" style="width: 90%" v-model="paramForm.name" placeholder="请输入参数名"/>
          </el-form-item>
          <el-form-item label="参数类型" prop="dataType">
            <el-select size="small" style="width: 90%" v-model="paramForm.dataType" placeholder="请选择参数类型">
              <el-option v-for="item in dataTypes" :key="item" :label="item" :value="item"/>
            </el-select>
          </el-form-item>
          <el-form-item label="参数值" prop="paramData">
            <el-input size="small" style="width: 90%" v-model="paramForm.paramData" :autosize="{ minRows: 6}" type="textarea" clearable placeholder="请输入参数值"/>
          </el-form-item>
          <el-form-item label="参数描述" prop="description">
            <el-input size="small" style="width: 90%" :autosize="{ minRows: 4}" type="textarea" clearable placeholder="请输入参数描述" 
                v-model="paramForm.description" maxlength="200" show-word-limit/>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button size="small" @click="paramVisible=false">取消</el-button>
          <el-button size="small" type="primary" @click="submitParamForm('paramForm', paramForm)">保存</el-button>
      </div> 
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/common/pagination'
import {timestampToTime} from '@/utils/util'
import ParamTable from './common/paramTable'
export default {
    components: {
        Pagination, ParamTable
    },
    data() {
        return{
            loading:false,
            paramVisible: false,
            searchForm: {
                page: 1,
                limit: 10,
                condition: ""
            },
            dataTypes: ["String", "Int", "Float", "Boolean", "JSONObject", "JSONArray"],
            paramData: [],
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            title: null,
            groupName: "custom",
            paramForm: {
                name: null,
                dataType: "String",
                paramData: null,
                description: null,
                groupName: "custom"
            },
            rules: {
                name: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }],
                dataType: [{ required: true, message: '参数类型不能为空', trigger: 'blur' }],
                paramData: [{ required: true, message: '参数值不能为空', trigger: 'blur' }],
            }
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["公共参数", "参数管理"]);
        this.getdata(this.searchForm);
    },
    methods: {
        handleClick(v){
            this.getdata(this.searchForm);
        },
        // 获取列表数据方法
        getdata(searchParam) {
            this.loading = true
            
            let url = '/auto_test/params/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                groupName: this.groupName,
                condition: searchParam.condition,
                projectId: this.$store.state.projectId
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i =0; i<data.list.length; i++){
                    data.list[i].updateTime= timestampToTime(data.list[i].updateTime);
                    data.list[i].index = (searchParam.page-1) * searchParam.limit + i+1;
                }
                this.paramData = data.list;
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
        // 新增参数
        addParam(){
            this.title = "新增参数";
            this.paramForm = {
                name: null,
                dataType: "String",
                paramData: null,
                description: null,
                groupName: this.groupName
            };
            this.paramVisible = true;
        },
        editParam(row){
            this.title = "编辑参数";
            this.paramForm = {
                id: row.id,
                name: row.name,
                dataType: row.dataType,
                paramData: row.paramData,
                description: row.description,
                groupName: row.groupName,
                createTime: row.createTime,
                createUser: row.createUser
            };
            this.paramVisible = true;
        },
        submitParamForm(confirm, form){
            this.$refs[confirm].validate(valid => {
                if (valid) {
                    let url = '/auto_test/params/add';
                    form.projectId = this.$store.state.projectId;
                    this.$post(url, form, response =>{
                        this.$message.success("保存成功");
                        this.paramVisible = false;
                        this.getdata(this.searchForm);
                    });
                }else{
                    return false;
                }
            });
        },
        deleteParam(paramId){
            this.$confirm('确定要删除参数吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/auto_test/params/delete/'+paramId;
                this.$post(url, {}, response => {
                    this.$message.success("删除成功");
                    this.getdata(this.searchForm);
                });
            })
            .catch(() => {
                this.$message.success("取消成功");
            })
        }
    }
}
</script>

<style scoped>

</style>