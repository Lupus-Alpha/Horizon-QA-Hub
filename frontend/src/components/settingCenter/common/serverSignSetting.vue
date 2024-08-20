/**
 * 设置中心  服务标识
 */
<template>
  <div>
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="">
        <el-input size="small" v-model="searchForm.condition" prefix-icon="el-icon-search" placeholder="请输入标识名称"/>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="search">搜索</el-button>
        <el-button size="small" @click="reset">重置</el-button>
      </el-form-item>
      <el-form-item style="float: right">
        <el-button size="small" v-if="showOpt" type="primary" icon="el-icon-plus" @click="addServerSign">新增标识</el-button>
      </el-form-item>
    </el-form>
    <el-table size="small" :data="serverSignData" v-loading="loading">
        <el-table-column prop="index" label="序号" align="center" width="50px"/>
        <el-table-column prop="name" label="标识名称"/>
        <el-table-column prop="description" label="标识说明" min-width="200px"/>
        <el-table-column prop="updateTime" label="更新时间" width="150px"/>
        <el-table-column fixed="right" align="operation" label="操作" width="100px">
            <template slot-scope="scope">
                <el-button v-if="showOpt" type="text" size="mini" @click="editServerSign(scope.row)">编辑</el-button>
                <el-button v-if="showOpt" type="text" size="mini" @click="deleteServerSign(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparam" @callFather="callFather"/>
    <!-- 新增标识弹窗 -->
    <el-dialog title="新增标识" :visible.sync="serverSignVisible" width="600px" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 30px;" :model="serverSignForm" :rules="rules" ref="serverSignForm">
            <el-form-item label="标识名称" prop="name">
                <el-input size="small" style="width:95%" v-model="serverSignForm.name" placeholder="请输入标识名称"/>
            </el-form-item>
            <el-form-item label="标识描述" prop="description">
                <el-input size="small" style="width:95%" v-model="serverSignForm.description" type="textarea" 
                  :autosize="{ minRows: 3}" clearable placeholder="请输入标识描述" maxlength="200" show-word-limit/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="small" @click="serverSignVisible=false">取消</el-button>
            <el-button size="small" type="primary" @click="submitServerSign">确定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/common/pagination'
import {timestampToTime} from '@/utils/util'
export default {
    name: "ServerSignSetting",
    props: {
      showOpt: {
        type: Boolean,
        default: false
      },
      activeName: {
        type: String,
        default: ""
      }
    },
    components: {
      Pagination
    },
    data() {
        return{
          loading: false,
          serverSignData: [],
          searchForm: {
            page: 1,
            limit: 10,
            condition: ""
          },
          pageparam: {
            currentPage: 1,
            pageSize: 10,
            total: 0
          },
          serverSignVisible: false,
          serverSignForm: {},
          rules: {
              name: [{ required: true, message: '标识名称不能为空', trigger: 'blur' }],
              description: [{ required: true, message: '标识描述不能为空', trigger: 'blur' }]
          }
        }
    },
    created(){
      this.getData(this.searchForm);
    },
    watch: {
        activeName(){
          if(this.activeName === "serverSign"){
            this.getData(this.searchForm);
          }
        }
    },
    methods: {
      getData(searchParam){
        let url = "/auto_test/server_sign/list/" + searchParam.page + '/' + searchParam.limit;;
        let param = {
            projectId: this.$store.state.projectId,
            condition: searchParam.condition
        };
        this.$post(url, param, response => {
            let data = response.data;
            for(let i =0; i<data.list.length; i++){
                data.list[i].updateTime = timestampToTime(data.list[i].updateTime);
                data.list[i].index = (this.pageparam.currentPage-1) * this.pageparam.pageSize + i+1;
            }
            this.serverSignData = data.list;
            this.loading = false;
            // 分页赋值
            this.pageparam.currentPage = this.searchForm.page;
            this.pageparam.pageSize = this.searchForm.limit;
            this.pageparam.total = data.total;
        });
      },
      // 搜索按钮
      search() {
          this.getData(this.searchForm);
      },
      // 重置按钮
      reset() {
          this.searchForm.condition = "";
          this.getData(this.searchForm);
      },
      // 分页插件事件
      callFather(param) {
          this.searchForm.page = param.currentPage;
          this.searchForm.limit = param.pageSize;
          this.getData(this.searchForm);
      },
      addServerSign(){
        this.serverSignForm = {};
        this.serverSignVisible = true;
      },
      editServerSign(row){
        this.serverSignForm = {
          id: row.id,
          name: row.name,
          description: row.description,
          createTime: row.createTime
        };
        this.serverSignVisible = true;
      },
      deleteServerSign(signId){
        this.$confirm("确定要删除域名标识吗?", '删除提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        .then(() => {
            let url = "/auto_test/server_sign/delete/"+signId;
            this.$post(url, {}, response => {
                this.$message.success("删除成功");
                this.getData(this.searchForm);
            });
        })
        .catch(() => {
            this.$message.success("取消成功");
        })
      },
      submitServerSign(){
        this.$refs["serverSignForm"].validate(valid => {
            if (valid) {
                this.serverSignForm.projectId = this.$store.state.projectId;
                let url = "/auto_test/server_sign/add";
                this.$post(url, this.serverSignForm, response =>{
                    this.$message.success("保存成功");
                    this.serverSignVisible = false;
                    this.getData(this.searchForm);
                });
            }else{
                return false;
            }
        });
      }
    }

}
</script>

<style scoped>

</style>
