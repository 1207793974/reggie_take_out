package com.maz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maz.reggie.common.R;
import com.maz.reggie.dto.DishDto;
import com.maz.reggie.entity.Category;
import com.maz.reggie.entity.Dish;
import com.maz.reggie.entity.DishFlavor;
import com.maz.reggie.service.CategoryService;
import com.maz.reggie.service.DishFlavorService;
import com.maz.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto 数据传输对象
     * @return 结果
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }

    /**
     * 分页列表
     * @param page 分页
     * @param pageSize 每页数量
     * @param name 要搜索的菜品名称
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        /*List<DishDto> list = records.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());*/
        List<DishDto> list = new ArrayList<>();
        for (Dish record : records){
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record,dishDto);
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            list.add(dishDto);
        }
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);

    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    /**
     * 修改菜品
     * @param dishDto 数据传输对象
     * @return 结果
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 批量删除菜品
     * @param ids 批量的id
     * @return 删除信息
     */
    @DeleteMapping
    public R<String> delete(List<Long> ids){
        dishService.removeWithFlavor(ids);
        return R.success("删除菜品成功");
    }

    /**
     * 修改菜品售卖状态
     * @param dishState 售卖状态
     * @param ids 批量修改的id
     * @return 修改信息
     */
    @PostMapping("/status/{dishState}")
    public R<String> changeSealStatus(@PathVariable Integer dishState, @RequestParam List<Long> ids){
        for (Long id : ids){
            Dish dish = dishService.getById(id);
            dish.setStatus(dishState);
            dishService.updateById(dish);
        }
        return R.success("修改成功");
    }

/*    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);

    }*/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dtoList = list.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //sql: select * from dish_flavor where dish_i d = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dtoList);

}


}
