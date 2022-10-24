package com.ebor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.common.CustomException;
import com.ebor.entity.Category;
import com.ebor.entity.Dish;
import com.ebor.entity.Setmeal;
import com.ebor.mapper.CategoryMapper;
import com.ebor.service.CategoryService;
import com.ebor.service.DishService;
import com.ebor.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * check whether category is linked to certain dishes or setmeals, then delete category by id
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        // if the category is linked to dishes, throw an exception
        if(count1>0){
            throw new CustomException("This category is linked to certain dishes, you can't delete it.");
        }

        LambdaQueryWrapper<Setmeal> setLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setLambdaQueryWrapper);

        if(count2>0){
            throw new CustomException("This category is linked to certain setmeals, you can't delete it.");
        }

        super.removeById(id);
    }
}
