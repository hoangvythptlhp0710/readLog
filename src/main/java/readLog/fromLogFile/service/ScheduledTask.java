package readLog.fromLogFile.service;

import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private final MongoService mongoDbService;

    public ScheduledTask(MongoService mongoDbService) {
        this.mongoDbService = mongoDbService;
    }

//    @Scheduled(cron = "* * * * * ?")
    public void pushLogService() throws Exception {
        mongoDbService.readLogFromFolder();
    }
}
