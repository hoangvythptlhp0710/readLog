package readLog.fromLogFile.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private final PushLogToMongoDB mongoDbService;

    public ScheduledTask(PushLogToMongoDB mongoDbService) {
        this.mongoDbService = mongoDbService;
    }

//    @Scheduled(cron = "* * * * * ?")
    public void pushLogService() throws Exception {
        mongoDbService.readLogFromFolder();
    }
}
