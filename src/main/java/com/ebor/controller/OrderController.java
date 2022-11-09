package com.ebor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebor.common.R;
import com.ebor.dto.DishDto;
import com.ebor.dto.OrdersDto;
import com.ebor.entity.Category;
import com.ebor.entity.Dish;
import com.ebor.entity.OrderDetail;
import com.ebor.entity.Orders;
import com.ebor.service.OrderDetailService;
import com.ebor.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * user submits the order
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        orderService.submit(orders);

        return R.success("succesfully submitted the order!");
    }


//    @GetMapping("/userPage")
//    public R<Page> page(int page, int pageSize){
//        log.info(page+"======"+pageSize);
//        Page<Orders> pageInfo = new Page<>(page, pageSize);
//        // condition constructor
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//        // add order condition according to Sort
//        queryWrapper.orderByDesc(Orders::getOrderTime);
//        // do the page query
//        orderService.page(pageInfo, queryWrapper);
//        return R.success(pageInfo);
//    }

    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        // create page instances
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        // set query conditions for pageInfo
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders::getOrderTime);

        // call the page method of dishService to query
        orderService.page(pageInfo, queryWrapper);

        // copy the properties of pageinfo to dishDtoPage except for the "records"
        BeanUtils.copyProperties(pageInfo, ordersDtoPage,"records");

        // deal with the records: convert the item in it to be DishDto class and query & set categoryName
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> list = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();

            // copy the properties in Dish object to DishDto object
            BeanUtils.copyProperties(item, ordersDto);

            Long orderId = Long.valueOf(item.getNumber());

            LambdaQueryWrapper<OrderDetail> qw = new LambdaQueryWrapper<>();
            qw.eq(OrderDetail::getOrderId, orderId);

            List<OrderDetail> orderDetails = orderDetailService.list(qw);


            if(orderDetails!=null){
                // set the categoryName property of DishDto object
                ordersDto.setOrderDetails(orderDetails);
            }
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);
    }



}
