package com.tony.health_jobs.task;

import com.tony.health_common.constant.RedisConstant;
import com.tony.health_common.untils.QiniuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@Component
public class ClearPicTask {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 34 10 * * ?")
    public void taskTime() {
        Set<String> difference = stringRedisTemplate.opsForSet()
                .difference(RedisConstant.SETMEAL_PIC_RESOURCE, RedisConstant.SETMEAL_PIC_DB_RESOURCE);

        for (String name : difference) {
            QiniuUtils.deleteFileFromQiniu(name);
            stringRedisTemplate.opsForSet().remove(RedisConstant.SETMEAL_PIC_RESOURCE, name);
            log.info("自定义任务ClearPicTask,多余垃圾图片已删除： " + name);
        }

    }
}
