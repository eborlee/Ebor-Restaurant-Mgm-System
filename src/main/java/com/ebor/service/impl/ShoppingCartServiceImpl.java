package com.ebor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.entity.ShoppingCart;
import com.ebor.entity.User;
import com.ebor.mapper.ShoppingCartMapper;
import com.ebor.mapper.UserMapper;
import com.ebor.service.ShoppingCartService;
import com.ebor.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
