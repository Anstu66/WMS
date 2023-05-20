package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2023-05-19
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService GoodsService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods Goods){
        return GoodsService.save(Goods)?Result.success():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Goods Goods) {
        return GoodsService.updateById(Goods)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return GoodsService.removeById(id)?Result.success():Result.fail();
    }
    //自定义查询 mybatis-p方法,
    @PostMapping("/searchPage")
    public Result searchPage(@RequestBody QueryPageParam query) {

//        //前段返回，后端获取entity的其他属性,用于对比
        HashMap param = query.getParam();
        String name =(String)param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");

//        String count =(String)param.get("count");
//        String remark = (String)param.get("remark");

        //具体的分页
        Page<Goods> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        //在此使用快捷的sql语句，不需要到xml文件中写
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !("null".equals(name))){
            queryWrapper.like(Goods::getName, name);
        }
        if(StringUtils.isNotBlank(goodstype) && !("null".equals(goodstype))){
            queryWrapper.eq(Goods::getGoodstype,goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !("null".equals(storage))){
            queryWrapper.eq(Goods::getStorage,storage);
        }

        IPage result = GoodsService.pageCC(page,queryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
}
