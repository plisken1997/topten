import update from '../container/Reorder'

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

const addCard = (newCard) => ({
  type: ADD_NEW_CARD,
  payload: newCard
})

export { onDragEnd, addCard };
