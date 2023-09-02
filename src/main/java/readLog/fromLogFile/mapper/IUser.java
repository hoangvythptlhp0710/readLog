package readLog.fromLogFile.mapper;

import org.apache.ibatis.annotations.*;
import readLog.fromLogFile.model.UserInformation;

import java.util.List;

@Mapper

public interface IUser {

    @Select("SELECT u.ID AS USER_ID, u.NAME AS NAME, u.PHONE AS PHONE_NUMBER, c.ID AS COMPANY_ID, c.NAME AS COMPANY_NAME\n" +
            "FROM TBL_USERS u JOIN TBL_COMPANIES c ON u.COMPANY_ID = c.ID")
    @Results({
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "username", column = "NAME"),
            @Result(property = "phoneNumber", column = "PHONE_NUMBER"),
            @Result(property = "companyId", column = "COMPANY_ID"),
            @Result(property = "companyName", column = "COMPANY_NAME")
    })
    List<UserInformation> getUserInformation();

    @Select("SELECT u.ID AS USER_ID, u.NAME AS NAME, u.PHONE AS PHONE_NUMBER, c.ID AS COMPANY_ID, c.NAME AS COMPANY_NAME\n" +
            "        FROM TBL_USERS u JOIN TBL_COMPANIES c ON u.COMPANY_ID = c.ID WHERE u.id = #{userId}")
    @Results({
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "username", column = "NAME"),
            @Result(property = "phoneNumber", column = "PHONE_NUMBER"),
            @Result(property = "companyId", column = "COMPANY_ID"),
            @Result(property = "companyName", column = "COMPANY_NAME")
    })
    UserInformation getUserInformationByUserId(long userId);

}
