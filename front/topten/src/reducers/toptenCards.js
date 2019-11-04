import * as actions from '../components/cards/actions/toptenCards'
//import {cardsPool, highlighted} from './fixtures/cardsPools'

const newCard = {title: '', description: ''}

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
      return {...state, newCard, cardsPool: state.cardsPool.concat([action.payload])}
    case actions.NEW_CARD_CHANGED:
      return {...state, newCard: action.payload}
    case actions.UNPROMOTE_CARD:
      const newhighlighted = state.highlighted.filter(c => c.id !== action.payload.id)
      const newcardsPool = state.cardsPool.concat(state.highlighted.filter(c => c.id === action.payload.id))
      return {...state, highlighted: newhighlighted, cardsPool: newcardsPool}
    case actions.DROP_CARD:
      return {...state, cardsPool: state.cardsPool.filter(c => c.id !== action.payload.id)}
      case actions.CARDS_LOADED:
      return {...state, cardsPool: action.payload.cardsPool, highlighted: action.payload.highlighted}
    case actions.SKIP:
      console.log("skip action", action)
      return state
    default:
      return state
  }
}

export default reduceTopTenCards
