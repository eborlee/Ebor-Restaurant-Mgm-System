package com.ebor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.entity.Category;
import com.ebor.entity.Dish;
import com.ebor.mapper.CategoryMapper;
import com.ebor.mapper.DishMapper;
import com.ebor.service.CategoryService;
import com.ebor.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
