import * as actions from '../actions/toptenCards'

const card = (id, title, label) => ({id, title, label})

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

const defaultState = {
  cardsPool,
  highlighted
}

const topTenCards = (state = defaultState, action) => {
    switch (action.type) {
      case actions.ON_DRAG_END_EMPTY:
        return state
      case actions.ON_DRAG_END:
        const {cardsPool, highlighted} = action.payload
        return {...state, cardsPool, highlighted}
      default:
        return state
    }
  }
  
  export default topTenCards
