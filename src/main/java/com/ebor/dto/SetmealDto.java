package com.ebor.dto;

import com.ebor.entity.Setmeal;
import com.ebor.entity.SetmealDish;
import com.ebor.entity.Setmeal;
import com.ebor.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
