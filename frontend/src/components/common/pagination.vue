<template>
<div class="block">
    <el-pagination
      @size-change="call_father"
      @current-change="call_father"
      :current-page=parm.currentPage
      :page-sizes="[5, 10, 15, 20]"
      :page-size=parm.pageSize
      layout="total, sizes, prev, pager, next, jumper"
      :total=parm.total>
    </el-pagination>
</div>
</template>

<script>
export default {
  name: 'pagination',
  props: {
      pageparam: Object

  },
  data() {
    return {
      parm: this.pageparam          
    }
  },
   methods: {      
    call_father(val){
      console.log(this.parm);
      this.param.currentPage = val;
      this.$emit("callFather",this.parm)
    }
},
}
</script>