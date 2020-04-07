import React from 'react'
import PropTypes from 'prop-types'
import Card from './Card'
import {WithDroppable, WithDraggable} from '../../hoc/DNDBindings'
import './style/cardsPool.css'

const DraggableCard = WithDraggable(Card)

const CardsPool = ({cardsPool = [], placeholder, dropCard, updateCard, ...props}) => {
    return (
        <div className="cont-cards-pool">
            {cardsPool.map((c, k) => (
                <div className="cards-pool-item" key={k}>
                    <DraggableCard {...c} index={k} key={k} updateCard={updateCard}/>
                    <button className="cards-poll-drop" onClick={dropCard(c.id)}>drop</button>
                </div>
            ))}
            {placeholder}
        </div>
    )
}

CardsPool.propTypes = {
    cardsPool: PropTypes.array.isRequired,
    placeholder: PropTypes.element.isRequired,
    dropCard: PropTypes.func.isRequired,
    updateCard: PropTypes.func.isRequired
}

export default WithDroppable("droppable-CardsPool")(CardsPool)
