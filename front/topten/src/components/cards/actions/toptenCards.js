import update from './reorder'
import {updateValue} from '../../../util/actions'
import config from '../../../config'

export const ON_DRAG_END_EMPTY = 'ON_DRAG_END_EMPTY'
export const ON_DRAG_END = 'ON_DRAG_END'

const onDragEnd = (highlightedInput, cardsPoolInput, result) => {
  if (!result.destination) {
    return {
      type: ON_DRAG_END_EMPTY
    }
  }
  const {cardsPool, highlighted} = update(result, highlightedInput, cardsPoolInput)
  return {
      type: ON_DRAG_END,
      payload: {
          cardsPool,
          highlighted
      }
  }
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

const unpromote = (id, cardsPoolId) => {
  return {
    type: UNPROMOTE_CARD,
    payload: {
      id
    }
  }
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

export { onDragEnd, addCard, newCardChange, unpromote, dropCard }
