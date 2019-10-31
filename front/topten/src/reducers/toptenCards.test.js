import reduceTopTenCards from './toptenCards'
import * as actions from '../cards/actions/toptenCards'
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
        type: actions.ADD_NEW_CARD,
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


test('should unpromote a card', () => {

    expect(defaultState.cardsPool.map(c => c.id)).toEqual(expect.arrayContaining([1, 2, 3, 4]))
    expect(defaultState.highlighted.map(c => c.id)).toEqual(expect.arrayContaining([5, 6]))
    
    const payload = {
        id: 5
    }

    const action = {
        payload,
        type: actions.UNPROMOTE_CARD,
    }

    const newState = reduceTopTenCards(defaultState, action)
    const {highlighted, cardsPool} = newState

    expect(cardsPool.map(c => c.id)).toEqual(expect.arrayContaining([1, 2, 3, 4, 5]))
    expect(highlighted.map(c => c.id)).toEqual(expect.arrayContaining([6]))
})

test('should drop a card only when the card comes from the cardspool', () => {

    expect(defaultState.cardsPool.map(c => c.id)).toEqual(expect.arrayContaining([1, 2, 3, 4]))
    expect(defaultState.highlighted.map(c => c.id)).toEqual(expect.arrayContaining([5, 6]))    

    const action = {
        payload: {id: 2},
        type: actions.DROP_CARD,
    }

    const newState = reduceTopTenCards(defaultState, action)
    
    expect(newState.cardsPool.map(c => c.id)).toEqual(expect.arrayContaining([1, 3, 4]))
    expect(newState.highlighted.map(c => c.id)).toEqual(expect.arrayContaining([5, 6]))

    const rejectedAction = {
        payload: {id: 5},
        type: actions.DROP_CARD,
    }
    
    const nextState = reduceTopTenCards(newState, rejectedAction)

    expect(nextState.cardsPool.map(c => c.id)).toEqual(expect.arrayContaining([1, 3, 4]))
    expect(nextState.highlighted.map(c => c.id)).toEqual(expect.arrayContaining([5, 6]))
})
