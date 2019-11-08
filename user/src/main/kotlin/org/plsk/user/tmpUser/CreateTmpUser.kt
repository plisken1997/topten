package org.plsk.user.tmpUser

import org.plsk.core.command.CommandHandler
import org.plsk.user.User

data class CreateTmpUser(val ip: String)

class CreateTmpUserHandler: CommandHandler<CreateTmpUser, User> {

  override fun handle(command: CreateTmpUser): User {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}