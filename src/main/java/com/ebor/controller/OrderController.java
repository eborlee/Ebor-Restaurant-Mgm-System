package com.ebor.controller;


import com.ebor.common.R;
import com.ebor.entity.Orders;
import com.ebor.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

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
}
