package org.plsk.core.validation

interface Validation<Command, Target> {

    suspend fun validate(command: Command): Target

}