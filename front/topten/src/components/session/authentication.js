import config from '../../config'

export const INIT_SESSION_START = 'INIT_SESSION_START'
export const SESSION_OK = 'SESSION_OK'

export const initSession = httpGet => () => dispatch => {
    dispatch({type: INIT_SESSION_START})

    return httpGet(config.toptenConfig.initSession.path())
      .then(({data}) => {
        dispatch({
            type: SESSION_OK,
            payload: data
        })
    })
  }