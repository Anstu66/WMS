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
        <el-button type="success" style="margin-left: 5px;" @click="inbound" v-if="user.roleId !=2">入库</el-button>
      <el-button type="success" style="margin-left: 5px;" @click="outbound" v-if="user.roleId !=2">出库</el-button>

    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background: 'rgba(169,169,169,0.3)', corlor : 'rgb(169,169,169)'}"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="物品名" width="180">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="180" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodstype" label="分类" width="180" :formatter="formatGoodstype">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注" >
      </el-table-column>
      <el-table-column prop="operate" label="操作" v-if="user.roleId !=2">
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
        title="新增物品"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>

      <el-form ref="form" :rules="rules" :model="form"  label-width="80px">

        <el-form-item label="物品名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="仓库名" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" placeholder="请选择仓库" style="margin-left: 5px">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="分类" prop="goodstype">
          <el-col :span="20">
            <el-select v-model="form.goodstype" placeholder="请选择仓库" style="margin-left: 5px">
              <el-option
                  v-for="item in goodstypeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
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


    <el-dialog
        title="出入库"
        :visible.sync="inDialogVisible"
        width="30%"
        center>

      <el-dialog
          width="70%"
      title="用户选择"
      :visible.sync="innerVisble"
      appen-to-body>
        <SelectUser @doselectUser="doselectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="confirmUser">确 定</el-button>
  </span>
      </el-dialog>

      <el-form ref="form1" :rules="rules1" :model="form1"  label-width="80px">

        <el-form-item label="物品名">
          <el-col :span="20">
            <el-input v-model="form1.goodsname" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人">
          <el-col :span="20">
            <el-input v-model="form1.username"  @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form1.count"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form1.remark"></el-input>
          </el-col>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doInbouns">确 定</el-button>
  </span>
    </el-dialog>

  </div>

</template>

<script>
import SelectUser from "@/components/user/SelectUser";

export default {
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule, value, callback) => {
      if(value>9999){
        callback(new Error('数量输⼊过⼤'));
      }else{
        callback();
      }
    };

    return {
      user : JSON.parse(sessionStorage.getItem('CurUser')),
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
      inDialogVisible:false,
      innerVisble:false,
      currentNow:{},
      tempUser:{},
      form:{
        id:'',
        name:'',
        storage:'',
        goodstype:'',//要和数据库中的数据名字保持一致
        count:'',
        remark:''
      },
      form1:{
        goods:'',
        userid:'',
        username:'',
        adminId:'',
        goodsname:'',
        count:'',
        remark:'',
        action:'1'
      },
      rules1:{
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        count: []
      },
      //限定
      rules: {
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        goodstype:[
          {required: true, message: '请输入分类', trigger: 'blur'},
        ],
        storage:[
          {required: true, message: '请输入仓库', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ],
      }
    }
  },

  methods:{
    doselectUser(val){
      this.tempUser = val;
      },
    confirmUser(){
      this.form1.username= this.tempUser.name;
      this.form1.userid= this.tempUser.id
      this.innerVisble=false
    },
    selectUser(){
      this.innerVisble=true
    },
    selectCurrentChange(val){
      this.currentNow=val;
    },
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
    resetInForm(){
      this.$refs.form1.resetFields();
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
    inbound(){
      if(!this.currentNow.id){
        alert("please select record");
        return;
      }
      this.$nextTick(()=>{
        this.resetInForm();
      })
      this.inDialogVisible=true

      this.form1.goodsname=this.currentNow.name
      this.form1.goods=this.currentNow.id
      this.form1.adminId=this.user.id
      this.form1.action=1

      // 清空校验规则
      this.rules1.count = [];

    },
    outbound() {
      if (!this.currentNow.id) {
        alert("Please select a record");
        return;
      }
      this.$nextTick(() => {
        this.resetInForm();
      });
      this.inDialogVisible = true;
      this.form1.goodsname = this.currentNow.name;
      this.form1.goods = this.currentNow.id;
      this.form1.adminId = this.user.id;

      this.form1.action = '2';
      //动态设置出库的规则
      this.rules1.count = [
        { required: true, message: '请输⼊数量', trigger: 'blur' },
        { pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数字', trigger: 'blur' },
        { validator: this.checkOutCount, trigger: 'blur' }
      ];
    },
      //检查出库的数量和物品数量
    checkOutCount(rule, value, callback){
        const availableCount = this.currentNow.count; // 当前物品数量
        if (value > availableCount) {
          callback(new Error('出库数量不能大于物品数量'));
        } else {
          callback();
        }
      },
    doSave(){
      this.$axios.post(this.$httpUrl+'/goods/save', this.form).then(res => res.data).then(res => {
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
      this.$axios.post(this.$httpUrl+'/goods/update', this.form).then(res => res.data).then(res => {
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
    doInbouns(){
      this.$axios.post(this.$httpUrl+'/record/save', this.form1).then(res => res.data).then(res => {
        if(res.code==200){
          this.$message({
            message: '新增成功！',
            type: 'success'
          });
          this.inDialogVisible=false//关闭当前窗口
          this.loadPost()//刷新页面
          this.resetInForm()
        }else{
          this.$message({
            message: '新增失败',
            type: 'error'
          });
        }
      })
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
    loadPost() {
      //二次传递
      this.$axios.post(this.$httpUrl+'/goods/searchPage', {
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          goodstype: this.goodstype+'',
          storage: this.storage+''
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
  },
  beforeMount(){
    this.loadStorage()
    this.loadGoodstype()
    this.loadPost()
  }
}
</script>

<style scoped>

</style>