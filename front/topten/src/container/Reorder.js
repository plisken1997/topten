const reorder = (result, startIndex, endIndex) => {
    if (result.length === 1) {
        return result
    }
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);

    return result;
}

const add = (list, item, endIndex) => {
    const result = Array.from(list);
    return result.slice(0, endIndex).concat([item]).concat(result.slice(endIndex))
}

const update = (result, highlighted, cardsPool) => {
    if (result.source.droppableId === result.destination.droppableId) {
        if (result.source.droppableId === "droppable-Highlighted") {                
            return {
                highlighted: reorder(
                    highlighted,
                    result.source.index,
                    result.destination.index
                    ),
                cardsPool
            }
        } else {        
            return {
                cardsPool: reorder(
                    cardsPool,
                    result.source.index,
                    result.destination.index
                ),
                highlighted
            }
        }
    } else {
        if (result.destination.droppableId === "droppable-Highlighted") {
            const item = cardsPool.find(e => e.id === result.draggableId)
            return {
                highlighted: add(
                    highlighted,
                    item,
                    result.destination.index
                ),
                cardsPool: cardsPool.filter(e => e.id !== result.draggableId)
            }
        } else {
            const item = highlighted.find(e => e.id === result.draggableId)
            return {
                highlighted: highlighted.filter(e => e.id !== result.draggableId),
                cardsPool: add(
                    cardsPool,
                    item,
                    result.destination.index
                )
            }
        }
    }
}

export default update
