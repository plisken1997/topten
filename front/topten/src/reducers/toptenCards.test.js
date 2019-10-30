import reduceTopTenCards from './toptenCards'
import {ADD_NEW_CARD} from '../actions/toptenCards'
import {cardsPool, highlighted, card} from './fixtures/cardsPools'

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