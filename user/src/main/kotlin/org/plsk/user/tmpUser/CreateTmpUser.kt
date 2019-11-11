package org.plsk.user.tmpUser

import org.plsk.core.command.CommandHandler
import org.plsk.core.dao.DataWriter
import org.plsk.core.id.UUIDGen
import org.plsk.user.TmpUser
import org.plsk.user.User

data class CreateTmpUser(val ip: String)

class CreateTmpUserHandler(
    private val dataWriter: DataWriter<User, String>,
    private val uuidGen: UUIDGen
): CommandHandler<CreateTmpUser, User> {

  override fun handle(command: CreateTmpUser): User {
    val id = uuidGen.random().toString()
    val user = TmpUser(id, uuidGen.fromString(id + command.ip).toString(), command.ip)
    dataWriter.store(user)
    return user
  }

}