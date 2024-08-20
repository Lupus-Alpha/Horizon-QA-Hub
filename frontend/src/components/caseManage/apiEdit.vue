<template>
    <div>
        <page-header title="编辑接口" :cancel="cancelAdd" :save="saveAdd"></page-header>
        <el-form ref="apiForm" :rules="rules" :model="apiForm" label-width="80px">
        <p class="tip">基础信息</p>
        <el-row :gutter="40">
             <el-col :span="10">
                <el-form-item label="接口请求" prop="path">
                    <el-input size="small" v-model="apiForm.path" placeholder="请输入接口地址" style="margin-top: 5px">
                        <el-select v-model="apiForm.method" slot="prepend" style="width: 80px" size="small">
                            <el-option v-for="item in methods" :key="item" :label="item" :value="item"/>
                        </el-select>
                    </el-input>
                </el-form-item>
            </el-col>
             <el-col :span="6">
                <el-form-item label="请求协议" prop="protocol">
                    <el-select size="small" v-model="apiForm.protocol" placeholder="请选择请求协议">
                        <el-option v-for="item in protocols" :key="item" :label="item" :value="item"/>
                    </el-select>
                </el-form-item>
            </el-col>
             <el-col :span="8">
                <el-form-item label="模块分类" prop="moduleId">
                    <select-tree placeholder="请选择模块分类" :selectedValue="apiForm.moduleId"
                        :selectedLabel="apiForm.moduleName" :treeData="modules" @selectModule="selectModule($event)"/>
                </el-form-item>
            </el-col>
             </el-row>
            <el-row :gutter="40">
            <el-col :span="10">
                <el-form-item label="接口名称" prop="name">
                    <el-input placeholder="请输入内容" v-model="apiForm.name" class="input-with-select"></el-input>
                </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-form-item label="服务标识" prop="serverSign">
                    <el-select size="small" v-model="apiForm.serverSign" clearable placeholder="请选择服务标识">
                        <el-option v-for="item in serverSigns" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-form-item>
            </el-col>
        
            <el-col :span="8">
                <el-form-item label="接口描述">
                    <el-input size="small" clearable placeholder="请输入接口描述" v-model="apiForm.description"/>
                </el-form-item>
            </el-col>
            </el-row>
        <p class="tip">请求参数</p>
        <el-tabs style="width: 100%" v-model="activeTab">
            <el-tab-pane label="请求头" name="header">
                <div class="request-param">
                    <request-header :reqHeader="apiForm.header" style="width: 100%"/>
                </div>
            </el-tab-pane>
            <el-tab-pane label="请求体" name="body">
                <div class="request-param">
                    <request-body :reqBody="apiForm.body" style="width: 100%"/>
                </div>
            </el-tab-pane>
            <el-tab-pane label="QUERY参数" name="query">
                <div class="request-param">
                    <request-query :reqQuery="apiForm.query" style="width: 100%"/>
                </div>
            </el-tab-pane>
            <el-tab-pane label="REST参数" name="rest">
                <div class="request-param">
                    <request-rest :reqRest="apiForm.rest" style="width: 100%"/>
                </div>
            </el-tab-pane>
        </el-tabs>

        </el-form>
    </div>
</template>
<script>
import PageHeader from '../common/pageheader'
import RequestHeader from './common/request/requestHeader'
import RequestBody from './common/request/requestBody'
import RequestQuery from './common/request/requestQuery'
import RequestRest from './common/request/requestRest'
import SelectTree from './common/module/selectTree'
export default {
   components: {
       RequestHeader,RequestBody,RequestQuery,RequestRest,PageHeader,SelectTree
   },
    data() {
        return{
            activeTab: "body",
            apiForm: {
                id: "",
                name: "",
                path: "",
                method: "GET",
                protocol: "HTTP",
                serverSign: "",
                moduleId: "",
                moduleName: "",
                description: "",
                header: [],
                body: {},
                query: [],
                rest: []
            },
            methods: ['POST', 'GET', 'PUT', 'DELETE', 'HEAD', 'PATCH', 'OPTIONS'],
            protocols: ["HTTP"],
            serverSigns: [],
            modules: [],
            rules: {
                name: [{ required: true, message: '接口名称不能为空', trigger: 'blur' }],
                protocol: [{ required: true, message: '请求协议不能为空', trigger: 'blur' }],
                path: [{ required: true, message: '接口地址不能为空', trigger: 'blur' }],
                moduleId: [{ required: true, message: '接口模块不能为空', trigger: 'blur' }]
            }
        }
    },
    created() {
        this.getDetail(this.$route.params); 
        this.getModule();
    },
    methods: {
        getModule(){
            let url = '/auto_test/module/list/api_module/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.modules = response.data;
            });
        },
        selectModule(data){
            this.apiForm.moduleId = data.id;
            this.apiForm.moduleName = data.label;
        },
        getDetail(apiParam){
            if (!apiParam.apiId){ // 新增接口
                this.apiForm.body = {
                    type: 'json',
                    form: [],
                    json: '',
                    raw: '',
                    file: []
                }
            }else{  // 编辑接口
                let url = '/auto_test/api/detail/' + apiParam.apiId;
                this.$get(url, response =>{
                    let data = response.data;
                    if(data.header){
                        data.header = JSON.parse(data.header);
                    }
                    if(data.body){
                        data.body = JSON.parse(data.body);
                    }
                    if(data.query){
                        data.query = JSON.parse(data.query);
                    }
                    if(data.rest){
                        data.rest = JSON.parse(data.rest);
                    }
                    this.apiForm = data;
                })
            }
        },
        saveAdd(){
            this.$refs["apiForm"].validate(valid => {
                if (valid) {
                    let data = JSON.parse(JSON.stringify(this.apiForm)); // 深拷贝this.apiForm对象，以便对其进行修改而不影响原始对象。
                    data.projectId = this.$store.state.projectId;
                    if(this.$route.params.nodeID){
                        data.moduleId = this.$route.params.nodeID
                    }
                    data.header = JSON.stringify(data.header); // 使用JSON.stringify方法将它们转换为字符串，以便在后续的操作中进行传输或存储。
                    data.body = JSON.stringify(data.body);
                    data.query = JSON.stringify(data.query);
                    data.rest = JSON.stringify(data.rest);
                    let url = '/auto_test/api/save';
                    this.$post(url, data, response =>{
                        this.$message.success("保存成功");
                        this.$router.push({path: '/caseCenter/apiManage'});
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
.request-param{
    min-height:480px;
}
</style>