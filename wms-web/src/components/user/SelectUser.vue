<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="please input name " style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left: 5px;" >
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="success" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetParam">重置</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background: 'rgba(169,169,169,0.3)', corlor : 'rgb(169,169,169)'}"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="no" label="账号" width="180">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="180">
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="80">
      </el-table-column>
      <el-table-column prop="sex" label="性别" width="80">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.sex === 1 ? 'danger' : scope.row.sex === 0 ? 'primary' : 'success'"
              disable-transitions>{{scope.row.sex === 1 ? '男' : (scope.row.sex === 0 ? '女' : '其他')
            }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleId" label="角色" width="120">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
              disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' :
              (scope.row.roleId === 1 ? '管理员' : '用户')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="180">
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

        <el-form-item label="账户" prop="no">
          <el-col :span="20">
            <el-input v-model="form.no"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="年龄" prop="age">
          <el-col :span="20">
            <el-input v-model="form.age"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="性别" >
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
            <el-radio label="2">其他</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
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
let checkAge;
export default {
  name: "SelectUser",
  data() {
    //验证年龄
    checkAge = (rule, value, callback) => {
      if(value>150){
        callback(new Error('年龄输⼊过⼤'));
      }else{
        callback();
      }
    };
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.id){
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{
        if(res.code === 200){
          callback(new Error('账号已经存在'));
        }else{
          callback();
        }
      })
    };
    return {
      //从数据库中返回单数据
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      name:'',
      sex:'',
      sexs:[
        {
          value:'1',
          label:'男'
        },{
          value:'0',
          label:'女'
        },{
          value:'2',
          label:'其他'
        }
      ],
      centerDialogVisible:false,
      form:{
        id:'',
        no:'',
        password:'',
        name:'',
        age:'',
        sex:'0',
        phone:'',
        roleId:'2'
      },
      //限定
      rules: {
        no: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输⼊年龄', trigger: 'blur'},
          {min: 1, max: 3, message: '⻓度在 1 到 3 个位', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数字',trigger: "blur"},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
          {required: true,message: "⼿机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输⼊正确的⼿机号码", trigger:
                "blur"}
        ]
      }
    }
  },

  methods: {
    selectCurrentChange(val){
      // this.currentNow=val;
      this.$emit("doselectUser",val);
    },
    //刷新表单
    resetForm() {
      this.$refs.form.resetFields();
    },
    del(id){
      console.log(id)
      this.$axios.get(this.$httpUrl+'/user/del?id='+id,).then(res => res.data).then(res => {
        if(res.code==200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost()//刷新页面
        }else{
          this.$message({
            message: '新增失败',
            type: 'error'
          });
        }
      })
    },
    mod(row){
      //拿到该行的数据
      console.log(row)

      //异步通信
      this.centerDialogVisible=true//出现表单
      this.$nextTick(()=>{
        //复制到表单
        this.form.id=row.id
        this.form.no=row.no
        this.form.password=''
        this.form.name=row.name
        this.form.age=row.age+''
        this.form.sex=row.sex+''
        this.form.phone=row.phone
        this.form.roldId=row.roldId
      })
    },
    add(){
      this.centerDialogVisible=true//出现表单
      this.$nextTick(()=>{
        this.resetForm();
      })
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/user/save', this.form).then(res => res.data).then(res => {
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
      this.$axios.post(this.$httpUrl+'/user/update', this.form).then(res => res.data).then(res => {
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
    loadGet() {
      //二次传递
      this.$axios.get(this.$httpUrl+'/user/list').then(res => res.data).then(res => {
        console.log(res)
      })
    },
    resetParam(){
      this.name=''
      this.sex=''
    },
    loadPost() {
      //二次传递
      this.$axios.post(this.$httpUrl+'/user/searchPageCCC', {
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          sex:this.sex,
          roleId: '2'
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
    //this.loadGet();
    this.loadPost();
  }
}
</script>

<style scoped>

</style>