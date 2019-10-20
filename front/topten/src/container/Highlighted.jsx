import React from 'react'
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

export default WithDroppable("droppable-Highlighted")(Highlighted)
