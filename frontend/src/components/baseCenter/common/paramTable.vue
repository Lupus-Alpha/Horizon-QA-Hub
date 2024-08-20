/**
 * 参数管理  参数列表
 */
<template>
  <div>
    <!--列表-->
    <el-table size="small" :data="paramData" v-loading="loading">
        <el-table-column prop="index" label="序号" width="80px" align="center"/>
        <el-table-column prop="name" label="参数名称" width="150px"/>
        <el-table-column prop="dataType" label="参数类型" width="100px"/>
        <el-table-column prop="paramData" label="参数值" min-width="200px"/>
        <el-table-column prop="description" label="参数描述" min-width="150px"/>
        <el-table-column prop="updateTime" label="更新时间" width="150px"/>
        <el-table-column prop="createUser" label="创建人" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="100px">
            <template slot-scope="scope">
                <el-button type="text" size="mini" @click="editParam(scope.row)">编辑</el-button>
                <el-button type="text" size="mini" @click="deleteParam(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination :pageparam="pageparam" @callFather="callFather"/>
  </div>
</template>

<script>
import Pagination from '@/components/common/pagination'
export default {
    name: "ParamTable",
    components: {
        Pagination
    },
    props: {
        paramData: Array,
        loading: {
            type: Boolean,
            default: false
        },
        pageparam: Object
    },
    data() {
        return{
        }
    },
    created() {
    },
    methods: {
        // 分页插件事件
        callFather(param) {
            this.$emit("callFather", param);
        },
        editParam(row){
            this.$emit("editParam", row);
        },
        deleteParam(paramId){
            this.$emit("deleteParam", paramId);
        },
    }
}
</script>

<style scoped>

</style>