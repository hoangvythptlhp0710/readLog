package readLog.fromLogFile.repository;


import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import readLog.fromLogFile.model.Log;

import java.util.Date;

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

    public boolean existsLog(Date dateTime, Integer phoneNumber, Integer companyId, Integer userId, String service, String requestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dateTime").is(dateTime));
        query.addCriteria(Criteria.where("phoneNumber").is(phoneNumber));
        query.addCriteria(Criteria.where("companyId").is(companyId));
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("service").is(service));
        query.addCriteria(Criteria.where("requestId").is(requestId));
        return mongoTemplate.exists(query, Log.class, COLLECTION);
    }



}
