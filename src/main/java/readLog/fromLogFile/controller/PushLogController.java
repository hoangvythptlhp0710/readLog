package readLog.fromLogFile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import readLog.fromLogFile.model.UserInformation;
import readLog.fromLogFile.service.GetUserInformation;
import readLog.fromLogFile.service.PushLogToMongoDB;

import java.util.List;

@RestController
public class PushLogController {

    private final PushLogToMongoDB mongoDbService;
    @Autowired
    private GetUserInformation getUserInformation;

    public PushLogController(PushLogToMongoDB mongoDbService, GetUserInformation getUserInformation) {
        this.mongoDbService = mongoDbService;
        this.getUserInformation = getUserInformation;
    }

    @GetMapping("/pushLog")
    public ResponseEntity<?> pushLogService() throws Exception {
        mongoDbService.readLogFromFolder();
        return ResponseEntity.ok("Push log to MongoDB successfully");
    }


    @GetMapping("/getUserInformation")
    public ResponseEntity<?> getUserInformation() {
//        List<UserInformation> userInformations = getUserInformation.getAllUserInformation();
        List<Long> test = getUserInformation.test();
        return ResponseEntity.ok(
                test
        );
    }
}
