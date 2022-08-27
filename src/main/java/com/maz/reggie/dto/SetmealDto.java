package com.maz.reggie.dto;

import com.maz.reggie.entity.Setmeal;
import com.maz.reggie.entity.SetmealDish;
import com.maz.reggie.entity.Setmeal;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
