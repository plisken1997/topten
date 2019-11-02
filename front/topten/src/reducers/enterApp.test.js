import reducer from './enterApp'
import * as actions from '../components/enterApp/actions/enterApp'
import {post} from '../util/http/fakeHttp'

const emptyToptenConfig = {name: "", description: '', slots: 10}

const defaultState = {
    toptenConfig: emptyToptenConfig,
    toptens: [],
    configErrors: []
}

const identity = a => a

test('change the topten config name should update the remaining object', () => {
    const config = {
        name: "",
        slots: 20, 
        description: ''
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
            slots: 20,
            description: ''
        },
        toptens: [],
        configErrors: []
    }
    expect(newState).toEqual(expectedState)
})

test('add a valid topten config should add the config at the end of the list', () => {
    const config = {
        name: "test topten config",
        slots: 20, 
        description: 'description...'
    }
    
    actions.saveConfig(post({data: {id: '5c7eaa0c-35c4-4843-adc8-d759e9e6cc24'}}))(config)(identity)
        .then(action => {
            const newState = reducer({...defaultState, configErrors: [{name: 'name must not be empty'}]}, action)
            const expectedState = {
                toptenConfig: emptyToptenConfig,
                toptens: [{...config, id: '5c7eaa0c-35c4-4843-adc8-d759e9e6cc24'}],
                configErrors: []
            }
            expect(newState).toEqual(expectedState)
        })
})

test('store the topten config errors', () => {
    const config = {
        name: "",
        slots: 20, 
        description: 'description...'
    }
    
    actions.saveConfig(post({}))(config)(identity)
        .then(action => {
            const newState = reducer({...defaultState, toptenConfig: config}, action)
            const expectedState = {
                toptenConfig: config,
                toptens: [],
                configErrors: [
                    {name: 'name must not be empty'}
                ]
            }
            expect(newState).toEqual(expectedState)
        })
})
