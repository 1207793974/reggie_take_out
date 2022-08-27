package com.maz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maz.reggie.common.CustomException;
import com.maz.reggie.entity.Category;
import com.maz.reggie.entity.Dish;
import com.maz.reggie.entity.Setmeal;
import com.maz.reggie.mapper.CategoryMapper;
import com.maz.reggie.service.CategoryService;
import com.maz.reggie.service.DishService;
import com.maz.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //查询当前分类是否关联了菜品和套餐
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = (int) dishService.count(dishLambdaQueryWrapper);

        if (count1 > 0){
            //抛出业务异常
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = (int) setmealService.count();
        if(count2 > 0){
            //抛出业务异常
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }
        super.removeById(id);


    }
}
