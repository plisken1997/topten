import {updateValue} from '../../../util/actions'
import config from '../../../config'

export const CHANGE_TOPTEN_CONFIG = 'CHANGE_TOPTEN_CONFIG'
export const toptenConfigChange = updateValue(CHANGE_TOPTEN_CONFIG)

export const SAVE_TOPTEN_CONFIG_START = 'SAVE_TOPTEN_CONFIG_START'
export const SAVE_TOPTEN_CONFIG = 'SAVE_TOPTEN_CONFIG'
export const SAVE_TOPTEN_CONFIG_ERROR = 'SAVE_TOPTEN_CONFIG_ERROR'

export const saveConfig = (httpPost) => (obj) => dispatch => {
    const errors = validateSave(obj)

    if (!!errors.length) {
        return new Promise((resolve, reject) => {
            resolve(
                dispatch({
                    type: SAVE_TOPTEN_CONFIG_ERROR,
                    errors
                })
            )
        })
    }

    dispatch({type: SAVE_TOPTEN_CONFIG_START})

    return save(httpPost, obj)
        .then(({data}) => {
            const {id} = data
            return dispatch({
                type: SAVE_TOPTEN_CONFIG,
                payload: {...obj, id}
            })
        })
}

const save = (httpPost, obj) => httpPost(config.toptenConfig.cardspool.path, obj)
const validateSave = obj => {
    const errors = []
    if (!obj.name.trim().length) {
        errors.push({name: 'name must not be empty'})
    }
    return errors
}