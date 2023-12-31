package readLog.fromLogFile.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import readLog.fromLogFile.dto.response.CountByDay;
import readLog.fromLogFile.mapper.IUser;
import readLog.fromLogFile.model.Log;
import readLog.fromLogFile.model.UserInformation;
import readLog.fromLogFile.repository.MongoDBRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MongoService {

    @Autowired
    private IUser iUser;
    private final MongoDBRepository mongoDBRepository;

    public MongoService(MongoDBRepository mongoDBRepository) {
        this.mongoDBRepository = mongoDBRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);
    @Value("${file.path}")
    private String filePath;

    public void readLogFromFolder() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\|");
                if (split.length < 8) {
                    logger.info("Invalid log, skipping...");
                    continue;
                }
                Log log = new Log();
                String date = split[1].trim();
                String time = split[2].trim();
                String phoneNumber = split[3].trim();
                String companyId = split[4].trim();
                String userId = split[5].trim();
                String service = split[6].trim();
                String requestId = split[7].trim();


                log.setDateTime(castStringToDate(date, time));
                if (phoneNumber.trim().equals("null")) {
                    log.setPhoneNumber(null);
                } else {
                    log.setPhoneNumber(phoneNumber.trim());
                }
                if (companyId.trim().equals("null")) {
                    log.setCompanyId(null);
                } else {
                    log.setCompanyId(Long.parseLong(companyId.trim()));
                }
                if (userId.trim().equals("null")) {
                    log.setUserId(null);
                } else {
                    log.setUserId(Long.parseLong(userId.trim()));

                }
                log.setService(service.trim());
                log.setRequestId(requestId.trim());


                // Handle other columns similarly
                if (!logAlreadyExists(log)) {
                    mongoDBRepository.saveAll(log);
                    logger.info("Pushed log to MongoDB");
                } else {
                    logger.info("Log already exists in MongoDB, skipping...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // handle date and string time
    private Date castStringToDate(String date, String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = dateFormat.parse(date + " " + time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.add(Calendar.HOUR_OF_DAY, 7); // Add 7 hours

        return calendar.getTime();
    }


    public void pushUserToMongoDb() {
        List<UserInformation> userInformations = iUser.getUserInformation();
        for (UserInformation userInformation: userInformations) {
            if (!userAlreadyExists(userInformation)) {
                mongoDBRepository.saveUserInformation(userInformation);
                logger.info("Pushed user information to MongoDB");
            }
            logger.info("User already exists in DB");
        }
    }


    // check if log exists
    private boolean logAlreadyExists(Log log) {
        return mongoDBRepository.existsLog(
                log.getDateTime(),
                log.getPhoneNumber(),
                log.getCompanyId(),
                log.getUserId(),
                log.getService(),
                log.getRequestId()
        );
    }

    private boolean userAlreadyExists(UserInformation userInformation) {
        return mongoDBRepository.existsUserInformation(
                userInformation.getUserId(),
                userInformation.getUsername(),
                userInformation.getPhoneNumber(),
                userInformation.getCompanyId(),
                userInformation.getCompanyName()
        );
    }

    public List<CountByDay> countByDay() {
        return mongoDBRepository.countTheLogin();
    }


}
