package org.plsk.security.accessToken

import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter
import org.plsk.core.dao.QueryFilter

class AccessTokenRepository<AccessToken, UUID>: DataReader<AccessToken, UUID>, DataWriter<AccessToken, UUID> {
  override fun store(data: AccessToken): UUID {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun update(data: AccessToken): UUID {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun findAll(filter: Iterable<QueryFilter>): List<AccessToken> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun find(id: UUID): AccessToken? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}