package com.ebor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebor.entity.Category;
import com.ebor.entity.Dish;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
