package org.plsk.web.config

import com.mongodb.reactivestreams.client.MongoDatabase
import org.plsk.core.command.CommandHandler
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
  private val appSecret: String = "c8f8e18a-5625-4f6f-8549-c990d5c5847f"

  @Bean
  fun provideAccessTokenProvider(accessTokenRepository: AccessTokenRepository): AccessTokenProvider =
      JwtsTokenProvider(accessTokenRepository, appId, appSecret)

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
