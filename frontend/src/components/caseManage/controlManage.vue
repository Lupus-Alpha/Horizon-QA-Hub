<template>
  <div>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-radio-group size="small" v-model="searchForm.system" @change="selectSystem">
                <el-radio-button label="android"/>
                <el-radio-button label="apple"/>
            </el-radio-group>
        </el-form-item>
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入控件NO、名称"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addControl">新增控件</el-button>
        </el-form-item>
    </el-form>
    <!-- 页面模块 -->
    <el-col :span="4" class="left-tree">
        <module-tree title="视图模块" :treeData="treeData" :currentModule="searchForm.moduleId" @clickModule="clickModule($event)" @appendModule="appendModule($event)" 
            @removeModule="removeModule(arguments)" @dragNode="dragNode(arguments)"/>
    </el-col>
    <!-- 控件列表 -->
    <el-col :span="20" class="right-table">
        <!--列表-->
        <el-table size="small" :data="controlListData" v-loading="loading" control-loading-text="拼命加载中">
            <el-table-column prop="num" label="NO" width="60px"/>
            <el-table-column prop="name" label="控件名称" min-width="150"/>
            <el-table-column prop="system" label="所属系统"/>
            <el-table-column prop="by" label="定位方式">
                <template slot-scope="scope">
                    <span v-if="scope.row.by ==='PROP'">属性定位</span>
                    <span v-if="scope.row.by ==='XPATH'">Xpath定位</span>
                    <span v-if="scope.row.by ==='PRED'">Predicate定位</span>
                    <span v-if="scope.row.by ==='CLASS'">ClassChain定位</span>
                </template>
            </el-table-column>
            <el-table-column prop="expressionText" label="表达式" min-width="120"/>
            <el-table-column prop="moduleName" label="所属页面"/>
            <el-table-column prop="createUser" label="创建人"/>
            <el-table-column prop="updateTime" label="更新时间" width="150px"/>
            <el-table-column fixed="right" align="operation" label="操作" width="100">
                <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="editControl(scope.row)">编辑</el-button>
                    <el-button type="text" size="mini" @click="deleteControl(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <Pagination :pageparam="pageparam" @callFather="callFather"/>
    </el-col>
    <!-- 添加模块弹框 -->
    <module-append title="添加页面模块" :show.sync="moduleVisible" :moduleForm="moduleForm" @closeDialog="closeDialog('module')" @submitModule="submitModule($event)"/>
    <!-- 添加控件弹框 -->
    <el-dialog title="编辑控件" :visible.sync="controlVisible" width="50%" destroy-on-close>
        <el-form label-width="120px" style="padding-right: 30px;" :model="controlForm" :rules="rules" ref="controlForm">
            <el-form-item label="控件名称" prop="name">
                <el-input size="small" style="width:95%" v-model="controlForm.name" auto-complete="off" placeholder="控件名称"/>
            </el-form-item>
            <el-form-item label="所属系统" prop="system">
                <el-select size="small" style="width:95%;" v-model="controlForm.system" placeholder="所属系统" >
                    <el-option v-for="item in systems" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="定位方式" prop="by">
                <el-select size="small" style="width:95%;" v-model="controlForm.by" placeholder="定位方式" @change="changeBy">
                    <el-option v-for="item in byList" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="表达式" prop="expression">
                <div v-if="controlForm.by === 'PROP'" style="width:95%">
                    <el-row v-for="(item, index) in controlForm.expressionList" :key="index">
                        <el-col :span="6">
                            <el-select size="small" style="width:95%" v-model="item.propName" placeholder="属性名">
                                <el-option v-for="prop in propList" :key="prop" :label="prop" :value="prop"></el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="15">
                            <el-input size="small" style="width:100%" v-model="item.propValue" placeholder="请输入属性值"/>
                        </el-col>
                        <el-col :span="3">
                            <div style="font-size: 24px; margin-top:8px; margin-left:5px; display: flex">
                                <i class="el-icon-circle-plus lm-success" @click="addProp(index)"></i>
                                <i v-if="controlForm.expressionList.length > 1" class="el-icon-remove lm-error" @click="deleteProp(index)"></i>
                            </div>
                        </el-col>
                    </el-row>
                </div>
                <el-input v-else size="small" style="width:95%" v-model="controlForm.expression" placeholder="请输入元素定位表达式"/>
            </el-form-item>
            <el-form-item label="所属页面" prop="moduleId">
                <!-- <select-tree style="width:95%;" placeholder="所属视图" :selectedValue="controlForm.moduleId" :selectedLabel="controlForm.moduleName" :treeData="treeData" @selectModule="selectModule($event)"/> -->
            </el-form-item>
            <el-form-item label="控件描述">
                <el-input size="small" style="width:95%" v-model="controlForm.description" :autosize="{ minRows: 3}" type="textarea" maxlength="200" show-word-limit auto-complete="off" placeholder="控件描述"/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="closeDialog('control')">取消</el-button>
            <el-button size="small" type="primary" @click="submitControlForm">确定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import {timestampToTime} from '@/utils/util'
import Pagination from '@/components/common/pagination'
import moduleTree from './common/module/moduleTree.vue'
import ModuleAppend from './common/module/moduleAppend'


export default {
    // 注册组件
    components: {
        Pagination, moduleTree, ModuleAppend
    },
    data() {
        return{
            loading:false,
            moduleVisible: false,
            controlVisible: false,
            controlForm: {
                id:"",
                name:"",
                system: "",
                by: "",
                expression: "XPATH",
                expressionList: [],
                moduleId: "",
                moduleName:"",
                description: ""
            },
            moduleForm: {
                moduleName: "",
                parentId: "",
                parentName: ""
            },
            systems:[
                { label: "安卓", value: "android" },
                { label: "苹果", value: "apple" },
            ],
            searchForm: {
                page: 1,
                limit: 10,
                condition: "",
                moduleId: "",
                system: "android"
            },
            controlListData: [],
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            treeData: [],
            byList:[],
            propList: [],
            rules: {
                name: [{ required: true, message: '元素名称不能为空', trigger: 'blur' }],
                system: [{ required: true, message: '所属系统不能为空', trigger: 'blur' }],
                by: [{ required: true, message: '定位方式不能为空', trigger: 'blur' }],
                expression: [{ required: true, message: '表达式不能为空', trigger: 'blur' }],
                // moduleId: [{ required: true, message: '所属页面不能为空', trigger: 'blur' }]
            },
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["用例中心", "控件管理"]);
        this.getTree();
        this.getdata(this.searchForm);
    },
    methods: {
        selectSystem(val){
            this.searchForm.system = val;
            this.searchForm.page = 1;
            this.getdata(this.searchForm);
        },
        // 点击模块
        clickModule(data){
            this.searchForm.moduleId = data.id;
            this.getdata(this.searchForm);
        },
        // 添加模块
        appendModule(data) {
            if (data){
                this.moduleForm.parentId = data.id;
                this.moduleForm.parentName = data.label;
                this.moduleForm.data = data;
            }else{
                this.moduleForm.parentId = 0;
                this.moduleForm.parentName = "根节点";
                this.moduleForm.data = "";
            }
            this.moduleVisible = true;
        },
        // 删除模块
        removeModule(args) {
            let node = args[0];
            let data = args[1];
            if(data.children.length != 0){
                this.$message.warning("当前模块有子模块, 无法删除");
                return;
            }
            let url = '/auto_test/module/delete';
            this.$post(url, data, response =>{
                this.getTree();
                this.$message.success("模块删除成功");
            });
        },
        // 拖拽模块
        dragNode(args){
            let dragNode = args[0];
            let newParent = args[1];
            let url = '/auto_test/module/save';
            let moduleForm = dragNode.data;
            moduleForm.parentId = newParent;
            this.$post(url, moduleForm, response =>{
                this.$message.success("更改成功");
            });
        },
        // 关闭弹框
        closeDialog(val){
            if(val==='module'){
                this.moduleVisible = false;
            }else{
                this.controlVisible = false;
            }
        },
        // 提交模块
        submitModule(moduleForm) {
            moduleForm.projectId = this.$store.state.projectId;
            moduleForm.moduleType = 'view_module';
            let url = '/auto_test/module/save';
            this.$post(url, moduleForm, response =>{
                this.getTree();
                this.moduleVisible = false;
                this.moduleForm.name = "";
            });
        },
        // 获取树数据
        getTree(){
            let url = '/auto_test/module/list/view_module/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.treeData = response.data;
            });
        },
        // 获取列表数据方法
        getdata(searchParam) {
            this.loading = true;
            let url = '/auto_test/control/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                condition: searchParam.condition,
                moduleId: searchParam.moduleId,
                projectId: this.$store.state.projectId,
                system: searchParam.system
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i=0;i<data.list.length;i++){
                    if(data.list[i].by === "PROP"){
                        let expressions = JSON.parse(data.list[i].expression);
                        let text = "";
                        for(let j=0;j<expressions.length;j++){
                            text = text + expressions[j].propName + ": " + expressions[j].propValue + ";";
                        }
                        data.list[i].expressionText = text;
                    }else{
                        data.list[i].expressionText = data.list[i].expression;
                    }
                    data.list[i].updateTime = timestampToTime(data.list[i].updateTime);
                }
                this.controlListData = data.list;
                this.loading = false
                // 分页赋值
                this.pageparam.currentPage = this.searchForm.page;
                this.pageparam.pageSize = this.searchForm.limit;
                this.pageparam.total = data.total;
            });
        },
        // 分页插件事件
        callFather(parm) {
            this.searchForm.page = parm.currentPage
            this.searchForm.limit = parm.pageSize
            this.getdata(this.searchForm)
        },
        // 搜索按钮
        search() {
            this.getdata(this.searchForm)
        },
        // 重置按钮
        reset() {
            this.searchForm.moduleId = "";
            this.searchForm.condition = "";
            this.getdata(this.searchForm)
        },
        // 新增控件
        addControl(){
            this.controlForm = {
                system: this.searchForm.system,
                id:"",
                name:"",
                by: "XPATH",
                expression: "",
                expressionList: [],
                moduleId: "",
                moduleName:"",
                description: ""
            };
            this.controlVisible = true;
        },
        // 编辑控件
        editControl(row){
            this.controlForm.id = row.id;
            this.controlForm.name = row.name;
            this.controlForm.system = row.system;
            this.controlForm.by = row.by;
            this.controlForm.expression = row.expression;
            if(row.by === "PROP"){
                this.controlForm.expressionList = JSON.parse(row.expression);
            }
            this.controlForm.moduleId = row.moduleId;
            this.controlForm.moduleName = row.moduleName;
            this.controlForm.description = row.description;
            this.controlVisible = true;
        },
        selectModule(data){
            this.controlForm.moduleId = data.id;
            this.controlForm.moduleName = data.label;
        },
        // 更改定位方式
        changeBy(val){
            if(val === "PROP"){
                if(this.controlForm.expressionList.length === 0){
                    this.controlForm.expressionList.push({propName:"",propValue:""});
                }
            }
        },
        // 新增属性定位
        addProp(index){
            this.controlForm.expressionList.splice(index+1, 0, {propName:"",propValue:""});
        },
        // 删除属性定位
        deleteProp(index){
            this.controlForm.expressionList.splice(index, 1);
        },
        submitControlForm(){
            // 请求接口
            if(this.controlForm.by === "PROP"){
                let re = true;
                for(let i=0;i<this.controlForm.expressionList.length;i++){
                    let expression = this.controlForm.expressionList[i];
                    if(expression.propName === "" || expression.propValue === ""){
                        re = false;
                        break;
                    }
                }
                if(re){
                    this.controlForm.expression = JSON.stringify(this.controlForm.expressionList);
                }else{
                    this.controlForm.expression = "";
                }
            }
            this.$refs["controlForm"].validate(valid => {
                if (valid) {
                    this.controlForm.projectId = this.$store.state.projectId;
                    this.controlForm.moduleId = '085687fb-7707-42db-853a-ea0002692fb0';
                    let url = '/auto_test/control/save';
                    this.$post(url, this.controlForm, response =>{
                        this.$message.success("保存成功");
                        this.controlVisible = false;
                        this.getdata(this.searchForm);
                    });
                }else{
                    return false;
                }
            })
        },
        // 删除控件
        deleteControl(id){
            this.$confirm('确定要删除控件吗?', '删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
            .then(() => {
                let url = '/auto_test/control/delete/'+id;
                this.$post(url, {id: row.id}, response => {
                    this.$message.success("删除成功");
                    this.getdata(this.searchForm);
                });
            })
            .catch(() => {
                this.$message.success("取消成功");
            })
        },
    },
    watch: {
        'controlForm.system'(newVal, oldVal){
            if(newVal === "android"){
                this.propList = locateProps.android;
                this.byList = locateBys.android;
            }else{
                this.propList = locateProps.apple;
                this.byList = locateBys.apple;
            }
        }
    }
}
</script>

<style scoped>
.left-tree {
    padding-right: 5px;
    border-right:1px solid rgb(219, 219, 219);
}
.right-table {
    padding-left: 5px;
}
</style>
