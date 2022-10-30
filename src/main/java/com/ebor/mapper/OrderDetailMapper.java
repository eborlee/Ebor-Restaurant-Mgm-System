package com.ebor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebor.entity.OrderDetail;
import com.ebor.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
