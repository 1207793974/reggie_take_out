package com.maz.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maz.reggie.dto.DishDto;
import com.maz.reggie.entity.Dish;
import com.maz.reggie.entity.DishFlavor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
    public void removeWithFlavor(List<Long> ids);
    public void changeSealStatus(Integer dishStatus, List<Long> ids);
}
