<template>
  <div>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入环境名称"/>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addEnv">新增环境</el-button>
        </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-table size="small" :data="envData" v-loading="loading">
        <el-table-column prop="index" label="序号" width="50px" align="center"/>
        <el-table-column prop="name" label="环境名称" min-width="150px"/>
        <el-table-column prop="description" label="环境描述" min-width="200px"/>
        <el-table-column prop="createUser" label="创建人" width="120px"/>
        <el-table-column prop="updateTime" label="更新时间" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="150px">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="viewDomain(scope.row.id)">查看域名</el-button>
                <el-button type="text" size="mini" @click="editEnv(scope.row)">编辑</el-button>
                <el-button type="text" size="mini" @click="deleteEnv(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination :pageparam="pageparam" @callFather="callFather"/>
    <!-- 添加环境界面 -->
    <el-dialog :title="title" :visible.sync="envVisible" width="800px" destroy-on-close>
      <el-form label-width="120px" style="padding-right: 30px;" :model="envForm" :rules="rules" ref="envForm">
          <el-form-item label="环境名" prop="name">
            <el-input size="small" style="width: 90%" v-model="envForm.name" placeholder="请输入环境名"/>
          </el-form-item>
          <el-form-item label="环境描述">
            <el-input size="small" style="width: 90%" :autosize="{ minRows: 4}" type="textarea" clearable placeholder="请输入环境描述" 
                v-model="envForm.description" maxlength="200" show-word-limit/>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button size="small" @click="envVisible=false">取消</el-button>
          <el-button size="small" type="primary" @click="submitEnvForm('envForm', envForm)">保存</el-button>
      </div> 
    </el-dialog>
    <!-- 查看域名列表界面 -->
    <el-dialog title="域名列表" :visible.sync="domainVisible" width="800px" destroy-on-close>
        <!-- 列表 -->
        <el-table size="small" :data="domainData" v-loading="domainLoading">
            <el-table-column prop="type" label="标识类型" width="100px">
                <template slot-scope="scope">
                    <span v-if="scope.row.type === 'path'">路由标识</span>
                    <span v-if="scope.row.type === 'sign'">服务标识</span>
                </template>
            </el-table-column>
            <el-table-column label="域名标识" width="100px">
                <template slot-scope="scope">
                    <span v-if="scope.row.type === 'path'">{{scope.row.domainKey}}</span>
                    <span v-if="scope.row.type === 'sign'">{{scope.row.serverSignName}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="domainValue" label="域名值" min-width="200px"/>
            <el-table-column prop="updateTime" label="更新时间" width="150px"/>
            <el-table-column fixed="right" align="operation" label="操作" width="100px">
                <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="editDomain(scope.row)">编辑</el-button>
                    <el-button type="text" size="mini" @click="deleteDomain(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button size="small" type="text" @click="addDomian">新增域名</el-button>
    </el-dialog>
    <!-- 添加域名界面 -->
    <el-dialog :title="title" :visible.sync="editDomainVisible" width="500px" destroy-on-close>
      <el-form label-width="120px" style="padding-right: 30px;" :model="domainForm" :rules="rules" ref="domainForm">
          <el-form-item label="标识类型" prop="type">
            <el-select size="small" style="width: 90%" v-model="domainForm.type" placeholder="请选择标识类型" @change="changeType">
                <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="域名标识" prop="domainKey">
              <el-select v-if="domainForm.type==='sign'" style="width: 90%" size="small" v-model="domainForm.domainKey" placeholder="请选择服务标识">
                <el-option v-for="item in signList" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
            <el-input v-else size="small" style="width: 90%" v-model="domainForm.domainKey" placeholder="请输入路由标识"/>
          </el-form-item>
          <el-form-item label="域名值" prop="domainValue">
            <el-input size="small" style="width: 90%" v-model="domainForm.domainValue" placeholder="请输入域名值"/>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button size="small" @click="editDomainVisible=false">取消</el-button>
          <el-button size="small" type="primary" @click="submitDomainForm('domainForm', domainForm)">保存</el-button>
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
            domainLoading:false,
            envVisible: false,
            domainVisible: false,
            editDomainVisible: false,
            currentEnvId: null,
            searchForm: {
                page: 1,
                limit: 10,
                condition: ""
            },
            signList: [],
            typeList: [
                {label: "路由标识", value: "path"},
                {label: "服务标识", value: "sign"}
            ],
            envData: [],
            domainData: [],
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            title: null,
            envForm: {
                id: null,
                name: null,
                description: null
            },
            domainForm: {
                id: null,
                type: "sign",
                domainKey: null,
                domainValue: null,
                environmentId: null
            },
            rules: {
                name: [{ required: true, message: '环境名称不能为空', trigger: 'blur' }],
                type: [{ required: true, message: '标识类型不能为空', trigger: 'blur' }],
                domainKey: [{ required: true, message: '域名标识不能为空', trigger: 'blur' }],
                domainValue: [{ required: true, message: '域名值不能为空', trigger: 'blur' }],
            }
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["环境中心", "环境管理"]);
        this.getEnvData(this.searchForm);
        this.getSignList();
    },
    methods: {
        getSignList(){
            let url = '/auto_test/server_sign/all/' + this.$store.state.projectId;
            this.$get(url, response => {
                this.signList = response.data;
            });
        },
        // 获取环境列表数据方法
        getEnvData(searchParam) {
            this.loading = true
            let url = '/auto_test/environment/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                condition: searchParam.condition,
                projectId: this.$store.state.projectId
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i =0; i<data.list.length; i++){
                    data.list[i].updateTime= timestampToTime(data.list[i].updateTime);
                    data.list[i].index = (searchParam.page-1) * searchParam.limit + i+1;
                }
                this.envData = data.list;
                this.loading = false;
                // 分页赋值 保障子组件里的值与父组件的值一致
                this.pageparam.currentPage = this.searchForm.page;
                this.pageparam.pageSize = this.searchForm.limit;
                this.pageparam.total = data.total;
            });
        },
        // 获取域名列表数据方法
        getDomainData() {
            this.domainLoading = true
            let url = '/auto_test/environment/domain/list/' + this.currentEnvId;
            this.$get(url, response => {
                let data = response.data;
                for(let i =0; i<data.length; i++){
                    data[i].updateTime= timestampToTime(data[i].updateTime);
                }
                this.domainData = data;
                this.domainLoading = false;
            });
        },
        // 分页插件事件
        callFather(parm) {
            this.searchForm.page = parm.currentPage;
            this.searchForm.limit = parm.pageSize;
            this.getEnvData(this.searchForm);
        },
        // 搜索按钮
        search() {
            this.getEnvData(this.searchForm);
        },
        // 重置按钮
        reset() {
            this.searchForm.condition = "";
            this.getEnvData(this.searchForm);
        },
        // 新增环境
        addEnv(){
            this.title = "新增环境";
            this.envForm = {
                id: null,
                name: null,
                description: null,
            };
            this.envVisible = true;
        },
        editEnv(row){
            this.title = "编辑环境";
            this.envForm = {
                id: row.id,
                name: row.name,
                description: row.description,
                createTime: row.createTime,
                createUser: row.createUser
            };
            this.envVisible = true;
        },
        viewDomain(id){
            this.currentEnvId = id;
            this.getDomainData();
            this.domainVisible = true;
        },
        changeType(){
            this.domainForm.domainKey = null;
        },
        addDomian(){
            this.domainForm = {
                id: null,
                type: "sign",
                domainKey: null,
                domainValue: null,
                environmentId: this.currentEnvId
            };
            this.editDomainVisible = true;
        },
        editDomain(row){
            this.domainForm = {
                id: row.id,
                type: row.type,
                domainKey: row.domainKey,
                domainValue: row.domainValue,
                environmentId: row.environmentId,
                createTime: row.createTime,
                createUser: row.createUser
            };
            this.editDomainVisible = true;
        },
        submitEnvForm(confirm, form){
            this.$refs[confirm].validate(valid => {
                if (valid) {
                    let url = '/auto_test/environment/add';
                    form.projectId = this.$store.state.projectId;
                    this.$post(url, form, response =>{
                        this.$message.success("保存成功");
                        this.envVisible = false;
                        this.getEnvData(this.searchForm);
                    });
                }else{
                    return false;
                }
            });
        },
        submitDomainForm(confirm, form){
            this.$refs[confirm].validate(valid => {
                if (valid) {
                    let url = '/auto_test/environment/domain/save';
                    this.$post(url, form, response =>{
                        this.$message.success("保存成功");
                        this.editDomainVisible = false;
                        this.getDomainData();
                    });
                }else{
                    return false;
                }
            });
        },
        deleteEnv(envId){
            this.$confirm('确定要删除环境吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/autotest/environment/delete/'+envId;
                this.$post(url, {}, response => {
                    this.$message.success("删除成功");
                    this.getEnvData(this.searchForm);
                });
            })
            .catch(() => {
                this.$message.success("取消成功");
            })
        },
        deleteDomain(domainId){
            this.$confirm('确定要删除域名吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/autotest/domain/delete/'+domainId;
                this.$post(url, {}, response => {
                    this.$message.success("删除成功");
                    this.getDomainData();
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