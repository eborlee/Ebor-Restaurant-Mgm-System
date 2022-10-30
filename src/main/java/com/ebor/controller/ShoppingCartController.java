package com.ebor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ebor.common.BaseContext;
import com.ebor.common.R;
import com.ebor.entity.ShoppingCart;
import com.ebor.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {


    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * add to shopping cart
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        // set User id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();


        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,currentId);
        if(dishId!=null){
            // indicates that user adds a dish rather than a setmeal
            qw.eq(ShoppingCart::getDishId, dishId);


        }else{
            qw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cart = shoppingCartService.getOne(qw);
        // order the same dish twice, just add number
        // query the current dish or setmeal whether in shopping cart
        if(cart!=null) {
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartService.updateById(cart);
        }else{ // add the dish to the cart
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        }

        return R.success(cart);

    }

    /**
     * View the shoppingCart
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, currentId);
        qw.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(qw);
        return R.success(list);
    }

    /**
     * sub to shopping cart
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        // set User id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();



        if(dishId!=null){
            // indicates that user adds a dish rather than a setmeal
            LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
            qw.eq(ShoppingCart::getUserId,currentId);
            qw.eq(ShoppingCart::getDishId, dishId);
            ShoppingCart cart = shoppingCartService.getOne(qw);
            cart.setNumber(cart.getNumber()-1);
            if(cart.getNumber()>0){
                shoppingCartService.updateById(cart);
            }else{
                shoppingCartService.removeById(cart.getId());
            }
            return R.success(cart);
        }else{
            LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
            qw.eq(ShoppingCart::getUserId,currentId);
            qw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            ShoppingCart cart = shoppingCartService.getOne(qw);
            cart.setNumber(cart.getNumber()-1);
            if(cart.getNumber()>0){
                shoppingCartService.updateById(cart);
            }else{
                shoppingCartService.removeById(cart.getId());
            }
            return R.success(cart);
        }


    }


    /**
     * clear the cart
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, currentId);

        shoppingCartService.remove(qw);

        return R.success("Successfully cleared to cart");
    }
}
