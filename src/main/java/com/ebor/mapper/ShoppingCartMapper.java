package com.ebor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebor.entity.ShoppingCart;
import com.ebor.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
