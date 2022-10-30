package com.ebor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebor.common.R;
import com.ebor.dto.DishDto;
import com.ebor.entity.Category;
import com.ebor.entity.Dish;
import com.ebor.entity.DishFlavor;
import com.ebor.service.CategoryService;
import com.ebor.service.DishFlavorService;
import com.ebor.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Add new dish
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(String.valueOf(dishDto));
        dishService.saveWithFlavor(dishDto);
        return R.success("Successfully added the new dish!");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        // create page instances
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // set query conditions for pageInfo
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // call the page method of dishService to query
        dishService.page(pageInfo, queryWrapper);

        // copy the properties of pageinfo to dishDtoPage except for the "records"
        BeanUtils.copyProperties(pageInfo, dishDtoPage,"records");

        // deal with the records: convert the item in it to be DishDto class and query & set categoryName
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();

            // copy the properties in Dish object to DishDto object
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                // set the categoryName property of DishDto object
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * Query dish info and corresponding flavor 用于回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto =  dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }


    /**
     * Update dish information
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(String.valueOf(dishDto));
        dishService.updateWithFlavor(dishDto);
        return R.success("Successfully updates the new dish!");
    }

    /**
     * Delete by Ids
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String ids){
        List<String> idsList = new ArrayList(List.of(ids.split(",")));
        List<Long> idsListLong = idsList.stream().map((item)->{
            return Long.valueOf(item);
        }).collect(Collectors.toList());

        if(idsListLong.size()==1){
            dishService.removeById(idsListLong.get(0));
        }else{
            dishService.removeByIds(idsListLong);
        }


        return R.success("Successfully removes the dish!");
    }


    /**
     * Update the dish status
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status, @RequestParam List<Long> ids){
        log.info(String.valueOf(status));
        log.info(String.valueOf(ids));
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.in(ids!=null, Dish::getId, ids);
        List<Dish> dishes = dishService.list(qw);

        for(Dish dish:dishes){
            if(dish!=null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }

        return R.success("Successfully set to be active");
    }

    /**
     * Query dishes by conditions
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
//        qw.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
//        qw.eq(Dish::getStatus,1); // only query the dishes with status === 1;
//        qw.like(dish.getName()!=null,Dish::getName, dish.getName());
//        qw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime );
//
//        List<Dish> list = dishService.list(qw);
//
//
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
        qw.eq(Dish::getStatus,1); // only query the dishes with status === 1;
        qw.like(dish.getName()!=null,Dish::getName, dish.getName());
        qw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime );

        List<Dish> list = dishService.list(qw);
        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();

            // copy the properties in Dish object to DishDto object
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                // set the categoryName property of DishDto object
                dishDto.setCategoryName(categoryName);
            }
            // 当前菜品id，用其去查菜品口味
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> qwFlavor = new LambdaQueryWrapper<>();
            qwFlavor.eq(DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(qwFlavor);

            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());



        return R.success(dishDtoList);
    }


}
