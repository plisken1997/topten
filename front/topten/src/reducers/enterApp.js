import * as actions from '../components/enterApp/actions/enterApp'

const emptyToptenConfig = {name: '', description: '', slots: 10}

const defaultState = {
    toptenConfig: emptyToptenConfig,
    toptens: null,
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
        case actions.TOPTEN_LIST_LOADED:
            const extractInfo = topten => {
                const {id, name, description, slots} = topten // poor spread operation, I known...
                return {id, name, description, slots}
            }
            return {...state, toptens: action.payload.toptens.map(extractInfo)}
        default:
            return state
    }
}

export const getConfig = (toptens, configId) => {
    const config = toptens?.find(topten => topten.id === configId)
    return config || {}
}

export default reduceEnterApp
