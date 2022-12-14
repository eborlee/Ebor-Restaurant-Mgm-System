package com.ebor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebor.common.R;
import com.ebor.entity.Category;
import com.ebor.entity.Employee;
import com.ebor.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * Create a new category
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("Successfully add a new category");
    }

    /**
     * page query
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Integer type){

        // page constructor
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // condition constructor
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // add order condition according to Sort
        queryWrapper.eq(null!=type, Category::getType,type);
        queryWrapper.orderByAsc(Category::getSort);
        // do the page query
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long id){

        categoryService.remove(id);
        return R.success("Successfully removed Category information");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("Successfully updated the category information!");
    }

    /**
     * To get the category list by condition
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(category.getType()!=null, Category::getType, category.getType()); // to query dish cate or setmeal cate
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
