package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("amdinShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置营业状态
     */
    @PutMapping("/{status}")
    @ApiOperation(value = "设置营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("修改店铺状态：{}", status);
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }
    /**
     * 查询营业状态
     */
    @GetMapping("/status")
    @ApiOperation(value = "查询营业状态")
    public Result<Integer> getStatus() {
        log.info("查询营业状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("营业状态：{}", status);
        return Result.success(status);
    }
}
