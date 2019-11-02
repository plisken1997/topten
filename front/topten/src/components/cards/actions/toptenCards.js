import update from './reorder'
import {updateValue} from '../../../util/actions'

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

const addCard = (newCard) => {
  const isComplete = (newCard) => newCard.title.trim().length > 0
  if (!isComplete(newCard)) {
    return {type: SKIP}
  }
  return {
    type: ADD_NEW_CARD,
    payload: newCard
  }
}

export const NEW_CARD_CHANGED = 'NEW_CARD_CHANGED'

const newCardChange = updateValue(NEW_CARD_CHANGED)

export const UNPROMOTE_CARD = 'UNPROMOTE_CARD'

const unpromote = id => {
  return {
    type: UNPROMOTE_CARD,
    payload: {
      id
    }
  }
}

export const DROP_CARD = 'DROP_CARD'
const dropCard =  id => {
  return {
    type: DROP_CARD,
    payload: {
      id
    }
  }
}

export { onDragEnd, addCard, newCardChange, unpromote, dropCard }
