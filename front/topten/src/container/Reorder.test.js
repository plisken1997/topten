import update from './Reorder'

const card = (id, title, label) => ({id, title, label})

const card1 = card(1, "Casino", "I whish I could see this soon")
const card2 = card(2, "Taxi driver", "time to see it again")
const card3 = card(3, "Aviator", "I'll just need 3 hours free")
const card4 = card(4, "The wall street wolf", "Saw it only 2 times, needs a third")
const card5 = card(5, "The irishman", "the last Scorsese movie !")
const card6 = card(6, "Goodfellas", "my favorite")

test('should reorder the highlight column', () => {
    const result = {
        draggableId: 5,
        source: {
            index: 4,
            droppableId: "droppable-Highlighted"
        },
        destination: {
            index: 2,
            droppableId: "droppable-Highlighted"
        }
    }
    const {highlighted} = update(result, [card1, card2, card3, card4, card5, card6], [])
    expect(highlighted[2]).toEqual(card5)
});


test('should reorder the cardsPool column', () => {
    const result = {
        draggableId: 5,
        source: {
            index: 4,
            droppableId: "droppable-CardsPool"
        },
        destination: {
            index: 2,
            droppableId: "droppable-CardsPool"
        }
    }
    const {cardsPool} = update(result, [], [card1, card2, card3, card4, card5, card6])
    expect(cardsPool[2]).toEqual(card5)
});


test('should add a card to the beginning of the highlight column', () => {
    const result = {
        draggableId: 5,
        source: {
            index: 1,
            droppableId: "droppable-CardsPool"
        },
        destination: {
            index: 0,
            droppableId: "droppable-Highlighted"
        }
    }
    const {highlighted, cardsPool} = update(result, [card1, card2, card3], [card4, card5, card6])
    expect(highlighted[0]).toEqual(card5)
    expect(cardsPool).not.toContain(card5)
    expect(highlighted).toHaveLength(4)
    expect(cardsPool).toHaveLength(2)
});

test('should add a card to the end of the cardsPool column', () => {
    const result = {
        draggableId: 2,
        source: {
            index: 1,
            droppableId: "droppable-Highlighted"
        },
        destination: {
            index: 3,
            droppableId: "droppable-CardsPool"
        }
    }
    const {highlighted, cardsPool} = update(result, [card1, card2, card3], [card4, card5, card6])
    expect(cardsPool[3]).toEqual(card2)
    expect(highlighted).not.toContain(card2)
    expect(highlighted).toHaveLength(2)
    expect(cardsPool).toHaveLength(4)
});

test('should add a card as the 3th position of the cardsPool column', () => {
    const result = {
        draggableId: 2,
        source: {
            index: 1,
            droppableId: "droppable-Highlighted"
        },
        destination: {
            index: 2,
            droppableId: "droppable-CardsPool"
        }
    }
    const {highlighted, cardsPool} = update(result, [card1, card2], [card4, card3, card5, card6])
    expect(cardsPool[2]).toEqual(card2)
    expect(highlighted).not.toContain(card2)
    expect(highlighted).toHaveLength(1)
    expect(cardsPool).toHaveLength(5)
});


test('should add the first card of the Highlighted column', () => {
    const result = {
        draggableId: 2,
        source: {
            index: 1,
            droppableId: "droppable-CardsPool"
        },
        destination: {
            index: 0,
            droppableId: "droppable-Highlighted"
        }
    }
    const {highlighted, cardsPool} = update(result, [], [card1, card2, card4, card3, card5, card6])
    expect(highlighted[0]).toEqual(card2)
    expect(highlighted).toHaveLength(1)
    expect(cardsPool).not.toContain(card2)
    expect(cardsPool).toHaveLength(5)
});
