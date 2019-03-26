package org.plsk.core.command


interface CommandHandler<Command> {

    fun handle(command: Command)

}