package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 * 增删改查
 *
 * @author wms
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public List<User> list(){
        return  userService.list();
    }

    //通过no查询
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        List<User> list = userService.lambdaQuery().eq(User::getNo, no).list();
        return list.size() > 0 ? Result.success(list) : Result.fail();
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user)?Result.success():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateById(user)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return userService.removeById(id)?Result.success():Result.fail();
    }
    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List list = userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword()).list();

        if(list.size()>0){
            User user1 = (User)list.get(0);
            List menulist = menuService.lambdaQuery().like(Menu::getMenuright, user1.getRoleId()).list();
            HashMap res =new HashMap();
            res.put("user",user1);
            res.put("menu",menulist);
           return Result.success(res);
        }
        return Result.fail();
    }

    //一下是使用mabatis-p基本的方法
    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user) {
        return userService.updateById(user);
    }
    //新增或修改
    @PostMapping("/saveorMod")
    public boolean delete(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }
    //删除
    @GetMapping("/delete")
    public boolean delete(Integer id){
        return userService.removeById(id);
    }
    //模糊查询
    @PostMapping("/search")
    public Result search(@RequestBody User user) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    //先判断姓名
    if(StringUtils.isNotBlank(user.getName())){
        queryWrapper.like(User::getName, user.getName());
    }
        return  Result.success(userService.list(queryWrapper));
    }

    //分页查询(mybatis-plus中的方法)
//    @PostMapping("/searchPage")
//    public List<User> searchPage(@RequestBody QueryPageParam query) {
//
////        return userService.list(queryWrapper);
////        System.out.println(query);
//
////        //获取entity的其他属性
//        HashMap param = query.getParam();
//        String name = param.get("name").toString();
//        System.out.println("name"+(String)param.get("name"));
////        System.out.println("no"+(String)param.get("no"));
//
//        Page<User> page = new Page();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
//            queryWrapper.like(User::getName, name);
//        }
//
//        IPage result = userService.page(page,queryWrapper);
//        System.out.println("total"+result.getTotal());
//        return result.getRecords();
//
//    }

    //自定义查询 需要再UserMapper.xml文件修改自定义的查询sql语句
//    @PostMapping("/searchPageC")
//    public List<User> searchPageC(@RequestBody QueryPageParam query) {
//
////        //获取entity的其他属性
//        HashMap param = query.getParam();
//        String name = param.get("name").toString();
//        System.out.println("name"+(String)param.get("name"));
//
//        //具体的分页
//        Page<User> page = new Page();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//
////        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
////        queryWrapper.like(User::getName, name);
//
//        IPage result = userService.pageC(page);
//        System.out.println("total"+result.getTotal());
//        return result.getRecords();
//
//    }

    //自定义查询 mybatis-p方法,
    @PostMapping("/searchPageCCC")
    public Result searchPageCCC(@RequestBody QueryPageParam query) {

//        //获取entity的其他属性
        HashMap param = query.getParam();
        String name =(String)param.get("name");
        String sex = (String)param.get("sex");
        String roleId = (String)param.get("roleId");
        //具体的分页
        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        //在此使用快捷的sql语句，不需要到xml文件中写
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !("null".equals(name))){
            queryWrapper.like(User::getName, name);
        }

        if(StringUtils.isNotBlank(sex)){
            queryWrapper.eq(User::getSex, sex);
        }

        if(StringUtils.isNotBlank(roleId)){
            queryWrapper.eq(User::getRoleId, roleId);
        }

        IPage result = userService.pageCC(page,queryWrapper);
        System.out.println("total"+result.getTotal());
        return Result.success(result.getRecords(),result.getTotal());

    }
}