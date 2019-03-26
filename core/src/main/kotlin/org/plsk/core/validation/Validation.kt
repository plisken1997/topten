package org.plsk.core.validation

interface Validation<Command, Target> {

    fun validate(command: Command): Target

}