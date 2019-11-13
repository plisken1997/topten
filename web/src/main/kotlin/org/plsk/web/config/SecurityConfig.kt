package org.plsk.web.config

import com.mongodb.reactivestreams.client.MongoDatabase
import org.plsk.core.clock.Clock
import org.plsk.core.command.CommandHandler
import org.plsk.core.dao.DataReader
import org.plsk.mongo.security.DocumentAccessTokenRepository
import org.plsk.security.Authentication
import org.plsk.security.AuthenticationFailure
import org.plsk.security.WithUnknownUserAuthentication
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.security.accessToken.AccessTokenRepository
import org.plsk.security.accessToken.JwtsTokenProvider
import org.plsk.security.session.SessionProvider
import org.plsk.security.session.WithTmpUserSessionProvider
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler
import org.plsk.user.tmpUser.CreateTmpUser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityConfig {

  private val appId: String = "topten"

  @Bean
  fun provideAccessTokenProvider(accessTokenRepository: AccessTokenRepository, clock: Clock, userReader: DataReader<User, String>): AccessTokenProvider =
      JwtsTokenProvider(accessTokenRepository, userReader, clock, appId)

  @Bean
  fun provideAccessTokenRepository(db: MongoDatabase): AccessTokenRepository = DocumentAccessTokenRepository(db)

  @Bean
  fun provideSessionProvider(
      userQueryHandler: UserQueryHandler,
      accessTokenProvider: AccessTokenProvider
  ): SessionProvider<AuthenticationFailure> = WithTmpUserSessionProvider(userQueryHandler, accessTokenProvider)

  @Bean
  fun provideAuthentication(
      createUser: CommandHandler<CreateTmpUser, User>,
      sessionProvider: SessionProvider<AuthenticationFailure>
  ): Authentication<AuthenticationFailure> = WithUnknownUserAuthentication(createUser, sessionProvider)
}
