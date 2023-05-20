package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.Storage;
import com.wms.service.StorageService;
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
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage)?Result.success():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage) {
        return storageService.updateById(storage)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {

        return storageService.removeById(id)?Result.success():Result.fail();
    }
    //自定义查询 mybatis-p方法,
    @PostMapping("/searchPage")
    public Result searchPage(@RequestBody QueryPageParam query) {

//        //获取entity的其他属性
        HashMap param = query.getParam();
        String name =(String)param.get("name");

        //具体的分页
        Page<Storage> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        //在此使用快捷的sql语句，不需要到xml文件中写
        LambdaQueryWrapper<Storage> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !("null".equals(name))){
            queryWrapper.like(Storage::getName, name);
        }

        IPage result = storageService.pageCC(page,queryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
    //通过no查询
    @GetMapping("/list")
    public Result list(){
        List list = storageService.list();
        return Result.success(list);
    }
}
