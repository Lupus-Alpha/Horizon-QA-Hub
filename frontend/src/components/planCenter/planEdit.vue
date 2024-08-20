<template>
  <div>
    <page-header title="编辑计划" :cancel="cancelAdd" :save="saveAdd"/>
    <el-form ref="planForm" :rules="rules" :model="planForm" label-width="80px">
        <el-row :gutter="40">
            <el-col :span="16">
                <el-form-item label="计划名称" prop="name">
                    <el-input size="small" style="width: 100%" v-model="planForm.name" placeholder="请输入计划名称"/>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="失败重试" prop="retry">
                    <el-radio-group style="width: 60%" v-model="planForm.retry">
                        <el-radio :label="'Y'">是</el-radio>
                        <el-radio :label="'N'">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="40">
            <el-col :span="8">
                <el-form-item label="执行引擎" prop="engineId">
                    <el-select size="small" style="width: 100%" v-model="planForm.engineId" placeholder="请选择执行引擎">
                        <el-option v-for="item in engineList" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="执行环境" prop="environmentId">
                    <el-select size="small" style="width: 90%" v-model="planForm.environmentId" clearable placeholder="请选择执行环境">
                        <el-option v-for="item in environmentList" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                    <el-tooltip style="width:5%" content="当前计划包含API/WEB用例时 环境不能为空" placement="bottom">
                        <i class="el-icon-info"></i>
                    </el-tooltip>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="40">
            <el-col :span="16">
                <el-form-item label="计划描述" style="margin-bottom:0px">
                    <el-input size="small" style="width: 100%" clearable placeholder="请输入计划描述" v-model="planForm.description">
                    </el-input>
                </el-form-item>
            </el-col>
        </el-row>
        <el-form-item style="margin-left:-80px;" prop="planCollections"/>
        <el-table :data="planForm.planCollections" size="small">
            <el-table-column label="序号" prop="index" width="100px">
            </el-table-column>
            <el-table-column label="集合名称" prop="collectionName" min-width="180px">
            </el-table-column>
            <el-table-column label="集合描述" prop="collectionDescription" min-width="240px">
            </el-table-column>
            <el-table-column label="操作" width="100px">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="deletePlanCollection(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button size="small" icon="el-icon-plus" type="text" @click="selectCollectionVisible=true">新增集合</el-button>
    </el-form>
    <!-- 添加集合界面 -->
    <el-dialog title="选择集合" :visible.sync="selectCollectionVisible" width="800px" destroy-on-close>
        <select-collection :selections="selections" :selectCollectionVisible="selectCollectionVisible"/>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="selectCollectionVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="selectCollectionSave">保存</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import PageHeader from '../common/pageheader'
import SelectCollection from './common/selectCollection'
import {getUUID} from '@/utils/util'
export default {
    components:{PageHeader, SelectCollection},
    data() {
        return{
            selections: [],
            selectCollectionVisible: false,
            planForm: {
                id: "",
                name: "",
                environmentId: null,
                engineId: "",
                retry:"N",
                description: "",
                planCollections:[]
            },
            environmentList: [],
            engineList: [],
            collectionListData: [], 
            rules: {
                name: [{ required: true, message: '计划名称不能为空', trigger: 'blur' }],
                engineId: [{ required: true, message: '执行引擎不能为空', trigger: 'blur' }],
                retry: [{ required: true, message: '失败重试不能为空', trigger: 'blur' }],
                planCollections: [{ required: true, message: '请至少选择一条测试集合', trigger: 'blur' }]
            }    
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["计划中心", "测试计划", "计划编辑"]);
        this.getEnvironment();
        this.getEngine();
        this.getDetail(this.$route.params);
    },
    methods: {
        getEnvironment(){
            let url = "/auto_test/environment/all/" + this.$store.state.projectId;
            this.$get(url, response => {
                this.environmentList = response.data;
            });
        },
        getEngine(){
            let url = "/auto_test/engine/all/" + this.$store.state.projectId;
            this.$get(url, response => {
                this.engineList = response.data;
            });
        },
        getDetail(param){
            if (param.planId){
                let url = "/auto_test/plan/detail/" + param.planId;
                this.$get(url, response => {
                    let data = response.data;
                    for(let i=0;i<data.planCollections.length;i++){
                        data.planCollections[i].index = i+1;
                    }
                    this.planForm = data;
                });
            }
        },
        // 保存用例选择
        selectCollectionSave(){
            for(let i=0;i<this.selections.length;i++){
                let planCollection = {
                    id: getUUID(),
                    index: this.planForm.planCollections.length+1,
                    collectionId: this.selections[i].id,
                    collectionName: this.selections[i].name,
                    collectionDescription: this.selections[i].description
                }
                this.planForm.planCollections.push(planCollection);
            }
            this.selectCollectionVisible = false;
        },
        deletePlanCollection(index){
            this.planForm.planCollections.splice(index, 1);
            for(let i=0; i<this.planForm.planCollections.length; i++){
                this.planForm.planCollections[i].index = i+1;
            }
        },
        cancelAdd(){
            this.$router.push({path: '/planManage/testPlan'})
        },
        saveAdd(){
            this.$refs["planForm"].validate(valid => {
                if (valid) {
                    this.planForm.projectId = this.$store.state.projectId;
                    let url = '/auto_test/plan/save';
                    this.$post(url, this.planForm, response =>{
                        this.$message.success("保存成功");
                        this.$router.push({path: '/planManage/testPlan'});
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