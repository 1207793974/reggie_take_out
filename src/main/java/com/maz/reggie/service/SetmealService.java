package com.maz.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maz.reggie.dto.SetmealDto;
import com.maz.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，并保存套餐和菜品之间的关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
    public void removeWithDish(List<Long> ids);
    public void changeSealStatus(Integer SetmealStatus, List<Long> ids);
}
