package com.ebor.dto;

import com.ebor.entity.Dish;
import com.ebor.entity.DishFlavor;
import com.ebor.entity.Dish;
import com.ebor.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
