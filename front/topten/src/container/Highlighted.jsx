import React from 'react'
import PropTypes from 'prop-types'
import Card from './Card'
import {WithDroppable, WithDraggable} from './DNDBindings'
import './highlighted.css'

const DraggableCard = WithDraggable(Card)

const Highlighted = ({highlighted = [], placeholder, unpromote}) => {
    return (
        <div className="cont-highlighted">
            {highlighted.map((c, k) => (
                <div className="highlighted-item" key={k}>
                    <div className="highlighted-pos">{k+1}</div>
                    <DraggableCard index={k} key={k} {...c}/>
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
    unpromote: PropTypes.func.isRequired
}

export default WithDroppable("droppable-Highlighted")(Highlighted)
