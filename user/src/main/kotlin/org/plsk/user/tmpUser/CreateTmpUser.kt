package org.plsk.user.tmpUser

import org.plsk.core.command.CommandHandler
import java.util.*

data class CreateTmpUser(val ip: String)

class CreateTmpUserHandler: CommandHandler<CreateTmpUser, UUID> {

  override fun handle(command: CreateTmpUser): UUID {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}