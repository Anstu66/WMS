<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="please input goods_ename " style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="goodstype" placeholder="请选择分类" style="margin-left: 5px">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="success" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetParam">重置</el-button>
      <el-button type="success" style="margin-left: 5px;" @click="add" v-if="user.roleId !=2">新增</el-button>

    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background: 'rgba(169,169,169,0.3)', corlor : 'rgb(169,169,169)'}"
              border
    >
      //从后端拿取返回到前端
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="goodsname" label="物品名" width="180">
      </el-table-column>
      <el-table-column prop="storagename" label="仓库" width="180">
      </el-table-column>
      <el-table-column prop="goodstypename" label="分类" width="180">
      </el-table-column>

      <el-table-column prop="username" label="申请人" width="180">
      </el-table-column>
      <el-table-column prop="adminame" label="操作人" width="180">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="createtime" label="操作时间" width="180">
      </el-table-column>

      <el-table-column prop="remark" label="备注" >
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5,10,20,30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

  </div>

</template>

<script>

export default {
  name: "RecordManage",
  data() {
    return{
      user: JSON.parse(sessionStorage.getItem('CurUser')),
      storageData:[],
      goodstypeData:[],
      //从数据库中返回单数据
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      name:'',
      storage:'',
      goodstype:'',
      centerDialogVisible:false,
      form:{
        id:'',
        name:'',
        storage:'',
        goodstype:'',//要和数据库中的数据名字保持一致
        count:'',
        remark:''
      },
      //限定
    }
  },

  methods: {
    //拿到数据,将1 转化为分类名
    formatGoodstype(row){
      let temp = this.goodstypeData.find(item=>{
        return item.id == row.goodstype
      })
      return temp && temp.name;
    },
    //拿到数据,将1 转化为仓库名
    formatStorage(row){
      let temp = this.storageData.find(item=>{
        return item.id == row.storage
      })
      return temp && temp.name;
    },
    //刷新表单
    resetForm() {
      this.$refs.form.resetFields();
    },
    del(id){
      this.$axios.get(this.$httpUrl+'/goods/del?id='+id,).then(res => res.data).then(res => {
        if(res.code==200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost()//刷新页面
        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
    },
    mod(row){

      //异步通信
      this.centerDialogVisible=true//出现表单
      this.$nextTick(()=>{
        //复制到表单
        this.form.id=row.id
        this.form.name=row.name
        this.form.storage=row.storage
        this.form.goodstype=row.goodstype
        this.form.count=row.count
        this.form.remark=row.remark
      })
    },
    add(){
      this.centerDialogVisible=true//出现表单
      this.$nextTick(()=>{
        this.resetForm();
      })
    },

    doSave(){
      this.$axios.post(this.$httpUrl+'/record/save', this.form).then(res => res.data).then(res => {
        if(res.code==200){
          this.$message({
            message: '新增成功！',
            type: 'success'
          });
          this.centerDialogVisible=false//关闭当前窗口
          this.loadPost()//刷新页面
        }else{
          this.$message({
            message: '新增失败',
            type: 'error'
          });
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/record/update', this.form).then(res => res.data).then(res => {
        if(res.code==200){
          this.$message({
            message: '新增成功！',
            type: 'success'
          });
          this.centerDialogVisible=false//关闭当前窗口
          this.loadPost()//刷新页面
        }else{
          this.$message({
            message: '新增失败',
            type: 'error'
          });
        }
      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else{
            this.doSave();
          }
        } else {
          return false;
        }
      });
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    resetParam(){
      this.name=''
      this.storage=''
      this.goodstype=''
    },
    loadStorage(){
      //从后端加载表中的数据返回到前端
      this.$axios.get(this.$httpUrl+'/storage/list').then(res => res.data).then(res => {
        console.log(res)
        if(res.code==200){
          this.storageData=res.data
        }else{
          alert("fail")
        }
      })
    },
    loadGoodstype(){
      //从后端加载表中的数据返回到前端
      this.$axios.get(this.$httpUrl+'/goodstype/list').then(res => res.data).then(res => {
        console.log(res)
        if(res.code==200){
          this.goodstypeData=res.data
        }else{
          alert("fail")
        }
      })
    },
    loadPost() {
      //二次传递
      this.$axios.post(this.$httpUrl+'/record/searchPage', {
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          goodstype: this.goodstype+'',
          storage: this.storage+'',
          roleId:this.user.roleId +'',
          userId:this.user.id+''
        }
      }).then(res => res.data).then(res => {
        console.log(res)
        if(res.code==200){
          this.tableData=res.data
          this.total=res.total
        }else{
          alert("fail")
        }
      })
    },
  },
  beforeMount(){
    this.loadGoodstype()
    this.loadStorage()
    this.loadPost()
  }
}
</script>

<style scoped>

</style>