package com.ebor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebor.dto.SetmealDto;
import com.ebor.entity.Category;
import com.ebor.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetmealService extends IService<Setmeal> {
    /**
     * add new setmeal and meanwhile save the relationships between setmeal and dishes
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
