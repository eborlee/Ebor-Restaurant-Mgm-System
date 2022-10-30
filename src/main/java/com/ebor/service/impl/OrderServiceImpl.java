package com.ebor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebor.common.BaseContext;
import com.ebor.common.CustomException;
import com.ebor.entity.*;
import com.ebor.mapper.OrderMapper;
import com.ebor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public void submit(Orders orders) {
        // get the use id
        Long currentId = BaseContext.getCurrentId();

        // query the user's cart
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, currentId);
        List<ShoppingCart> carts = shoppingCartService.list(qw);

        if(carts==null || carts.size()==0){
            throw new CustomException("Your shopping cart is null");
        }

        // query user's address
        User user = userService.getById(currentId);
        Long addressBookId = orders.getAddressBookId();
        AddressBook address = addressBookService.getById(addressBookId);

        if(address==null){
            throw new CustomException("Address is not valid");
        }

        long orderId = IdWorker.getId(); // generate an order id


        AtomicInteger amount = new AtomicInteger(0); // thread safe

        List<OrderDetail> orderDetails = carts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber()); // quantity of dishes
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());

            // +=
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        // insert data to order table
        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2); // waiting for deliverey
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(currentId);
        orders.setUserName(user.getName());
        orders.setConsignee(address.getConsignee());
        orders.setPhone(address.getPhone());
        orders.setAddress((address.getProvinceName()==null?"":address.getProvinceName())+
                        (address.getCityName()==null?"":address.getCityName())+
                        (address.getDistrictName()==null?"":address.getDistrictName())+
                        (address.getDetail()==null?"":address.getDetail()));

        this.save(orders);

        // insert data to orderDetail table
        orderDetailService.saveBatch(orderDetails);
        // clear the cart
        shoppingCartService.remove(qw);
    }
}
