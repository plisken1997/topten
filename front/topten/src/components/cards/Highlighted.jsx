import React from 'react'
import PropTypes from 'prop-types'
import Card from './Card'
import {WithDroppable, WithDraggable} from '../../hoc/DNDBindings'
import './style/highlighted.css'

const DraggableCard = WithDraggable(Card)

const Highlighted = ({highlighted = [], placeholder, unpromote, updateCard}) => {
    return (
        <div className="cont-highlighted">
            {highlighted.map((c, k) => (
                <div className="highlighted-item" key={k}>
                    <div className="highlighted-pos">{k+1}</div>
                    <DraggableCard index={k} key={k} {...c} updateCard={updateCard} />
                    <button className="highlighted-unpromote" onClick={unpromote(c.id)}>remove</button>
                </div>
                )
            )}
            {placeholder}
        </div>
    )
}

Highlighted.propTypes = {
    highlighted: PropTypes.array.isRequired,
    placeholder: PropTypes.element.isRequired,
    unpromote: PropTypes.func.isRequired,
    updateCard: PropTypes.func.isRequired
}

export default WithDroppable("droppable-Highlighted")(Highlighted)
