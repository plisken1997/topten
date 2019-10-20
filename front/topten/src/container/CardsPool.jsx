import React from 'react'
import Card from './Card'
import {WithDroppable, WithDraggable} from './DNDBindings'
import './Container.css'

const DraggableCard = WithDraggable(Card)

const CardsPool = ({cardsPool = [], placeholder, ...props}) => {
    return (
        <div className="cont-cards-pool">
            {cardsPool.map((c, k) => <DraggableCard {...c} index={k} key={k}/>)}
            {placeholder}
        </div>
    )
}

export default WithDroppable("droppable-CardsPool")(CardsPool)
