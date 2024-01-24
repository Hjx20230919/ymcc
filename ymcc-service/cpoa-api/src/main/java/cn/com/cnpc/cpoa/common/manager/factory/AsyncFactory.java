package cn.com.cnpc.cpoa.common.manager.factory;

import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.service.LogService;
import cn.com.cnpc.cpoa.utils.SpringUtils;

import java.util.TimerTask;

/**
 * @Author: 17742856263
 * @Date: 2019/3/3 15:57
 * @Description: 异步工厂
 */
public class AsyncFactory {

    /**
     * 操作日志记录
     *
     * @param logDto 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysLogDto logDto)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                SpringUtils.getBean(LogService.class).save(logDto);
            }
        };
    }

}
