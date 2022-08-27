package com.maz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maz.reggie.dto.DishDto;
import com.maz.reggie.entity.DishFlavor;
import com.maz.reggie.mapper.DishFlavorMapper;
import com.maz.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
