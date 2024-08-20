<template>
  <div>
    <el-form ref="caseForm" :rules="rules" :model="caseForm" label-width="90px">
      <base-info :caseForm="caseForm" :applications="applications" @saveAdd="saveAdd"/>
      <p class="tip">操作步骤</p>
      <el-form-item style="margin-left:-80px;" prop="caseApps"/>
      <el-table :data="caseForm.caseApps" row-key="id" class="sort-table" size="small">
        <el-table-column width="60px">
            <template slot-scope="scope">
                <el-button v-if="scope.$index!==0" size="mini" type="text" icon="el-icon-top" @click="up(scope.$index)"/>
                <el-button v-if="scope.$index!==(caseForm.caseApps.length-1)" size="mini" type="text" icon="el-icon-bottom" @click="down(scope.$index)"/>
            </template>
        </el-table-column>
        <el-table-column label="序号" prop="index" width="100px">
        </el-table-column>
        <el-table-column label="操作名称" prop="operationName" width="180px">
        </el-table-column>
        <el-table-column label="操作对象" prop="elementText">
          <template slot-scope="scope">
              <span v-html="scope.row.elementText"/>
          </template>
        </el-table-column>
        <el-table-column label="操作数据" prop="dataText">
          <template slot-scope="scope">
              <span v-html="scope.row.dataText"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120px">
            <template slot-scope="scope">
                <el-button size="mini" type="text" @click="editCaseApp(scope.$index, scope.row)">编辑</el-button>
                <el-button size="mini" type="text" @click="deleteCaseApp(scope.$index)">删除</el-button>
            </template>
        </el-table-column>
      </el-table>
    </el-form>
    <el-button size="small" icon="el-icon-plus" type="text" @click="addCaseApp">新增操作</el-button>
    <!-- 添加操作界面 -->
    <el-dialog title="选择操作" :visible.sync="editOperationVisible" width="750px" destroy-on-close :modal-append-to-body="false">
        <el-form ref="operationForm" :rules="rules" :model="operationForm" label-width="100px" label-position="top">
          <!-- 第一部分 先选择操作 -->
          <el-form-item label="操作名称" prop="operationId">
            <el-cascader size="small" style="width: 100%" filterable :options="operations" v-model="operationForm.operationIds" :show-all-levels="false"
              :props="{ expandTrigger: 'hover', value: 'id', label: 'name', children:'operationList' }" placeholder="请选择操作" @change="changeOperation"/>
          </el-form-item>
          <!-- 第二部分 根据所选是否需要元素 带出 -->
          <el-form-item v-if="operationForm.elements.length !== 0" label="操作对象" prop="elements">
            <el-row :gutter="10" v-for="(ele, index) in operationForm.elements" :key="index">
              <el-col :span="4">
                <span>{{ele.paramName}}</span>
              </el-col>
              <el-col :span="20">
                <el-row :gutter="10">
                  <el-col :span="10">
                    <select-tree placeholder="请选择视图模块" :selectedValue="ele.moduleId"
                            :selectedLabel="ele.moduleName" :treeData="viewModules" @selectModule="selectModule($event, ele)"/>
                  </el-col>
                  <el-col :span="14">
                    <el-select size="small" style="width:100%" v-model="ele.id" filterable
                      :placeholder="ele.description" value-key="item" @change="changeElement($event, ele)">
                        <el-option v-for="item in ele.selectElements" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item v-if="operationForm.data.length !== 0" label="操作数据" prop="data">
            <el-row :gutter="10" v-for="(data, index) in operationForm.data" :key="index">
              <el-col :span="4">
                <span>{{data.paramName}}</span>
              </el-col>
              <el-col :span="20">
                <el-select v-if="data.paramName === 'appId'" size="small" style="width:100%" filterable v-model="data.value" :placeholder="data.description">
                    <el-option v-for="item in applications" :key="item.id" :label="item.name" :value="item.id"/>
                </el-select>
                <el-select v-else-if="data.paramName === 'assertion'" size="small" style="width:100%" filterable v-model="data.value" :placeholder="data.description">
                    <el-option v-for="item in assertions" :key="item.id" :label="item.name" :value="item.id"/>
                </el-select>
                <el-select v-else-if="data.paramName === 'continue'" size="small" style="width:100%" v-model="data.value" :placeholder="data.description">
                    <el-option v-for="item in continues" :key="item.id" :label="item.name" :value="item.id"/>
                </el-select>
                <el-input v-else size="small" v-model="data.value" :placeholder="data.description"/>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="editOperationVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="saveOperationEdit('operationForm', operationForm)">保存</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import BaseInfo from "./basicInfo"
import {getUUID} from '@/utils/util'
import PageHeader from '../../common/pageheader'
import SelectTree from '../common/module/selectTree'
import {assertions} from '@/utils/constant'

export default {
    components:{
      PageHeader, BaseInfo, SelectTree
      },
    name: 'AppCaseEdit',
    data() {
        return{
            caseForm: {
                id: "",
                name: "",
                type: "APP",
                system: null,
                moduleId: "",
                moduleName: "",
                commonParam: {
                  functions: [],
                  params: [],
                  appId: null,
                  activity: null,
                },
                caseApps: []
            },
            viewModules: [],
            operations: [],
            applications: [],
            assertions: assertions,
            continues: [{id: true, name: "是"}, {id: false, name:"否"}],
            operationForm: {
              id: "",
              index: -1,
              operationId: "",
              operationName: "",
              elements: [],
              data: []
            },
            editOperationVisible: false,
            rules: {
                name: [{ required: true, message: '用例名称不能为空', trigger: 'blur' }],
                type: [{ required: true, message: '用例类型不能为空', trigger: 'blur' }],
                moduleId: [{ required: true, message: '用例模块不能为空', trigger: 'blur' }],
                operationId: [{ required: true, message: '操作名称不能为空', trigger: 'blur' }],
                elements: [{ required: true, message: '操作对象不能为空', trigger: 'blur' }],
                data: [{ required: true, message: '操作数据不能为空', trigger: 'blur' }],
                caseApps: [{ required: true, message: '请至少添加一条操作步骤', trigger: 'blur' }],
                'commonParam.appId': [{ required: true, message: '被测应用不能为空', trigger: 'blur' }],
            }
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["用例中心", "APP用例"]);
        this.caseForm.system = this.$route.params.system;
        this.getApplications();
        this.getOperations();
        this.getViews();
        this.getDetail(this.$route.params);
    },
    methods: {
        // 重新排序
        sortCaseApp(){
            for(let i=0; i<this.caseForm.caseApps.length; i++){
                this.caseForm.caseApps[i].index = i+1;
            }
        },
        getOperationObj(val, opt) {
          for(let i=0; i<opt.length; i++){
            if(opt[i].id === val[0]){
              let operationList = opt[i].operationList;
              for(let j=0; j< operationList.length; j++){
                if(operationList[j].id === val[1]){
                  return operationList[j];
                }
              }
              return null;
            }
          }
          return null;
        },
        elementToText(elements){
          let text = "";
          for(let i=0;i<elements.length;i++){
            if(i===0){
              text = elements[i].paramName+ " : " +elements[i].moduleName+ " / " +elements[i].name;
            }else{
              text = text + "<br>" + elements[i].paramName+ " : " +elements[i].moduleName+ " / " +elements[i].name;
            }
          }
          return text;
        },
        dataToText(datas){
          let text = "";
          for(let i=0;i<datas.length;i++){
            let newText = '';
            if(datas[i].paramName === "assertion"){
              for(let j=0;j<this.assertions.length;j++){
                if(this.assertions[j].id === datas[i].value){
                  newText = datas[i].paramName+ " : " + this.assertions[j].name;
                  break;
                }
              }
            }else if(datas[i].paramName === "appId"){
              for(let j=0;j<this.applications.length;j++){
                if(this.applications[j].id === datas[i].value){
                  newText = datas[i].paramName+ " : " + this.applications[j].name;
                  break;
                }
              }
            }else if(datas[i].paramName === "continue"){
              if(datas[i].value === true){
                newText = datas[i].paramName+ " : "  + "是";
              }else{
                newText = datas[i].paramName+ " : "  + "否";
              }
            }
            else{
              newText = datas[i].paramName+ " : " +datas[i].value;
            }
            if(i===0){
              text = newText;
            }else{
              text = text + "<br>" + newText;
            }
          }
          return text;
        },
        addCaseApp(){
          this.operationForm =  {
              id: getUUID(),
              index: -1,
              operationIds: [],
              operationId: "",
              operationName: "",
              elements: [],
              data: [],
          };
          this.editOperationVisible = true;
        },
        editCaseApp(index, row){
          this.operationForm = {
            id: row.id,
            index: index,
            operationIds: row.operationIds,
            operationId: row.operationId,
            operationName: row.operationName,
            elements: row.elements,
            data: row.data,
          };
          for(let i=0;i<row.elements.length;i++){
            if(row.elements[i].selectElements != undefined & row.elements[i].selectElements > 0){
              continue;
            }else{
              this.getControls(row.elements[i].moduleId, row.elements[i]);
            }
          }
          this.editOperationVisible = true;
        },
        up(index){
            this.caseForm.caseApps[index-1]=this.caseForm.caseApps.splice(index,1,this.caseForm.caseApps[index-1])[0];
            this.sortCaseApp();
        },
        down(index){
            this.caseForm.caseApps[index]=this.caseForm.caseApps.splice(index+1,1,this.caseForm.caseApps[index])[0];
            this.sortCaseApp();
        },
        deleteCaseApp(index){
            this.caseForm.caseApps.splice(index, 1);
            this.sortCaseApp();
        },
        changeOperation(val){
          let operation = this.getOperationObj(val, this.operations);
          this.operationForm.operationId = operation.id;
          this.operationForm.operationName = operation.name;
          let elements = JSON.parse(operation.elements);
          for(let i=0;i<elements.length;i++){
            elements[i].moduleId = "";
            elements[i].moduleName = "";
            elements[i].id = "";
            elements[i].name = "";
            elements[i].selectElements = [];
          }
          this.operationForm.elements = elements;
          let data = JSON.parse(operation.data);
          for(let i=0;i<data.length;i++){
            if(data[i].paramName === 'continue'){
              data[i].value = false;
            }else{
              data[i].value = "";
            }
          }
          this.operationForm.data = data;
        },
        selectModule(data, element){
            element.moduleId = data.id;
            element.moduleName = data.label;
            element.id = "";
            element.name = "";
            this.getControls(data.id, element);
        },
        changeElement(val, element){
          let item = null;
          for(let i=0;i<element.selectElements.length;i++){
            if(element.selectElements[i].id === val){
              item = element.selectElements[i];
              break;
            }
          }
          element.id = item.id;
          element.name = item.name;
        },
        saveOperationEdit(confirm, form){
          this.$refs[confirm].validate(valid => {
              if (valid) {
                form.elementText = this.elementToText(form.elements);
                form.dataText = this.dataToText(form.data);
                if(form.index === -1){
                  form.index = this.caseForm.caseApps.length + 1;
                  this.caseForm.caseApps.push(form);
                }else{
                  form.index = form.index + 1;
                  this.$set(this.caseForm.caseApps, form.index-1, form);
                }
                this.editOperationVisible = false;
              }else{
                  return false;
              }
          });
        },
        getControls(moduleId, element){
            let url = '/auto_test/control/list/module/'+this.caseForm.system+'/'+moduleId;
            this.$get(url, response => {
                element.selectElements = response.data;
            });
        },
        getDetail(param){
            if (param.caseId){  // 编辑
                let url = "/auto_test/case/detail/" + this.caseForm.type.toLowerCase() + "/" + param.caseId;
                this.$get(url, response => {
                    let data = response.data;
                    if(data.commonParam){
                        data.commonParam = JSON.parse(data.commonParam);
                    }
                    for(let i=0;i<data.caseApps.length;i++){
                        let caseApp = data.caseApps[i];
                        // 处理app
                        caseApp.operationIds = [caseApp.operationType, caseApp.operationId];
                        caseApp.elements = JSON.parse(caseApp.elements);
                        caseApp.data = JSON.parse(caseApp.data);
                        caseApp.elementText = this.elementToText(caseApp.elements);
                        caseApp.dataText = this.dataToText(caseApp.data);
                    }
                    this.caseForm = data;
                });
            }
        },
        getViews(){
            let url = '/auto_test/module/list/view/' + this.$store.state.projectId;
            this.$get(url, response =>{
                this.viewModules = response.data;
            });
        },
        getOperations(){
            let url = '/auto_test/operation/group/app/list/' + this.$store.state.projectId + '?system=' +this.caseForm.system;
            this.$get(url, response =>{
                this.operations = response.data;
            });
        },
        getApplications(){
            let url = '/auto_test/application/all/' + this.caseForm.system + "/" + this.$store.state.projectId;
            this.$get(url, response =>{
                this.applications = response.data;
            });
        },
        saveAdd(){
            this.$refs["caseForm"].validate(valid => {
                if (valid) {
                    let data = JSON.parse(JSON.stringify(this.caseForm));
                    data.projectId = this.$store.state.projectId;
                    data.commonParam = JSON.stringify(data.commonParam);
                    for(let i=0; i<data.caseApps.length; i++){
                        data.caseApps[i].index = i+1;
                        for(let j=0; j<data.caseApps[i].elements.length; j++){
                          data.caseApps[i].elements[j].selectElements = [];
                        }
                        data.caseApps[i].elements = JSON.stringify(data.caseApps[i].elements);
                        data.caseApps[i].data = JSON.stringify(data.caseApps[i].data);
                    }
                    let url = '/auto_test/case/save';
                    this.$post(url, data, response =>{
                        this.$message.success("保存成功");
                        this.$router.push({path: '/caseCenter/caseManage'});
                    });
                }else{
                    return false;
                }
            });
        },
    }
}
</script>

<style scoped>

</style>
