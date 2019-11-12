package org.plsk.web.config

import com.mongodb.reactivestreams.client.MongoDatabase
import org.plsk.core.command.CommandHandler
import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter
import org.plsk.core.id.IdGen
import org.plsk.mongo.user.MongoUserRepository
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler
import org.plsk.user.dao.UserRepository
import org.plsk.user.tmpUser.CreateTmpUser
import org.plsk.user.tmpUser.CreateTmpUserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class UserConfig {

  @Bean
  fun provideUserRepository(db: MongoDatabase): UserRepository = MongoUserRepository(db)

  @Bean
  fun provideUserQueryHandler(userReader: DataReader<User, String>): UserQueryHandler = UserQueryHandler(userReader)


  @Bean
  fun provideCreateTmpUserHandler(dataWriter: DataWriter<User, String>,
                                  uuidGen: IdGen<UUID>): CommandHandler<CreateTmpUser, User> = CreateTmpUserHandler(dataWriter, uuidGen)
}
