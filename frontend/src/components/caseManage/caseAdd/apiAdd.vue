<template>
    <div>
        <el-form ref="caseForm" :rules="rules" :model="caseForm" label-width="90px">
        <basic-info :caseForm="caseForm" @saveAdd="saveAdd"/>
        <p class="tip"><span>接口请求</span></p>
        <!-- <el-form-item style="margin-left:-80px;" prop="caseApis"/> -->
        <el-table :data="caseForm.caseApis" row-key="id" class="sort-table" size="small">
            <el-table-column width="70px">
                <template slot-scope="scope">
                    <el-button v-if="scope.$index!==0" size="mini" type="text" icon="el-icon-top" @click="up(scope.$index)"/>
                    <el-button v-if="scope.$index!==(caseForm.caseApis.length-1)" size="mini" type="text" icon="el-icon-bottom" @click="down(scope.$index)"/>
                </template>
            </el-table-column>
            <el-table-column label="序号" prop="index" width="100px">
            </el-table-column>
            <el-table-column label="接口名称" prop="apiName">
            </el-table-column>
            <el-table-column label="请求方式" prop="apiMethod">
            </el-table-column>
            <el-table-column label="接口地址" prop="apiPath">
            </el-table-column>
            <el-table-column label="操作" width="120px">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="editCaseApi(scope.$index)">编辑</el-button>
                    <el-button size="mini" type="text" @click="deleteCaseApi(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        </el-form>
        <el-button size="small" icon="el-icon-plus" type="text" @click="selectApiVisible=true">选择接口</el-button>
        <el-dialog title="选择接口" :visible.sync="selectApiVisible" width="800px" destroy-on-close>
            <select-api :selections="selections" :selectApiVisible="selectApiVisible"/>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="selectApiVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="selectApiSave">保存</el-button>
        </div>
    </el-dialog>
    <el-drawer title="接口详情" :visible.sync="editCaseApiVisible" direction="rtl" :with-header="false" destroy-on-close size="920px">
        <div class="api-drawer-header">
            
            <el-row :gutter="40">
                <el-col :span="24">
                    <span style="font-size: 16px;">接口详情编辑</span>
                </el-col>
                <el-col :span="19">
                    <el-input size="small" v-model="caseApiForm.path" placeholder="请输入接口地址" style="margin-top: 5px;margin-left: 5px">
                        <el-select v-model="caseApiForm.method" slot="prepend" style="width: 80px" size="small">
                            <el-option v-for="item in methods" :key="item" :label="item" :value="item"/>
                        </el-select>
                    </el-input>
                </el-col>
                <el-col :span="5">
                    <el-button size="small" type="primary" style="float: right;" @click="editCaseApiVisible=false">确定</el-button>
                </el-col>
            </el-row>
        </div>
        <div class="api-drawer-body">
            <el-tabs style="width: 100%" v-model="activeTab">
                <el-tab-pane label="请求头" name="header">
                    <request-header :reqHeader="caseApiForm.header" style="width: 100%"/>
                </el-tab-pane>
                <el-tab-pane label="请求体" name="body">
                    <request-body :reqBody="caseApiForm.body" style="width: 100%"/>
                </el-tab-pane>
                <el-tab-pane label="QUERY参数" name="query">
                    <request-query :reqQuery="caseApiForm.query" style="width: 100%"/>
                </el-tab-pane>
                <el-tab-pane label="REST参数" name="rest">
                    <request-rest :reqRest="caseApiForm.rest" style="width: 100%"/>
                </el-tab-pane>
                <el-tab-pane label="响应断言" name="assertion">
                    <assertion :assertion="caseApiForm.assertion" :apiId="caseApiForm.apiId" style="width: 100%"/>
                </el-tab-pane>
                <!-- <el-tab-pane label="关联取值" name="relation">
                    <relation :relation="caseApiForm.relation" :apiId="caseApiForm.apiId" style="width: 100%"/>
                </el-tab-pane> -->
                <!-- <el-tab-pane label="逻辑控件" name="controller">
                    <controller :controller="caseApiForm.controller" style="width: 100%"/>
                </el-tab-pane> -->
            </el-tabs>
        </div>
    </el-drawer>
    </div>
</template>
<script>

import BasicInfo from "./basicInfo"
import {getUUID} from '@/utils/util'
import SelectApi from "../common/module/selectApi.vue"
import RequestHeader from '../common/request/requestHeader'
import RequestBody from '../common/request/requestBody'
import RequestQuery from '../common/request/requestQuery'
import RequestRest from '../common/request/requestRest'
import Assertion from '../common/module/assertion'
export default {
    // 注册组件
    components: {
        BasicInfo,SelectApi,RequestHeader,RequestBody,RequestQuery,RequestRest,Assertion
    },
    data() {
        return{
            selectApiVisible: false,
            selections: [],
            activeTab: "body",
            editCaseApiVisible: false,
            caseForm: {
                id: "",
                
                name: "",
                type: "API",
                moduleId: "",
                moduleName: "",
                system: null,
                commonParam: {
                    functions: [],
                    params: [],
                    header: "",
                    proxy: ""
                },
                caseApis: []
            },
            caseApiForm:{
                path:"",
                method:"",
                header: [],
                body: { type: 'json',form: [],json: '',raw: '',file: []},
                rest: [],
                query: [],
                assertion: [],
                relation: [],
                controller: []
            },
        }
    },
    created() {
        this.getDetail(this.$route.params);
    },
    methods: {
        selectApiSave (){
            for(let i=0;i<this.selections.length;i++){
                let caseApi = {
                    id: getUUID(),
                    index: this.caseForm.caseApis.length+1,
                    apiId: this.selections[i].id,
                    apiMethod: this.selections[i].method,
                    apiName: this.selections[i].name,
                    apiPath: this.selections[i].path
                }
                this.caseForm.caseApis.push(caseApi);
                this.selectApiVisible = false;
            }
        },
        saveAdd(){
            this.$refs["caseForm"].validate(valid=>{
                if(valid){
                    let data = JSON.parse(JSON.stringify(this.caseForm)); //深拷贝
                    data.projectId = this.$store.state.projectId;
                    data.commonParam = JSON.stringify(data.commonParam);
                    for(let i=0; i<data.caseApis.length; i++){
                        data.caseApis[i].index = i+1;
                        let caseApi = data.caseApis[i];
                        if(caseApi.header){  // 全部要转成字符串
                            caseApi.header = JSON.stringify(caseApi.header);
                        }
                        if(caseApi.body){
                            caseApi.body = JSON.stringify(caseApi.body);
                        }
                        if(caseApi.query){
                            caseApi.query = JSON.stringify(caseApi.query);
                        }
                        if(caseApi.rest){
                            caseApi.rest = JSON.stringify(caseApi.rest);
                        }
                        if(caseApi.assertion){
                            caseApi.assertion = JSON.stringify(caseApi.assertion);
                        }
                        if(caseApi.relation){
                            caseApi.relation = JSON.stringify(caseApi.relation);
                        }
                        if(caseApi.controller){
                            caseApi.controller = JSON.stringify(caseApi.controller);
                        }

                    }
                    let url = '/auto_test/case/save';
                    this.$post(url, data, response =>{
                        this.$message.success("保存成功");
                        this.$router.push({path: '/caseCenter/caseManage'});
                    });
                }else{
                    return false;

                }
            }

            )
        },
        getDetail(param){
            if (param.caseId){  // 编辑
                let url = "/auto_test/case/detail/api/" + param.caseId;
                this.$get(url, response => {
                    let data = response.data;
                    if(data.commonParam){
                        data.commonParam = JSON.parse(data.commonParam);
                    }
                    for(let i=0;i<data.caseApis.length;i++){
                        let caseApi = data.caseApis[i];
                        if(caseApi.header){
                            caseApi.header = JSON.parse(caseApi.header);
                        }
                        if(caseApi.body){
                            caseApi.body = JSON.parse(caseApi.body);
                        }
                        if(caseApi.query){
                            caseApi.query = JSON.parse(caseApi.query);
                        }
                        if(caseApi.rest){
                            caseApi.rest = JSON.parse(caseApi.rest);
                        }
                        if(caseApi.assertion){
                            caseApi.assertion = JSON.parse(caseApi.assertion);
                        }
                        if(caseApi.relation){
                            caseApi.relation = JSON.parse(caseApi.relation);
                        }
                        if(caseApi.controller){
                            caseApi.controller = JSON.parse(caseApi.controller);
                        }
                    }
                    this.caseForm = data; 
                     
                }
            )}
        
        },
        editCaseApi(index){
            let caseApi = this.caseForm.caseApis[index];
            this.activeTab = "body";
            if(!caseApi.body){
                let url = "/auto_test/api/detail/" + caseApi.apiId;
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
                    this.caseApiForm.header = data.header;
                    this.caseApiForm.body = data.body;
                    this.caseApiForm.rest = data.rest;
                    this.caseApiForm.query = data.query;
                    this.caseApiForm.assertion = [];
                    this.caseApiForm.relation = [];
                    this.caseApiForm.controller = [];
                    this.editCaseApiVisible = true;
                });
            }else{
                this.caseApiForm = caseApi;
                this.editCaseApiVisible = true;
            }
        },

    }
}
</script>
