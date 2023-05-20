<template>
  <div id="app">
    <router-view/>
<!--    <Index></Index>-->
  </div>
</template>

<script>

// import Index  from "./components/Index";

export default {
  name: 'App',
  components: {
    // Index
  },
  //持久化管理界面,刷新后界面的内容存储到seesion中，刷新后再调用，并重新加载动态路由
  data(){
    return{
      user : JSON.parse(sessionStorage.getItem("CurUser")),
    }
  },
  //监视页面变化并处理
  watch:{
    '$store.state.menu':{
      handler(val,old) {
        if(!old && this.user && this.user.no){
          this.$store.commit("setMenu",val)
        }
      },
      immediate:true
    }
  }
}
</script>

<style>
#app {
  height: 100%;
}
</style>
