package org.plsk.user.dao

import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter
import org.plsk.user.User

interface UserRepository: DataReader<User, String>, DataWriter<User, String>
