package com.ebor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.dto.DishDto;
import com.ebor.entity.Category;
import com.ebor.entity.Dish;
import com.ebor.entity.DishFlavor;
import com.ebor.mapper.CategoryMapper;
import com.ebor.mapper.DishMapper;
import com.ebor.service.CategoryService;
import com.ebor.service.DishFlavorService;
import com.ebor.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto); // after this step, dish id is created

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors =  flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());


        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    /**
     * Query dish info and corresponding flavor
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 1. query dish basic info
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 2. query flavor
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(qw);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // update dish table
        this.updateById(dishDto);

        // update flavor table
        // clear the current flavor---delete
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper();
        qw.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(qw);
        // and then add the new flavor---insert
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors =  flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());


        dishFlavorService.saveBatch(flavors);
    }

}
