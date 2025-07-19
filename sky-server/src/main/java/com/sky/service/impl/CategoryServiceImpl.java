package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        // 设置状态默认值
        category.setStatus(StatusConstant.DISABLE);
        // 设置创建时间、更新时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        // 设置创建人、更新人
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(category);
    }

    /**
     *
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> categories = categoryMapper.pageQuery(categoryPageQueryDTO);

        PageResult pageResult = new PageResult();
        // 设置总记录数
        pageResult.setTotal(categories.getTotal());
        // 设置当前页数据
        pageResult.setRecords(categories.getResult());

        return pageResult;
    }
    /**
     * 删除分类
     * @param id
     */
    public void deleteById(Long id) {
        // 判断当前分类是否关联了菜品，如果已经关联，则无法删除
        int dishCount = dishMapper.countByCategoryId(id);
        if (dishCount > 0) {
            // 当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 判断当前分类是否关联了套餐，如果已经关联，则无法删除
        int setmealCount = setmealMapper.countByCategoryId(id);
        if (setmealCount > 0) {
            // 当前分类下有套餐，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        // 正常删除分类
        categoryMapper.deleteById(id);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO){
        Category category = new Category();
        // 对象属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);
        // 设置更新时间、更新人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        // 执行更新
        categoryMapper.update(category);
    }
    /**
     * 启用、禁用分类
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     * @return
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
