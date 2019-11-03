import * as actions from '../components/enterApp/actions/enterApp'

const emptyToptenConfig = {name: '', description: '', slots: 10}

const defaultState = {
    toptenConfig: emptyToptenConfig,
    toptens: [],
    configErrors: []
}

const reduceEnterApp = (state = defaultState, action) => {
    switch (action.type) {
        case actions.CHANGE_TOPTEN_CONFIG:
            return {...state, toptenConfig: action.payload}
        case actions.SAVE_TOPTEN_CONFIG_START:
            return {...state, configErrors: []}
        case actions.SAVE_TOPTEN_CONFIG_ERROR:
            return {...state, configErrors: action.errors}
        case actions.SAVE_TOPTEN_CONFIG:
            const newToptens = state.toptens.concat([action.payload])
            return {...state, toptenConfig: emptyToptenConfig, toptens: newToptens, configErrors: []}
        default:
            return state
    }
}

export const getConfig = (toptens, configId) => {
    const config = toptens.find(topten => topten.id === configId)
    return config || {}
}

export default reduceEnterApp
