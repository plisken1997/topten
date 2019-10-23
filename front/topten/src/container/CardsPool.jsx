import React from 'react'
import PropTypes from 'prop-types'
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

CardsPool.propTypes = {
    cardsPool: PropTypes.array.isRequired,
    placeholder: PropTypes.element.isRequired
}

export default WithDroppable("droppable-CardsPool")(CardsPool)
