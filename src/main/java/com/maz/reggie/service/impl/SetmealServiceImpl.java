package com.maz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maz.reggie.common.CustomException;
import com.maz.reggie.common.R;
import com.maz.reggie.dto.SetmealDto;
import com.maz.reggie.entity.Dish;
import com.maz.reggie.entity.Setmeal;
import com.maz.reggie.entity.SetmealDish;
import com.maz.reggie.mapper.SetmealMapper;
import com.maz.reggie.service.DishService;
import com.maz.reggie.service.SetmealDishService;
import com.maz.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

//    @Autowired
//    private SetmealService setmealService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息 操作Setmeal
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品之间的关系， 操作SetmealDish
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除菜品和套餐的关系数据
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish( List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1;
        //查询套餐状态
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = (int)this.count(queryWrapper);
        //如果不能删除，抛出异常
        if(count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中数据
        this.removeByIds(ids);
        //删除关系表中数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }

    @Override
    public void changeSealStatus(Integer SetmealStatus, List<Long> ids) {

        /*if(SetmealStatus == 1){
            for (Long id : ids){
                Setmeal setmeal = this.getById(id);
                setmeal.setStatus(SetmealStatus);
                this.updateById(setmeal);
            }
        }else if(SetmealStatus == 0){
            for (Long id : ids){
                Setmeal setmeal = this.getById(id);
                SetmealDto setmealDto = new SetmealDto();
                BeanUtils.copyProperties(setmeal,setmealDto);
                List<SetmealDish> dishes = setmealDto.getSetmealDishes();
                List<Long> dishIds = dishes.stream().map((item) ->{
                    Long dishId = item.getDishId();
                    return dishId;
                }).collect(Collectors.toList());

                for (Long dishId : dishIds){
                    Dish dish = dishService.getById(dishId);
                    Integer dishStatus = dish.getStatus();
                    if(dishStatus == 0){
                        dishService.changeSealStatus(dishStatus,dishIds);
                    }

                }

            }
        }*/

    }
}
