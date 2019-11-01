import * as actions from '../enterApp/actions/enterApp'

const emptyToptenConfig = {name: "", slots: 10}

const defaultState = {
    toptenConfig: emptyToptenConfig,
    toptens: []
}

const reduceEnterApp = (state = defaultState, action) => {
    switch (action.type) {
        case actions.CHANGE_TOPTEN_CONFIG:
            return {...state, toptenConfig: action.payload}
        case actions.SAVE_TOPTEN_CONFIG:
            const id = '5c7eaa0c-35c4-4843-adc8-d759e9e6cc24'
            const newToptens = state.toptens.concat([{...action.payload, id}])
            return {...state, toptenConfig: emptyToptenConfig, toptens: newToptens}
        default:
            return state
    }
}

export default reduceEnterApp
