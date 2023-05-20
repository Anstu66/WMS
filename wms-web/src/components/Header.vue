<template>
  <div style="display: flex;line-height: 60px;">
    <div style="margin: 8px;">
    <i :class="icon" style="font-size: 20px; cursor: pointer " @click="collapse"></i>
  </div>
    <div style="flex: 1;text-align: center;font-size: 34px;">
    <span>welcome to wms !</span>
    </div>

  <el-dropdown>
    <span>{{user.name}}</span>
    <i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
      <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>

  </div>
</template>

<script>
export default {
  name: "HeaderPage",
  data(){
    return{
      user : JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  props:{
    icon:String
  },
  methods:{
    toUser(){
      console.log('to_User')
      this.$router.push("/Home")//退回登录页面

    },
    logout(){
      console.log('logout')

      this.$confirm('are you sure?','提示',{
      confirmButtonText:'yes',
      type:'warning',
        center:true,
      })
        .then(()=>{
            this.$message({
              type:'success',
              message:'成功退出'
            })

          this.$router.push("/")//退回登录页面

          sessionStorage.clear()//清空当前页面数据
          })
          .catch(()=>{
            this.$message({
              type:'info',
              message:'手滑了亿下'
            })
          })
    },
    collapse() {
      this.$emit('doCollapse')
       }
    },
    created(){
     this.$router.push("/Home")
  }
}
</script>

<style scoped>

</style>