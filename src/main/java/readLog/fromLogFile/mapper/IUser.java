package readLog.fromLogFile.mapper;

import org.apache.ibatis.annotations.Mapper;
import readLog.fromLogFile.model.UserInformation;

import java.util.List;

@Mapper
public interface IUser {

    List<UserInformation> getUserInformation();

    List<Long> test();
}
