package readLog.fromLogFile.repository;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import readLog.fromLogFile.dto.response.CountByDay;
import readLog.fromLogFile.model.Log;
import readLog.fromLogFile.model.UserInformation;

import java.util.Date;
import java.util.List;

@Repository
@AllArgsConstructor
public class MongoDBRepository {

    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION = "logs-by-company";

    private static final String INFORMATION = "user-information";


    public void saveAll(Log log) {
        Query query = new Query();
        query.addCriteria(Criteria.where("companyId").is(log.getCompanyId()));
        mongoTemplate.save(log, COLLECTION);
    }

    public boolean existsLog(Date dateTime, String phoneNumber, Long companyId, Long userId, String service, String requestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dateTime").is(dateTime));
        query.addCriteria(Criteria.where("phoneNumber").is(phoneNumber));
        query.addCriteria(Criteria.where("companyId").is(companyId));
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("service").is(service));
        query.addCriteria(Criteria.where("requestId").is(requestId));
        return mongoTemplate.exists(query, Log.class, COLLECTION);
    }


    public void saveUserInformation(UserInformation userInformation) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userInformation.getUserId()));
        mongoTemplate.save(userInformation, INFORMATION);
    }

    public boolean existsUserInformation(Long userId, String username, String phoneNumber, Long companyId, String companyName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("phoneNumber").is(phoneNumber));
        query.addCriteria(Criteria.where("companyId").is(companyId));
        query.addCriteria(Criteria.where("companyName").is(companyName));
        return mongoTemplate.exists(query, UserInformation.class, INFORMATION);
    }


//    public List<CountByDay> countTheLogin() {
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.lookup(INFORMATION, "userId", "userId", "userInfo"),
//                Aggregation.unwind("$userInfo"),
//                Aggregation.match(Criteria.where("service").is("LOGIN")),
//                Aggregation.group("userId", "companyId", "companyName")
//                        .count().as("count")
//                        .first("dateTime").as("date")
//                        .first("userId").as("userId")
//                        .first("userInfo.username").as("userName")
//                        .first("phoneNumber").as("phoneNumber")
//                        .first("companyId").as("companyId")
//                        .first("userInfo.companyName").as("companyName"),
//                Aggregation.project("date", "userId", "userName", "phoneNumber", "companyId", "companyName", "count")
//        );
//
//        AggregationResults<CountByDay> result = mongoTemplate.aggregate(aggregation, COLLECTION, CountByDay.class);
//        return result.getMappedResults();
//
//    }

    public List<CountByDay> countTheLogin2() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup(INFORMATION, "userId", "userId", "userInfo"),
                Aggregation.unwind("$userInfo"),
                Aggregation.match(
                        new Criteria().orOperator(
                                Criteria.where("field").is("LOGIN"),
                                Criteria.where("field").is("LOGIN_BIOMETRIC")
                        ).andOperator(
                                Criteria.where("phoneNumber").is(null),
                                Criteria.where("userId").is(null),
                                Criteria.where("companyId").is(null)
                                // Add other fields that need to be null here
                        )
                ),
                Aggregation.group("userId", "companyId", "companyName")
                        .count().as("count")
                        .first("dateTime").as("date")
                        .first("userId").as("userId")
                        .first("userInfo.username").as("userName")
                        .first("phoneNumber").as("phoneNumber")
                        .first("companyId").as("companyId")
                        .first("userInfo.companyName").as("companyName"),
                Aggregation.project("date", "userId", "userName", "phoneNumber", "companyId", "companyName", "count")
        );
        AggregationResults<CountByDay> result = mongoTemplate.aggregate(aggregation, COLLECTION, CountByDay.class);
        return result.getMappedResults();
    }

    public List<Log> getAllLogInformation() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "dateTime"));
        return mongoTemplate.find(query, Log.class, COLLECTION);
    }

    public List<UserInformation> getAllUserInformation() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "userId"));
        return mongoTemplate.find(query, UserInformation.class, INFORMATION);
    }

    public List<CountByDay> countTheLogin() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("user-informations", "userId", "userId", "userInfo"),
                Aggregation.match(Criteria.where("userInfo").ne(null)),
                Aggregation.project("userInfo.username", "userInfo.companyName", "dateTime", "userInfo.phoneNumber", "userInfo.userId", "userInfo.companyId", "service"),
                Aggregation.match(Criteria.where("service").in("LOGIN", "LOGIN_BIOMETRIC")),
                Aggregation.group("userInfo.userId", "userInfo.username", "userInfo.companyId", "userInfo.companyName", "userInfo.phoneNumber", "dateTime")
                        .count().as("count")
        );

        AggregationResults<CountByDay> results = mongoTemplate.aggregate(aggregation, "logs-by-company", CountByDay.class);
        return results.getMappedResults();
    }
}
