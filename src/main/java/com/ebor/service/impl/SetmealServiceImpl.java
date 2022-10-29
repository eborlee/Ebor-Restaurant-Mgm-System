package com.ebor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.common.CustomException;
import com.ebor.common.R;
import com.ebor.dto.SetmealDto;
import com.ebor.entity.Category;
import com.ebor.entity.Setmeal;
import com.ebor.entity.SetmealDish;
import com.ebor.mapper.CategoryMapper;
import com.ebor.mapper.SetmealMapper;
import com.ebor.service.CategoryService;
import com.ebor.service.SetmealDishService;
import com.ebor.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * add new setmeal and meanwhile save the relationships between setmeal and dishes
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // insert to setmeal
        this.save(setmealDto);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        setmealDishList.stream().map((item)->{
            item.setSetmealId(setmealDto.getId()); // this.save之后框架会自动更新dto中套餐id的值
            return item;
        }).collect(Collectors.toList());

        // insert to setmealDish
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // firstly query status to check whether it could be deleted
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.in(Setmeal::getId, ids);
        qw.eq(Setmeal::getStatus, 1);
        int count = this.count(qw);
        // if cannot, throw error
        if(count>0){
            throw new CustomException("The Setmeal is being sold, you cannot delete it");
        }

        // delete setmeal table
        this.removeByIds(ids);

        // delete setmealDish table
        // 此处不能用套餐id作为setmealDish表的主键去调setmealDishService的removeById
        LambdaQueryWrapper<SetmealDish> qw2 = new LambdaQueryWrapper<>();
        qw2.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(qw2);




    }
}
