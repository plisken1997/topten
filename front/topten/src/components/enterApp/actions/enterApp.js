import {updateValue} from '../../../util/actions'
import config from '../../../config'

export const CHANGE_TOPTEN_CONFIG = 'CHANGE_TOPTEN_CONFIG'
export const toptenConfigChange = updateValue(CHANGE_TOPTEN_CONFIG)

export const SAVE_TOPTEN_CONFIG_START = 'SAVE_TOPTEN_CONFIG_START'
export const SAVE_TOPTEN_CONFIG = 'SAVE_TOPTEN_CONFIG'
export const SAVE_TOPTEN_CONFIG_ERROR = 'SAVE_TOPTEN_CONFIG_ERROR'

// @todo clean values !
const cleanCardsPool = obj => obj

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

    return save(httpPost, cleanCardsPool(obj))
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

export const LOAD_TOPTEN_LIST = 'LOAD_TOPTEN_LIST'
export const TOPTEN_LIST_LOADED = 'TOPTEN_LIST_LOADED'
export const loadToptenList = httpGet => dispatch => {
    dispatch({type: LOAD_TOPTEN_LIST})

    return httpGet(config.toptenConfig.cardspool.path).then(({data}) => {
        return dispatch({
            type: TOPTEN_LIST_LOADED,
            payload: {
                toptens: data
            }
        })
    })
}