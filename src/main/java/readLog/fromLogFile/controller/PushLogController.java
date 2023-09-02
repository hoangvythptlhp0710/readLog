package readLog.fromLogFile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import readLog.fromLogFile.dto.response.UserInformationResponse;
import readLog.fromLogFile.model.UserInformation;
import readLog.fromLogFile.service.GetUserInformation;
import readLog.fromLogFile.service.MongoService;

import java.util.Arrays;
import java.util.List;

@RestController
public class PushLogController {

    private final MongoService mongoDbService;
    @Autowired
    private GetUserInformation getUserInformation;

    public PushLogController(MongoService mongoDbService, GetUserInformation getUserInformation) {
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
        List<UserInformation> userInformations = getUserInformation.getAllUserInformation();
        return ResponseEntity.ok(Arrays.toString(userInformations.toArray()));
    }

    @GetMapping("/getUserInformation/{userId}")
    public ResponseEntity<?> getUserInformationByUserID(@PathVariable("userId") long userId) {
        UserInformation userInformation = getUserInformation.getUserInformationByUserId(userId);
        return ResponseEntity.ok(UserInformationResponse.builder()
                .userId(userInformation.getUserId())
                .username(userInformation.getUsername())
                .phoneNumber(userInformation.getPhoneNumber())
                .companyId(userInformation.getCompanyId())
                .companyName(userInformation.getCompanyName())
                .build());

    }

    @PostMapping("/pushUserInformation")
    public ResponseEntity<?> pushUserInformationToMongoDb() throws Exception {
        mongoDbService.pushUserToMongoDb();
        return ResponseEntity.ok("Push user information to MongoDB successfully");
    }
}
