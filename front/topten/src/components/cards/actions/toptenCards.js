import update from './reorder'
import {updateValue} from '../../../util/actions'
import config from '../../../config'

export const ON_DRAG_END_EMPTY = 'ON_DRAG_END_EMPTY'
export const ON_DRAG_END = 'ON_DRAG_END'

const getSaveFn = (source, destination) => {
  const promote = (httpPost, cardsPoolId, payload) => httpPost(config.toptenConfig.promote.path(cardsPoolId), payload)
  const updateTop = (httpPost, cardsPoolId, payload) => httpPost(config.toptenConfig.updateTop.path(cardsPoolId), payload)
  const unPromote = (httpPost, cardsPoolId, payload) => httpPost(config.toptenConfig.unpromote.path(cardsPoolId), payload)
  const noop = (httpPost, cardsPoolId, payload) => new Promise(resolve => resolve({}))
  if (source === 'droppable-Highlighted') {
    if (source === destination) {
      return updateTop
    }
    return unPromote
  }

  if (source === destination) {
    return noop
  }

  return promote
}
const onDragEnd = (httpPost) => (highlightedInput, cardsPoolInput, cardsPoolId, result) => dispatch => {
  if (!result.destination) {
    return dispatch({
      type: ON_DRAG_END_EMPTY
    })
  }
  console.log(result)
  const saveFn = getSaveFn(result.source.droppableId, result.destination.droppableId)
  const payload = {
    cardId: result.draggableId,
    position: result.destination.index
  } 
  const {cardsPool, highlighted} = update(result, highlightedInput, cardsPoolInput)
  return saveFn(httpPost, cardsPoolId, payload)
    .then(() => dispatch({
      type: ON_DRAG_END,
      payload: {
          cardsPool,
          highlighted
      }
    })
  )
}

export const ADD_NEW_CARD = 'ADD_NEW_CARD'
export const SKIP = 'SKIP'

const saveNewCard = (httpPost, newCard, cardsPoolId) => httpPost(config.toptenConfig.addCard.path(cardsPoolId), newCard)

const addCard = (httpPost) => (newCard, cardsPoolId) => dispatch => {
  const isComplete = (newCard) => newCard.title.trim().length > 0
  if (!isComplete(newCard)) {
    return dispatch({type: SKIP})
  }
  return saveNewCard(httpPost, newCard, cardsPoolId)
    .then(({data}) => dispatch({
        type: ADD_NEW_CARD,
        payload: {...newCard, id: data.id}
      })
    )
}

export const NEW_CARD_CHANGED = 'NEW_CARD_CHANGED'

const newCardChange = updateValue(NEW_CARD_CHANGED)

export const UNPROMOTE_CARD = 'UNPROMOTE_CARD'
const saveUnpromote = (httpPost, cardId, cardsPoolId) => httpPost(config.toptenConfig.unpromote.path(cardsPoolId), {cardId})
const unpromote = (httpPost) => (id, cardsPoolId) => dispatch => {
  return saveUnpromote(httpPost, id, cardsPoolId)
    .then(() => dispatch({
      type: UNPROMOTE_CARD,
      payload: {
        id
      }
    })
  )
}

export const DROP_CARD = 'DROP_CARD'
const saveDropCard = (httpDelete, cardId, cardsPoolId) => httpDelete(config.toptenConfig.dropCard.path(cardsPoolId, cardId))
const dropCard =  httpDelete => (cardId, cardsPoolId) => dispatch => {
  return saveDropCard(httpDelete, cardId, cardsPoolId)
    .then(() => dispatch({
        type: DROP_CARD,
        payload: {
          id: cardId
        }
      })
    )
}

export const LOAD_CARDS_START = 'LOAD_CARDS_START'
export const CARDS_LOADED = 'CARDS_LOADED'

const loadCards = httpGet => cardsPoolId => dispatch => {
  dispatch({type: LOAD_CARDS_START})
  return httpGet(config.toptenConfig.loadCards.path(cardsPoolId))
    .then(({data}) => dispatch({
      type: CARDS_LOADED,
      payload: data
    }))
}

const mapFieldname = fieldname => fieldname === 'title' ? 'label' : fieldname

export const UPDATE_CARD_START = 'UPDATE_CARD_START'
export const  UPDATE_CARD_SUCCEEDED = 'UPDATE_CARD_SUCCEEDED'
const updateCard = httpPatch => (cardsPoolId, cardId, fieldname, value) => dispatch => {
  const payload = {cardId, field: fieldname, value}
  
  dispatch({type: UPDATE_CARD_START, payload})
  
  const field = mapFieldname(fieldname)
  
  return httpPatch(
    config.toptenConfig.updateCard.path(cardsPoolId),
    {cardId, field, value}
  ).then(({data}) => dispatch({type: UPDATE_CARD_SUCCEEDED, payload}))
}

export { onDragEnd, addCard, newCardChange, unpromote, dropCard, loadCards, updateCard }
