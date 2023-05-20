package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.service.GoodstypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2023-05-17
 */
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype Goodstype){
        return goodstypeService.save(Goodstype)?Result.success():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype Goodstype) {
        return goodstypeService.updateById(Goodstype)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {

        return goodstypeService.removeById(id)?Result.success():Result.fail();
    }
    //自定义查询 mybatis-p方法,
    @PostMapping("/searchPage")
    public Result searchPage(@RequestBody QueryPageParam query) {

//        //获取entity的其他属性
        HashMap param = query.getParam();
        String name =(String)param.get("name");

        //具体的分页
        Page<Goodstype> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        //在此使用快捷的sql语句，不需要到xml文件中写
        LambdaQueryWrapper<Goodstype> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !("null".equals(name))){
            queryWrapper.like(Goodstype::getName, name);
        }

        IPage result = goodstypeService.pageCC(page,queryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
    //通过no查询
    @GetMapping("/list")
    public Result list(){
        List list = goodstypeService.list();
        return Result.success(list);
    }
}
