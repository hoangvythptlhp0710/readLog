package readLog.fromLogFile.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import readLog.fromLogFile.dto.response.CountByDay;
import readLog.fromLogFile.model.Log;
import readLog.fromLogFile.model.UserInformation;
import readLog.fromLogFile.repository.MongoDBRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HandlingDataService {

    private final MongoDBRepository mongoDBRepository;

    private static Logger logger = LoggerFactory.getLogger(HandlingDataService.class);
    public HandlingDataService(MongoDBRepository mongoDBRepository) {
        this.mongoDBRepository = mongoDBRepository;
    }

    public ResponseEntity<?> handlingData() {
        List<Log> logs = mongoDBRepository.getAllLogInformation();
        logger.info(String.valueOf(logs.size()));

        for (Log log: logs) {
//            logger.info(log.getUserId() + " " +log.getService());
        }
        List<UserInformation> userInformations = mongoDBRepository.getAllUserInformation();

        logger.info(String.valueOf(userInformations.size()));

//        for (UserInformation userInformation: userInformations) {
//            logger.info(userInformation.getUserId() + " " + userInformation.getUsername()+ " " + userInformation.getCompanyId() + " " + userInformation.getPhoneNumber());
//        }

        // Step 1: Create a map for UserInformation lookup by userId and companyId
        Map<String, UserInformation> userInformationMap = userInformations.stream()
                .collect(Collectors.toMap(
                        user -> user.getUserId() + "_" + user.getCompanyId(),
                        user -> user
                ));

        // Step 2: Count the occurrences of "LOGIN" service for each user
        Map<String, Integer> loginServiceCounts = new HashMap<>();
        Map<String, Boolean> loginBiometricExists = new HashMap<>(); // To track if LOGIN_BIOMETRIC exists for a user

        for (Log log : logs) {
            String key = log.getUserId() + "_" + log.getCompanyId() + "_" + log.getPhoneNumber();

            if (log.getService().equals("LOGIN")) {
                loginServiceCounts.merge(key, 1, Integer::sum);
            } else if (log.getService().equals("LOGIN_BIOMETRIC")) {
                loginBiometricExists.put(key, true); // Mark that LOGIN_BIOMETRIC exists for this user
                loginServiceCounts.merge(key, 1, Integer::sum);
            }
        }

        // Step 3: Create a list of CountByDay objects based on the information from steps 1 and 2
        List<CountByDay> result = loginServiceCounts.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("_");
                    String userId = parts[0];
                    String companyId = parts[1];
                    String phoneNumber = parts[2];
                    UserInformation userInformation = userInformationMap.get(userId + "_" + companyId);

                    // Check if userId, companyId, and phoneNumber are not null
                    if (!Objects.equals(userId, "null") && !Objects.equals(companyId, "null") && !Objects.equals(phoneNumber, "null") && userInformation != null) {
                        Integer count = entry.getValue();
                        return new CountByDay(
                                new Date(), // You can set the date as needed
                                Long.parseLong(userId),
                                userInformation.getUsername(),
                                userInformation.getPhoneNumber(),
                                Long.parseLong(companyId),
                                userInformation.getCompanyName(),
                                count
                        );
                    }
                    return null; // Skip this entry if any of the fields are null
                })
                .filter(Objects::nonNull) // Remove entries where any field was null
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}

