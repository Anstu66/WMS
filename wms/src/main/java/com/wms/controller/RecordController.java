package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
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
 * @since 2023-05-19
 */
@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        Goods goods = goodsService.getById(record.getGoods());
        int oldcount = record.getCount();
        if("2".equals(record.getAction())){
            oldcount = -oldcount;
            record.setCount(oldcount);
        }
        int newcount = goods.getCount()+oldcount;
        goods.setCount(newcount);
        goodsService.updateById(goods);
        return recordService.save(record)?Result.success():Result.fail();
    }

    //自定义查询 mybatis-p方法,
    @PostMapping("/searchPage")
    public Result searchPage(@RequestBody QueryPageParam query) {

//        //前段返回，后端获取entity的其他属性,用于对比
        HashMap param = query.getParam();
        String name =(String)param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId =(String) param.get("roleId");
        String userId =(String) param.get("userId");

//        String count =(String)param.get("count");
//        String remark = (String)param.get("remark");

        //具体的分页
        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        //在此使用快捷的sql语句，不需要到xml文件中写
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        //此处可写sql语句
        queryWrapper.apply("a.goods=b.id and b.storage=c.id and b.goodsType=d.id");

        if("2".equals(roleId)){
            queryWrapper.apply("a.userId=" + userId);
        }

        if(StringUtils.isNotBlank(name) && !("null".equals(name))){
            queryWrapper.like("b.name",name); //对应Mapper中的表和数据库中的数据
        }
        if(StringUtils.isNotBlank(goodstype) && !("null".equals(goodstype))){
            queryWrapper.eq("d.id",goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !("null".equals(storage))){
            queryWrapper.eq("c.id",storage);
        }
        IPage result = recordService.pageCC(page,queryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
    //通过no查询
    @GetMapping("/list")
    public Result list(){
        List list = recordService.list();
        return Result.success(list);
    }
}
