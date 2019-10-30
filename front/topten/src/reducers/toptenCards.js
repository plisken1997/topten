import * as actions from '../actions/toptenCards'

const card = (id, title, desc) => ({id, title, desc})

const cardsPool = [
    card(1, "Casino", "I whish I could see this soon"), 
    card(2, "Taxi driver", "time to see it again"), 
    card(3, "Aviator", "I'll just need 3 hours free"), 
    card(4, "The wall street wolf", "Saw it only 2 times, needs a third")
]
const highlighted = [
    card(5, "The irishman", "the last Scorsese movie !"), 
    card(6, "Goodfellas", "my favorite")
]

const newCard = {title: '', desc: ''}

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
