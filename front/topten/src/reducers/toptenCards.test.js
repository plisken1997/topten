import reduceTopTenCards from './toptenCards'
import {ADD_NEW_CARD} from '../actions/toptenCards'

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

test('should add the new card at the end of the cardspool list', () => {
    const payload = {
        title: 'test',
        desc: 'description'
    }

    const action = {
        payload,
        type: ADD_NEW_CARD,
    }

    const newState = reduceTopTenCards(defaultState, action)
    const {cardsPool} = newState
    const lastCard = cardsPool[cardsPool.length -1]
    
    expect(lastCard).toEqual(card(7, payload.title, payload.desc))

    const nextState = reduceTopTenCards(newState, action)
    const newCardsPool = nextState.cardsPool
    const newLastCard = newCardsPool[newCardsPool.length -1]
    expect(newLastCard).toEqual(card(8, payload.title, payload.desc))
})