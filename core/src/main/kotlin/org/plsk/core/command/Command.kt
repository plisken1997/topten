package org.plsk.core.command


interface CommandHandler<Command, Result> {

    suspend fun handle(command: Command): Result

}