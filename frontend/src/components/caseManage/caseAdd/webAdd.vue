<template>
  <div>
    <el-form ref="caseForm" :rules="rules" :model="caseForm" label-width="90px">
    <basic-info :caseForm="caseForm" @saveAdd="saveAdd"/>
    <p class="tip">操作步骤</p>
    <el-form-item style="margin-left:-80px;" prop="caseWebs"/>
    <el-table :data="caseForm.caseWebs" row-key="id" size="small">
        <el-table-column width="70px">
            <template slot-scope="scope">
                <el-button v-if="scope.$index!==0" size="mini" type="text" icon="el-icon-top" @click="up(scope.$index)"/>
                <el-button v-if="scope.$index!==(caseForm.caseWebs.length-1)" size="mini" type="text" icon="el-icon-bottom" @click="down(scope.$index)"/>
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
                <el-button size="mini" type="text" @click="editCaseWeb(scope.$index,scope.row)">编辑</el-button>
                <el-button size="mini" type="text" @click="deleteCaseWeb(scope.$index)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    </el-form>
    <el-button size="small" icon="el-icon-plus" type="text" @click="addCaseWeb()">新增操作</el-button>
    <el-dialog title="选择操作" :visible.sync="editOperationVisible" width="750px" destroy-on-close>
        <el-form ref="operationForm" :rules="rules" :model="operationForm" label-width="100px" label-position="top">
          <el-form-item label="操作名称" prop="operationId">
            <el-cascader size="small" style="width: 100%" filterable :options="operations" v-model="operationForm.operationIds" :show-all-levels="false"
              :props="{ expandTrigger: 'hover', value: 'id', label: 'name', children:'operationList' }" placeholder="请选择操作" @change="changeOperation"/>
          </el-form-item>
          <el-form-item v-if="operationForm.elements.length !== 0" label="操作对象" prop="elements">
            <el-row :gutter="10" v-for="(ele, index) in operationForm.elements" :key="index">
              <el-col :span="4">
                <span>{{ele.paramName}}</span>
              </el-col>
              <el-col :span="20">
                <el-row :gutter="10">
                  <el-col :span="10">
                    <select-tree placeholder="请选择页面模块" :selectedValue="ele.moduleId"
                            :selectedLabel="ele.moduleName" :treeData="pageModules" @selectModule="selectModule($event, ele)"/>
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
                <el-select v-if="data.paramName === 'domain'" size="small" style="width:100%" v-model="data.value" :placeholder="data.description">
                    <el-option v-for="item in serverSigns" :key="item.id" :label="item.name" :value="item.id"/>
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

import BasicInfo from "./basicInfo"
import {assertions} from '@/utils/constant'
import {getUUID} from '@/utils/util'
import SelectTree from '../common/module/selectTree'
export default {
    components:{
        BasicInfo,SelectTree
    },
    data() {
        return{
            caseForm: {
                id: "",
                name: "",
                type: "WEB",
                system: null,
                moduleId: "",
                moduleName: "",
                commonParam: {
                  functions: [],
                  params: [],
                  startDriver: true,
                  closeDriver: true,
                },
                caseWebs: []
            },
            pageModules: [],
            operations: [],
            serverSigns: [],
            assertions: assertions,
            continues: [{id: true, name: "是"}, {id: false, name:"否"}],
            operationForm: {
              id: "",
              index: -1,
              operationId: "",
              operationName: "",
              elements: [],
              data: [],
              row: null
            },
            editOperationVisible: false,
            rules: {
                name: [{ required: true, message: '用例名称不能为空', trigger: 'blur' }],
                type: [{ required: true, message: '用例类型不能为空', trigger: 'blur' }],
                moduleId: [{ required: true, message: '用例模块不能为空', trigger: 'blur' }],
                operationId: [{ required: true, message: '操作名称不能为空', trigger: 'blur' }],
                elements: [{ required: true, message: '操作对象不能为空', trigger: 'blur' }],
                data: [{ required: true, message: '操作数据不能为空', trigger: 'blur' }],
                caseWebs: [{ required: true, message: '请至少添加一条操作步骤', trigger: 'blur' }],
            }
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["用例中心", "WEB用例"]);
        this.getOperations();
        // this.getServerSign();
        this.getPages();
        // this.getDetail(this.$route.params);
    },
    methods: {
        saveAdd(){
          this.$refs["caseForm"].validate(valid => {
                if (valid) {
                    let data = JSON.parse(JSON.stringify(this.caseForm));
                    data.projectId = this.$store.state.projectId;
                    data.commonParam = JSON.stringify(data.commonParam);
                    for(let i=0; i<data.caseWebs.length; i++){
                        data.caseWebs[i].index = i+1;
                        for(let j=0; j<data.caseWebs[i].elements.length; j++){
                          data.caseWebs[i].elements[j].selectElements = [];
                        }
                        data.caseWebs[i].elements = JSON.stringify(data.caseWebs[i].elements);
                        data.caseWebs[i].data = JSON.stringify(data.caseWebs[i].data);
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
        selectModule(data,element){
            element.moduleId = data.id;
            element.moduleName = data.label;
            element.id = "";
            element.name = "";
            this.getElements(data.id, element);
        },
        getElements(moduleId, element){
            let url = '/auto_test/element/list/module/'+ moduleId;
            this.$get(url, response => {
                element.selectElements = response.data;
            });
        },
        saveOperationEdit(confirm, form){
          this.$refs[confirm].validate(valid => {
              if (valid) {
                form.elementText = this.elementToText(form.elements);
                form.dataText = this.dataToText(form.data);
                if(form.index === -1){
                  form.index = this.caseForm.caseWebs.length + 1;
                  this.caseForm.caseWebs.push(form);
                }else{
                  form.index = form.index + 1;
                  this.$set(this.caseForm.caseWebs, form.index-1, form);
                }
                this.editOperationVisible = false;
              }else{
                  return false;
              }
          });
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
            let newText = datas[i].paramName + " : ";
            if(datas[i].paramName === "domain"){
              for(let j=0;j<this.serverSigns.length;j++){
                // 找到标识id对应的name
                if(this.serverSigns[j].id === datas[i].value){
                  newText = newText + this.serverSigns[j].name;
                  break;
                }
              }
            }else if(datas[i].paramName === "assertion"){
              for(let j=0;j<this.assertions.length;j++){
                // 找到断言id对应的name
                if(this.assertions[j].id === datas[i].value){
                  newText = newText + this.assertions[j].name;
                  break;
                }
              }
            }else if(datas[i].paramName === "continue"){
              // 将true/false翻译为中文
              if(datas[i].value === true){
                newText = newText + "是";
              }else{
                newText = newText + "否";
              }
            }
            else{
              if(datas[i].value){
                newText = newText + datas[i].value;
              }
            }
            if(i===0){
              text = newText;
            }else{
              text = text + "<br>" + newText;
            }
          }
          return text;
        },
        addCaseWeb(){
            this.editOperationVisible = true;
            this.$get()
        },
        getOperations(){
          let url = '/auto_test/operation/group/web/list/' + this.$store.state.projectId+"?system=";
            this.$get(url, response =>{
                this.operations = response.data;
            });
        },
        getPages(){
            let url = '/auto_test/module/list/page_module/' + this.$store.state.projectId;
                this.$get(url, response =>{
                this.pageModules = response.data;
            });
        },
        changeOperation(val){
          let operation = this.getOperationObj(val, this.operations);
          this.operationForm.operationId = operation.id;
          this.operationForm.operationName = operation.name;
          console.log(operation.elements);
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
       
        
    }
}
</script>

<style scoped>

</style>
