package org.plsk.security.accessToken

import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter
import java.util.*

interface AccessTokenRepository: DataReader<AccessToken, String>, DataWriter<AccessToken, String>
