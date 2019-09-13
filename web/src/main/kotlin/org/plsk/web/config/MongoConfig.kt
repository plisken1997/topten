package org.plsk.web.config

import com.mongodb.reactivestreams.client.MongoDatabase
import org.litote.kmongo.reactivestreams.KMongo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoConfig {

  @Bean
  fun provideMongoDB(): MongoDatabase {
    val database = "topten"
    val mongoClient = KMongo.createClient("mongodb://localhost")
    return mongoClient.getDatabase(database)
  }
}