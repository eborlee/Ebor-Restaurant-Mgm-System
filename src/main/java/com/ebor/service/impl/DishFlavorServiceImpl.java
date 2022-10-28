package com.ebor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.entity.Dish;
import com.ebor.entity.DishFlavor;
import com.ebor.mapper.DishFlavorMapper;
import com.ebor.mapper.DishMapper;
import com.ebor.service.DishFlavorService;
import com.ebor.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
