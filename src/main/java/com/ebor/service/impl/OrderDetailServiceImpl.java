package com.ebor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.entity.OrderDetail;
import com.ebor.entity.Orders;
import com.ebor.mapper.OrderDetailMapper;
import com.ebor.mapper.OrderMapper;
import com.ebor.service.OrderDetailService;
import com.ebor.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
