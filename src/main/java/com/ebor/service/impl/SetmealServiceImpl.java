package com.ebor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.entity.Category;
import com.ebor.entity.Setmeal;
import com.ebor.mapper.CategoryMapper;
import com.ebor.mapper.SetmealMapper;
import com.ebor.service.CategoryService;
import com.ebor.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
