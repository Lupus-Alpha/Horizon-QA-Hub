<template>
    <div>
        <page-header title="用例编辑" :showDebug="true" :cancel="cancelAdd" :debug="debugCase" :save="saveAdd"/>
        <p class="tip"><span>基础信息</span></p>
        <el-form :model="caseForm">
            <el-row :gutter="20">
                <el-col :span="6">
                    <el-form-item label="用例名称" >
                        <el-input  size="small" v-model="caseForm.name" placeholder="请输入用例名称"/>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="用例类型" prop="type">
                    <el-select size="small" style="width: 100%" v-model="caseForm.type" disabled placeholder="请选择用例类型">
                        <el-option v-for="item in caseTypes" :key="item" :label="item" :value="item"/>
                    </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="模块分类" prop="moduleId">
                     <select-tree placeholder="请选择模块分类" :selectedValue="caseForm.moduleId" 
                        :selectedLabel="caseForm.moduleName" :treeData="modules" @selectModule="selectModule($event)"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="40">
            <el-col :span="16">
                <el-form-item label="用例描述">
                    <el-input size="small" clearable placeholder="请输入用例描述" v-model="caseForm.description"/>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item v-if="caseForm.type==='APP'" label="操作系统">
                    <el-select size="small" style="width: 100%" v-model="caseForm.system" disabled placeholder="请选择操作系统">
                        <el-option v-for="item in systems" :key="item" :label="item" :value="item"/>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
         <p class="tip">
            <span>配置信息</span>
            <el-tooltip content="如若使用自定义函数和公参 请先导入 否则无法使用" placement="bottom">
                <i class="el-icon-info"></i>
            </el-tooltip>
        </p>
        <el-row :gutter="40">
            <el-col :span="12">
                <el-form-item label="导入函数">
                    <el-select size="small" style="width: 100%" v-model="caseForm.commonParam.functions" filterable multiple clearable placeholder="请选择本用例需要使用的自定义函数">
                        <el-option v-for="item in functionList" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="导入公参">
                    <el-select size="small" style="width: 100%" v-model="caseForm.commonParam.params" filterable multiple clearable placeholder="请选择本用例需要使用的自定义公参">
                        <el-option v-for="item in paramList" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="40" v-if="caseForm.type === 'API'">
            <el-col :span="12">
                <el-form-item label="公用Header">
                    <el-select size="small" style="width: 100%" v-model="caseForm.commonParam.header" clearable placeholder="公共使用的请求头 接口若配置请求头会合并且替换同名请求头参数">
                        <el-option v-for="item in headers" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="公用Proxy">
                    <el-select size="small" style="width: 100%" v-model="caseForm.commonParam.proxy" clearable placeholder="公共使用的代理">
                        <el-option v-for="item in proxys" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="40" v-if="caseForm.type === 'WEB'">
            <el-col :span="12">
                <el-form-item label="启动Driver">
                    <el-switch size="small" v-model="caseForm.commonParam.startDriver" active-text="开始前重启浏览器"/>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="关闭Driver">
                    <el-switch size="small" v-model="caseForm.commonParam.closeDriver" active-text="结束后关闭浏览器"/>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="40" v-if="caseForm.type === 'APP'">
            <el-col :span="12">
                <el-form-item label="被测应用" prop="commonParam.appId">
                    <el-select size="small" style="width: 100%" v-model="caseForm.commonParam.appId" placeholder="被测应用">
                        <el-option v-for="item in applications" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="caseForm.system === 'android'">
                <el-form-item label="启动视图">
                    <el-input  size="small" style="width: 100%" v-model="caseForm.commonParam.activity" placeholder="指定app启动视图 例: com.demo.activity"/>
                </el-form-item>
            </el-col>
        </el-row>
        </el-form>
    </div>
</template>
<script>
import PageHeader from '../../common/pageheader'
import SelectTree from '../common/module/selectTree'
export default {
    name : "BasicInfo",
    components: {PageHeader,SelectTree},
    props:{
    caseForm: Object,
    applications: {
        type: Array,
        default:  () => {
	        return [];
	    },
    }
    },
    data() {
        return{
            caseTypes:["API", "WEB", "APP"],
            systems: ["android", "apple"],
            functionList: [],
            paramList:[],
            headers: [],
            proxys: []
        }
    },
    created() {
        this.getFunction();
        this.getParam();
        this.getModule();
        if (this.caseForm.type === "API"){
            this.getHeader();
            this.getProxy();
        }
    },
    methods: {
        getModule(){
            let url = '/auto_test/module/list/api_module/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.modules = response.data;
            });
        },
        selectModule(data){
            this.caseForm.moduleId = data.id;
            this.caseForm.moduleName = data.label;
        },
        saveAdd(){
            this.$emit("saveAdd");
        },
        getFunction(){
            let url = '/auto_test/function/custom/all/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.functionList = response.data;
            });
        },
        getParam(){
            let url = '/auto_test/params/all/custom/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.paramList = response.data;
            });
        },
        getHeader(){
            let url = "/auto_test/params/all/header/" + this.$store.state.projectId;
            this.$get(url, response =>{
                this.headers = response.data;
            });
        },
        getProxy(){
            let url = "/auto_test/params/all/proxy/" + this.$store.state.projectId;
            this.$get(url, response =>{
                this.proxys = response.data;
            });
        },

        }
}

</script>