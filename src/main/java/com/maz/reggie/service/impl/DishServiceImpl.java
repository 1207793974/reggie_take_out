package com.maz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maz.reggie.common.CustomException;
import com.maz.reggie.dto.DishDto;
import com.maz.reggie.dto.SetmealDto;
import com.maz.reggie.entity.Dish;
import com.maz.reggie.entity.DishFlavor;
import com.maz.reggie.entity.Setmeal;
import com.maz.reggie.entity.SetmealDish;
import com.maz.reggie.mapper.DishMapper;
import com.maz.reggie.service.DishFlavorService;
import com.maz.reggie.service.DishService;
import com.maz.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

//    @Autowired
//    private SetmealService setmealService;
    /**
     * 新增菜品并保存对应的口味数据
     * @param dishDto 数据传输对象
     */

    public void saveWithFlavor(DishDto dishDto){
        this.save(dishDto);
        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());
        //保存菜品口味到菜品口味表
        dishFlavorService.saveBatch(flavors);


    }

    /**
     * 根据Id查询菜品基本信息和对应的口味信息
     * @param id
     * @return dto
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品基本信息
        this.updateById(dishDto);
        //清理口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //重新添加口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {
        //判断菜品是否在售
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);
        int count = (int)this.count(queryWrapper);

        //如果在售，抛出异常不能删除
        if(count > 0){
            throw new CustomException("在售，无法删除");
        }
        //判断是否存在于套餐中
        /*LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getDishId,ids);
        setmealService.*/
        //如果不在售删除
        this.removeByIds(ids);

        //删除口味关系
        LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flavorLambdaQueryWrapper.in(DishFlavor::getId,ids);
        dishFlavorService.remove(flavorLambdaQueryWrapper);
    }

    @Transactional
    @Override
    public void changeSealStatus(Integer dishStatus, List<Long> ids){
//        for (Long id : ids){
//            Setmeal setmeal = setmealService.getById(id);
//            setmeal.setStatus(dishStatus);
//            setmealService.updateById(setmeal);
//        }



        /*LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //查询套餐状态
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();*/

        //修改菜品状态
        //修改套餐状态
    }


}
