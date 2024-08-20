<template>
    <div>
        <el-form :inline="true" :model="searchForm">
        <el-form-item label="">
            <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入设备名称"/>
        </el-form-item>
        <el-form-item label="">
            <el-select size="small" v-model="searchForm.system" placeholder="请选择系统">
                <el-option v-for="item in systems" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
        </el-form-item>
        <el-form-item label="">
            <el-select size="small" v-model="searchForm.status" placeholder="请选择状态">
                <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" @click="search">搜索</el-button>
            <el-button size="small" @click="reset">重置</el-button>
        </el-form-item>
    </el-form>
    <el-table size="small" :data="deviceData" v-loading="loading">
        <el-table-column prop="index" label="序号" width="50px" align="center"/>
        <el-table-column prop="name" label="设备名称" min-width="100px"/>
        <el-table-column prop="serial" label="设备号" min-width="150px"/>
        <el-table-column prop="system" label="设备系统" width="100px"/>
        <el-table-column prop="brand" label="设备品牌" width="120px"/>
        <el-table-column prop="model" label="设备型号" width="120px"/>
        <el-table-column prop="version" label="系统版本" width="120px"/>
        <el-table-column prop="status" label="设备状态" width="100px">
            <template slot-scope="scope">
                <span v-if="scope.row.status === 'online'" class="el-icon-circle-check lm-success" style="font-weight:bold"> 在线</span>
                <span v-if="scope.row.status === 'offline'" class="el-icon-circle-close lm-info" style="font-weight:bold"> 离线</span>
                <span v-if="scope.row.status === 'testing'" class="el-icon-video-pause lm-error" style="font-weight:bold"> 测试中</span>
            </template>
        </el-table-column>
        <el-table-column fixed="right" align="operation" label="操作" width="100px">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="viewDevice(scope.row)">查看资源</el-button>
                <el-button type="text" size="mini" @click="useDevice(scope.row)">使用设备</el-button>
            </template>
        </el-table-column>
    </el-table>
    <Pagination :pageparam="pageparam" @callFather="callFather"/>
    <el-dialog title="设备资源信息" :visible.sync="viewDeviceVisible" width="450px" destroy-on-close>
        <el-form style="margin-top:-30px">
            <el-form-item v-if="deviceForm.system='android'" label="atxUrl:">
                <span>{{deviceForm.atxUrl}}</span>
            </el-form-item>
            <el-form-item v-else label="wdaUrl:">
                <span>{{deviceForm.wdaUrl}}</span>
            </el-form-item>
        </el-form>
    </el-dialog>
    </div>
</template>
<script>
import Pagination from '@/components/common/pagination'
export default {
    components: {
        Pagination
    },
    data() {
        return{
            loading:false,
            viewDeviceVisible: false,
            searchForm: {
                page: 1,
                limit: 10,
                condition: "",
                status: null,
                system: null
            },
            systems: [
                {label: "安卓", value: "android"},
                {label: "苹果", value: "apple"}
            ],
            statusList: [
                {label: "在线", value: "online"},
                {label: "离线", value: "offline"},
                {label: "测试中", value: "testing"}
            ],
            deviceData: [],
            pageparam: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            deviceForm: {}
        }
    },
    created() {
        // this.$root.Bus.$emit('initBread', ["环境中心", "设备管理"]);
        this.getdata(this.searchForm);
    },
    methods: {
        // 获取列表数据方法
        getdata(searchParam) {
            this.loading = true
            let url = '/auto_test/device/list/' + searchParam.page + '/' + searchParam.limit;
            let param = {
                condition: searchParam.condition,
                status: searchParam.status,
                system: searchParam.system,
                projectId: this.$store.state.projectId
            };
            this.$post(url, param, response => {
                let data = response.data;
                for(let i =0; i<data.list.length; i++){
                    data.list[i].index = (searchParam.page-1) * searchParam.limit + i+1;
                }
                this.deviceData = data.list;
                this.loading = false;
                // 分页赋值
                this.pageparam.currentPage = this.searchForm.page;
                this.pageparam.pageSize = this.searchForm.limit;
                this.pageparam.total = data.total;
            });
        },
        // 分页插件事件
        callFather(parm) {
            this.searchForm.page = parm.currentPage;
            this.searchForm.limit = parm.pageSize;
            this.getdata(this.searchForm);
        },
        // 搜索按钮
        search() {
            this.getdata(this.searchForm);
        },
        // 重置按钮
        reset() {
            this.searchForm.status = null;
            this.searchForm.system = null;
            this.searchForm.condition = "";
            this.getdata(this.searchForm);
        },
        // 查看设备
        viewDevice(row){
            
            this.deviceForm.system = row.system;
            let sources = {};
            if(row.sources){
                sources = JSON.parse(row.sources);
            }
            
            this.deviceForm.wdaUrl = sources.wdaUrl;
            this.deviceForm.atxUrl = sources.atxUrl;
            console.log(this.deviceForm);
            this.viewDeviceVisible = true;
        },
        useDevice(row){
            if(row.system === 'android'){
                this.$router.push({ path: '/envCenter/androidController'});
            }

        }

    }
}
</script>

<style scoped>

</style>