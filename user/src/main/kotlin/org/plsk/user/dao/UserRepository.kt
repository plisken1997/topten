package org.plsk.user.dao

import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter
import org.plsk.user.User

@Deprecated("UserRepository must be a domain service implementing finite use cases")
interface UserRepository: DataReader<User, String>, DataWriter<User, String>
