package org.plsk.core.command


interface CommandHandler<Command, Result> {

    fun handle(command: Command): Result

}