package co.com.store.mongo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "co.com.store.mongo")
public class ReactiveMongoConfig {
}
