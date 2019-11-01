import reducer from './enterApp'
import * as actions from '../enterApp/actions/enterApp'

const emptyToptenConfig = {name: "", slots: 10}

const defaultState = {
    toptenConfig: emptyToptenConfig,
    toptens: []
}

test('change the topten config name should update the remaining object', () => {
    const config = {
        name: "",
        slots: 20
    }
    const event = {
        target: {
            value: 'test topten config'
        }
    }
    const action = actions.toptenConfigChange('name', config, event)
    const newState = reducer(defaultState, action)
    const expectedState = {
        toptenConfig: {
            name: 'test topten config',
            slots: 20
        },
        toptens: []
    }
    expect(newState).toEqual(expectedState)
})

test('add a valid topten config should add the config at the end of the list', () => {
    const config = {
        name: "test topten config",
        slots: 20
    }
    const action = actions.saveConfig(config)
    const newState = reducer(defaultState, action)
    const expectedState = {
        toptenConfig: emptyToptenConfig,
        toptens: [{...config, id: '5c7eaa0c-35c4-4843-adc8-d759e9e6cc24'}]
    }
    expect(newState).toEqual(expectedState)
})
