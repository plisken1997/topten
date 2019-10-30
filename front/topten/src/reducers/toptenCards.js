import * as actions from '../actions/toptenCards'
//import {cardsPool, highlighted} from './fixtures/cardsPools'

const card = (id, title, desc) => ({id, title, desc})

const newCard = {title: '', desc: ''}

const cardsPool = []
const highlighted = []

const defaultState = {
  cardsPool,
  highlighted,
  newCard
}

const reduceTopTenCards = (state = defaultState, action) => {
  switch (action.type) {
    case actions.ON_DRAG_END_EMPTY:
      return state
    case actions.ON_DRAG_END:
      const {cardsPool, highlighted} = action.payload
      return {...state, cardsPool, highlighted}
    case actions.ADD_NEW_CARD:
      const addCard = card(genId(state.cardsPool, state.highlighted), action.payload.title, action.payload.desc)
      return {...state, newCard, cardsPool: state.cardsPool.concat([addCard])}
    case actions.NEW_CARD_CHANGED:
      return {...state, newCard: action.payload}
    case actions.UNPROMOTE_CARD:
      const newhighlighted = state.highlighted.filter(c => c.id !== action.payload.id)
      const newcardsPool = state.cardsPool.concat(state.highlighted.filter(c => c.id === action.payload.id))
      return {...state, highlighted: newhighlighted, cardsPool: newcardsPool}
    case actions.DROP_CARD:
        return {...state, cardsPool: state.cardsPool.filter(c => c.id !== action.payload.id)}
    case actions.SKIP:
      console.log("skip action", action)
      return state
    default:
      return state
  }
}
  
export const extractHighestId = (cardsPool, highlighted) => {
  const start = cardsPool.length > 0 ? cardsPool[0].id : (highlighted.length > 0 ? highlighted[0] : 0)
  return [...cardsPool, ...highlighted].reduce((highestID, c) => highestID < c.id ? c.id : highestID, start)
}

const genId = (cardsPool, highlighted) => extractHighestId(cardsPool, highlighted) + 1

export default reduceTopTenCards
