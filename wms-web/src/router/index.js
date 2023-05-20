 import VueRouter from "vue-router";
 import LoginPage from '../components/Login';
 import IndexPage from '../components/Index';
 import HomePage from '../components/Home';
 // import AdminManage from "@/components/admin/AdminManage";
 // import UserManage from "@/components/user/UserManage";

const routes= [
{
    path:'/',
    name:'login',
    component: LoginPage
},
    {
        path:'/Index',
        name:'index',
        component: IndexPage,
        children :[
            {
                path:'/Home',
                name:'home',
                meta:{
                    title:'首页'
                },
                component: HomePage,
            },
           /* {
                path:'/Admin',
                name:'admin',
                meta:{
                    title:'管理员管理'
                },
                component: AdminManage,
            },
            {
                path:'/User',
                name:'user',
                meta:{
                    title:'用户管理'
                },
                component: UserManage,
            },*/
        ]
    }
]

 const router =new VueRouter({
    mode:'history',
     routes
 })


 //错误处理
 const VueRouterPush = VueRouter.prototype.push
 VueRouter.prototype.push = function push (to) {
     return VueRouterPush.call(this, to).catch(err => err)
 }

 export function resetRouter(){
    router.matcher = new VueRouter({
        mode:'history',
        routes:[]
    }).matcher
 }

 export default router;
