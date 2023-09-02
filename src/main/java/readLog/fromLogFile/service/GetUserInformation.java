package readLog.fromLogFile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import readLog.fromLogFile.mapper.IUser;
import readLog.fromLogFile.model.UserInformation;

import java.util.List;


@Service
public class GetUserInformation {

    private final MongoService pushLogToMongoDB;

    @Autowired
    private IUser iUser;

    public GetUserInformation(MongoService pushLogToMongoDB) {
        this.pushLogToMongoDB = pushLogToMongoDB;
    }

    public List<UserInformation> getAllUserInformation() {
        return iUser.getUserInformation();
    }

    public UserInformation getUserInformationByUserId(long userId) {
        return iUser.getUserInformationByUserId(userId);
    }

}
