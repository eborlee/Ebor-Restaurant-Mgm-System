package com.ebor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebor.dto.DishDto;
import com.ebor.entity.Category;
import com.ebor.entity.Dish;
import org.springframework.stereotype.Service;

@Service
public interface DishService extends IService<Dish> {
    // add dish, meanwhile insert corresponding flavor
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
