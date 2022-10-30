package com.ebor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebor.entity.Orders;
import com.ebor.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
