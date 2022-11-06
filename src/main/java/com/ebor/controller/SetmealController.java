package com.ebor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebor.common.R;
import com.ebor.dto.SetmealDto;
import com.ebor.entity.Category;
import com.ebor.entity.Setmeal;
import com.ebor.service.CategoryService;
import com.ebor.service.SetmealDishService;
import com.ebor.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @CacheEvict(value="setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(String.valueOf(setmealDto));
        setmealService.saveWithDish(setmealDto);
        return R.success("Successfully add the new setmeal");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.like(name!=null, Setmeal::getName, name);
        qw.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, qw);

        BeanUtils.copyProperties(pageInfo, dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> newRecords = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            //copy
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(newRecords);

        return R.success(dtoPage);
    }

    /**
     * Delete setmeal
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value="setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        log.info(ids.toString());
        setmealService.removeWithDish(ids);
        return R.success("Succesfully deleted the selected setmeal(s)");
    }

    @GetMapping("/list")
    @Cacheable(value="setmealCache", key = "#setmeal.categoryId +'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.eq(setmeal.getCategoryId()!=null, Setmeal::getCategoryId, setmeal.getCategoryId());
        qw.eq(setmeal.getStatus()!=null, Setmeal::getStatus, setmeal.getStatus());
        qw.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(qw);
        return R.success(list);
    }
}
