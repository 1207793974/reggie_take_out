package com.maz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maz.reggie.common.R;
import com.maz.reggie.entity.Category;
import com.maz.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加一个过滤条件
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息:{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id:{}",id);
//        categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //天加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
