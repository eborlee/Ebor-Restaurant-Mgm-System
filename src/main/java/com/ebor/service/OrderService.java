package com.ebor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebor.entity.Orders;
import com.ebor.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface OrderService extends IService<Orders> {


    void submit(Orders orders);
}
