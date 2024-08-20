package com.universe.backend.job;


import com.universe.backend.database.mapper.EngineMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ScheduleJob {
    @Resource
    private EngineMapper engineMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateOfflineEngine(){
        Long minLastHeartbeatTime = System.currentTimeMillis()-3*60*1000;
        engineMapper.updateOfflineEngine(minLastHeartbeatTime);
    }
}
