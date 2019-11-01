import {updateValue} from '../../util/actions'

export const CHANGE_TOPTEN_CONFIG = 'CHANGE_TOPTEN_CONFIG'
export const toptenConfigChange = updateValue(CHANGE_TOPTEN_CONFIG)

export const SAVE_TOPTEN_CONFIG = 'SAVE_TOPTEN_CONFIG'
export const saveConfig = (obj) => {
    console.log('create', obj)
    return {
        type: SAVE_TOPTEN_CONFIG,
        payload: obj
    }
}
