import * as actions from '../components/enterApp/actions/enterApp'

const emptyToptenConfig = {name: '', description: '', slots: 10}

const defaultState = {
    toptenConfig: emptyToptenConfig,
    toptens: [
        {id: '6f4d1eee-e687-3ca8-944b-cd361ac5fb76', name: 'best games for ever', description: 'a laconic list of my favorite games ever', slots: 50}
    ],
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
