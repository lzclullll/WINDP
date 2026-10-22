package com.energy.platform.task;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.energy.platform.entity.Alarm;
import com.energy.platform.entity.PowerReading;
import com.energy.platform.service.AlarmService;
import com.energy.platform.service.PowerReadingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class AlarmCheckTask {
    private final PowerReadingService powerService; private final AlarmService alarmService;
    public AlarmCheckTask(PowerReadingService powerService, AlarmService alarmService) { this.powerService = powerService; this.alarmService = alarmService; }
    @Scheduled(fixedDelay = 180000)
    public void check() {
        PowerReading latest = powerService.getOne(new LambdaQueryWrapper<PowerReading>().orderByDesc(PowerReading::getRecordTime).last("LIMIT 1"), false);
        if (latest != null && latest.getPowerKw() != null && latest.getPowerKw().compareTo(BigDecimal.TEN) < 0) {
            Alarm alarm = new Alarm(); alarm.setStationId(latest.getStationId()); alarm.setAlarmType("POWER_LOW"); alarm.setAlarmLevel("WARN"); alarm.setTitle("功率低值告警"); alarm.setContent("实时功率低于阈值"); alarm.setStatus("OPEN"); alarmService.save(alarm);
        }
    }
}
