package com.pragma.powerup.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.pragma.powerup.infrastructure.out.mongo.repository")
public class MongoConfig {
}
