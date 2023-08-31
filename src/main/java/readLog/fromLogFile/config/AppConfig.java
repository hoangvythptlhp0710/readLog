package readLog.fromLogFile.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class AppConfig {

    @Value("${mongodb.connection}")
    private String mongoDBConnection;

    @Value("${mongodb.database}")
    private String mongoDBDatabase;

    @Bean
    public MongoClient mongo() {
        final ConnectionString connectionString = new ConnectionString(mongoDBConnection);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), mongoDBDatabase);
    }



}
