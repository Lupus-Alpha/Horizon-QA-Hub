<template>
  <div>
    <page-header title="编辑集合" :cancel="cancelAdd" :save="saveAdd"/>
    <el-form ref="collectionForm" :rules="rules" :model="collectionForm" label-width="80px">
        <el-row :gutter="40">
            <el-col :span="16">
                <el-form-item size="small" label="集合名称" prop="name">
                    <el-input style="width: 100%" v-model="collectionForm.name" placeholder="请输入集合名称"/>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item size="small" label="执行设备" prop="deviceId">
                    <el-select style="width: 90%" v-model="collectionForm.deviceId" clearable placeholder="请选择执行设备">
                        <el-option v-for="item in deviceList" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                    <el-tooltip style="width:5%" content="当前集合包含APP测试时 执行设备必选" placement="bottom">
                        <i class="el-icon-info"></i>
                    </el-tooltip>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="40">
            <el-col :span="16">
                <el-form-item size="small" label="集合描述" style="margin-bottom:0px">
                    <el-input style="width: 100%" clearable placeholder="请输入集合描述" v-model="collectionForm.description"/>
                </el-form-item>
            </el-col>
        </el-row>
        <el-form-item style="margin-left:-80px;" prop="collectionCases"/>
        <el-table :data="collectionForm.collectionCases" row-key="id" class="sort-table" size="small">
            <el-table-column width="70px">
                <template slot-scope="scope">
                    <el-button v-if="scope.$index!==0" size="mini" type="text" icon="el-icon-top" @click="up(scope.$index)"/>
                    <el-button v-if="scope.$index!==(collectionForm.collectionCases.length-1)" size="mini" type="text" icon="el-icon-bottom" @click="down(scope.$index)"/>
                </template>
            </el-table-column>
            <el-table-column label="序号" prop="index" width="100px">
            </el-table-column>
            <el-table-column label="用例名称" prop="caseName" min-width="180px">
            </el-table-column>
            <el-table-column label="用例模块" prop="caseModule">
            </el-table-column>
            <el-table-column label="用例类型" prop="caseType">
            </el-table-column>
            <el-table-column label="用例系统" prop="caseSystem">
            </el-table-column>
            <el-table-column label="操作" width="120px">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="deleteCollectionCase(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button size="small" icon="el-icon-plus" type="text" @click="selectCaseVisible=true">新增</el-button>  
    </el-form>
    <el-dialog title="选择用例" :visible.sync="selectCaseVisible" width="800px" destroy-on-close>
        <select-case :selections="selections" :selectCaseVisible="selectCaseVisible"/>
        <!-- 使用 slot="footer" 属性可以将 <div> 元素的内容插入到 <el-dialog> 组件的 "footer" 插槽中。 -->
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="selectCaseVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="selectCaseSave">保存</el-button>
        </div>
    </el-dialog>
    
  </div>
</template>

<script>
import PageHeader from '../common/pageheader'
import {getUUID} from '@/utils/util'
import SelectCase from './common/selectCase'
export default {
    components:{PageHeader,SelectCase},
    data() {
        return{
            selections:[],
            selectCaseVisible: false,
            collectionForm: {
                id: "",
                name: "",
                deviceId: null,
                description: "",
                collectionCases:[]
            },
            deviceList: [],
            rules: {
                name: [{ required: true, message: '集合名称不能为空', trigger: 'blur' }],
                collectionCases: [{ required: true, message: '请至少选择一条测试用例', trigger: 'blur' }],
            }
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["计划中心", "测试集合", "集合编辑"]);
        this.getDetail(this.$route.params);
        this.getDevice();
    },
    methods: {
        getDevice(){
            let url = "/auto_test/device/all/total/"+this.$store.state.projectId;
            this.$get(url, response => {
                this.deviceList = response.data;
            });
        },
        up(index){
            this.collectionForm.collectionCases[index-1]=this.collectionForm.collectionCases.splice(index,1,this.collectionForm.collectionCases[index-1])[0];
            this.sortCollectiionCase();
        },
        down(index){
            this.collectionForm.collectionCases[index]=this.collectionForm.collectionCases.splice(index+1,1,this.collectionForm.collectionCases[index])[0];
            this.sortCollectiionCase();
        },
        // 重新排序
        sortCollectiionCase(){
            for(let i=0; i<this.collectionForm.collectionCases.length; i++){
                this.collectionForm.collectionCases[i].index = i+1; //重新遍历赋值
            }
        },
        // 保存用例选择
        selectCaseSave(){
            for(let i=0;i<this.selections.length;i++){
                let collectionCase = {
                    id: getUUID(),
                    index: this.collectionForm.collectionCases.length+1,//列表数据长度加一
                    caseId: this.selections[i].id,
                    caseName: this.selections[i].name,
                    caseModule: this.selections[i].moduleName,
                    caseType: this.selections[i].type,
                    caseSystem: this.selections[i].system,
                }
                this.collectionForm.collectionCases.push(collectionCase);
            }
            this.selections.splice(0, this.selections.length);
            this.selectCaseVisible = false;
        },
        deleteCollectionCase(index){
            this.collectionForm.collectionCases.splice(index, 1);
            this.sortCollectiionCase();
        },
        getDetail(param){
            if (param.collectionId){
                let url = "/auto_test/collection/detail/" + param.collectionId;
                this.$get(url, response => {
                    this.collectionForm = response.data;
                });
            }
        },
        cancelAdd(){
            this.$router.push({path: '/planManage/testCollection'})
        },
        saveAdd(){
            this.$refs["collectionForm"].validate(valid => {
                if (valid) {
                    this.collectionForm.projectId = this.$store.state.projectId;
                    this.sortCollectiionCase();
                    let url = '/auto_test/collection/save';
                    this.$post(url, this.collectionForm, response =>{
                        this.$message.success("保存成功");
                        this.$router.push({path: '/planManage/testCollection'});
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
