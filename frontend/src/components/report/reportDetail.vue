<template>
    <div>
        <div class="report-base">
            <el-row :gutter="40" style="margin: 20px -20px">
                <el-col :span="3">
                    <span>成功： <span class="lm-success">{{report.passCount}}</span></span>
                </el-col>
                <el-col :span="3">
                    <span>失败： <span class="lm-fail">{{report.failCount}}</span></span>
                </el-col>
                <el-col :span="3">
                    <span>错误： <span class="lm-error">{{report.errorCount}}</span></span>
                </el-col>
            </el-row>
            <el-row :gutter="40" style="margin: 20px -20px">
                <el-col :span="6">
                    <span>开始时间： {{report.startTime}}</span>
                </el-col>
                <el-col :span="6">
                    <span>结束时间： {{report.endTime}}</span>
                </el-col>
                <el-col :span="6">
                    <span>执行时长： {{report.during}}</span>
                </el-col>
            </el-row>  
    </div>
        <div class="report-base">
            <el-table size="small" :data="report.collectionList" stripe v-loading="loading">
                <el-table-column type="expand">
                    <!-- collectionData是一个作用域插槽变量,它代表了当前行的整个数据对象。 -->
                    <template slot-scope="collectionData"> 
                        <div style="padding-left: 40px">
                        <el-table size="mini" :data="collectionData.row.caseList" stripe>
                            <el-table-column label="用例名称" prop="caseName" min-width="150px"/>
                            <el-table-column label="用例描述" prop="caseDesc" min-width="200px"/>
                            <el-table-column label="开始时间" prop="startTime" width="150px"/>
                            <el-table-column label="结束时间" prop="endTime" width="150px"/>
                            <el-table-column label="执行时长" prop="during" width="120px"/>
                            <el-table-column fixed="right" align="operation" label="操作" width="100px">
                                <template slot-scope="scope">
                                    <el-button type="text" size="mini" @click="viewResult(scope.row)">详情</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="collectionName" label="集合名称" min-width="200"/>
                <el-table-column prop="total" label="用例总数" width="120px"/>
                <el-table-column prop="passCount" label="成功数" width="120px"/>
                <el-table-column prop="failCount" label="失败数" width="120px"/>
                <el-table-column prop="errorCount" label="错误数" width="120px"/>
            </el-table>
        </div>    
    </div>
</template>
<script>
import {timestampToTime} from '@/utils/util'
export default {
    data() {
        return{
            loading:false,
            report: {},
            resultVisable: false,
            caseReportId: null,
            caseType: null
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["测试追踪", "测试报告", "报告详情"]);
        this.getdata(this.$route.params);
    },
    methods: {
        getdata(param){
            let reportId = param.reportId;
            this.$get("/auto_test/report/get/"+reportId,response=>{
                let report = response.data;
                if(report.status === 'success'){
                    report.format = '成功';
                    report.color = '#67C23A';
                }else if(report.status === 'fail'){
                    report.format = '失败';
                    report.color = '#E6A23C';
                }else if(report.status === 'error'){
                    report.format = '错误';
                    report.color = '#F56C6C';
                }else if(report.status === 'completed'){
                    report.format = '完成';
                    report.color = '#535457';
                }else if(report.status === 'prepared'){
                    report.format = '等待执行';
                    report.color = '#409EFF';
                }else if(report.status === 'running'){
                    report.format = "执行中";
                    report.color = '#409EFF';
                }else if(report.status === 'discontinue'){
                    report.format = "已终止";
                    report.color = '#535457';
                }else if(report.status === 'timeout'){
                    report.format = "超时";
                    report.color = '#535457';
                }
                if(!report.startTime){
                    report.startTime = Date.now();
                }
                if(!report.endTime){
                    report.endTime = Date.now();
                }
                report.during = (report.endTime - report.startTime)/1000 + 'S';
                report.startTime = timestampToTime(report.startTime);
                report.endTime = timestampToTime(report.endTime);
                for(let i=0;i<report.collectionList.length;i++){
                    let collection = report.collectionList[i];
                    for(let j=0;j<collection.caseList.length;j++){
                        let collectionCase = collection.caseList[j];
                        collectionCase.startTime = timestampToTime(collectionCase.startTime);
                        collectionCase.endTime = timestampToTime(collectionCase.endTime);
                    }
                }
                this.report = report;
                this.loading = false;
            });
            },
            

        

    }
}
</script>
<style scoped>
.report-header{
    border-bottom: 1px solid rgb(219, 219, 219);
    height: 48px;
    display: flex;
    margin-bottom: 20px;
    align-items: center;
    margin-top: -18px;
}
.report-base{
    border-bottom: 1px solid rgb(219, 219, 219);
    margin-bottom: 10px;
    margin-top: 10px;
}
</style>