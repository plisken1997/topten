import * as actions from '../components/session/authentication'

const defaultState = {hasSession: false}

const authentication = (state = defaultState, action) => {
    switch (action.type) {
        case actions.SESSION_OK:
            localStorage.setItem('access_token', action.payload.token)
            return {...state, hasSession: true}
        default:
            return state
    }
}

export default authentication
