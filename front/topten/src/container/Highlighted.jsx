import React from 'react'
import PropTypes from 'prop-types'
import Card from './Card'
import {WithDroppable, WithDraggable} from './DNDBindings'
import './Container.css'

const DraggableCard = WithDraggable(Card)

const Highlighted = ({highlighted = [], placeholder}) => {
    return (
        <div className="cont-highlighted">
            {highlighted.map((c, k) => <DraggableCard index={k} key={k} {...c}/>)}
            {placeholder}
        </div>
    )
}

Highlighted.propTypes = {
    highlighted: PropTypes.array.isRequired,
    placeholder: PropTypes.element.isRequired
}

export default WithDroppable("droppable-Highlighted")(Highlighted)
