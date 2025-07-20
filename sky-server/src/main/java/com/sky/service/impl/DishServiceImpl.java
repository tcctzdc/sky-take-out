package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDTO
     */
        public void saveWithFlavor(DishDTO dishDTO) {
            log.info("新增菜品：{}", dishDTO);
            // 插入一条菜品
            Dish dish = new Dish();
            BeanUtils.copyProperties(dishDTO, dish);
            dishMapper.insert(dish);
            // 获取菜品的id
            Long dishId = dish.getId();
            // 插入多个口味
            List<DishFlavor> flavors = dishDTO.getFlavors();
            if (flavors != null && flavors.size() > 0) {
                flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));

                // 插入n条数据
                dishFlavorMapper.insertBatch(flavors);
            }
        }


}