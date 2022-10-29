package com.ebor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.entity.SetmealDish;
import com.ebor.mapper.SetmealDishMapper;
import com.ebor.service.SetmealDishService;
import com.ebor.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
