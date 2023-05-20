<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="please input storag_ename " style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="success" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetParam">重置</el-button>
      <el-button type="success" style="margin-left: 5px;" @click="add">新增</el-button>

    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background: 'rgba(169,169,169,0.3)', corlor : 'rgb(169,169,169)'}"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="仓库名" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注" >
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button type="success" size="small" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="are you sure??"
              @confirm="del(scope.row.id)" style="margin-left: 5px">

            <el-button slot="reference" type="danger" size="small">删除</el-button>
          </el-popconfirm>

        </template>
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

    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>

      <el-form ref="form" :rules="rules" :model="form"  label-width="80px">

        <el-form-item label="仓库名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark"></el-input>
          </el-col>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>

  </div>

</template>

<script>
export default {
  name: "StorageManage",
  data() {
    return {
      //从数据库中返回单数据
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      name:'',
      centerDialogVisible:false,
      form:{
        id:'',
        name:'',
        remark:''
      },
      //限定
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
        ],
      }
    }
  },

  methods: {
    //刷新表单
    resetForm() {
      this.$refs.form.resetFields();
    },
    del(id){
      this.$axios.get(this.$httpUrl+'/storage/del?id='+id,).then(res => res.data).then(res => {
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
      this.$axios.post(this.$httpUrl+'/storage/save', this.form).then(res => res.data).then(res => {
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
      this.$axios.post(this.$httpUrl+'/storage/update', this.form).then(res => res.data).then(res => {
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
    },
    loadPost() {
      //二次传递
      this.$axios.post(this.$httpUrl+'/storage/searchPage', {
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name
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
    }
  },
  beforeMount(){
    this.loadPost();
  }
}
</script>

<style scoped>

</style>