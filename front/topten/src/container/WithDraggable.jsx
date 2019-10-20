import React from 'react'
import { Draggable } from 'react-beautiful-dnd';

const WithDraggable = children => props => (
    <Draggable key={`draggable-${props.id}`} draggableId={`card-${props.id}`} index={props.index}>
        {(provided, snapshot) => (
            <div
                ref={provided.innerRef}
                {...provided.draggableProps}
                {...provided.dragHandleProps}>
                {children(props)}
            </div>
        )}
    </Draggable>
)

export default WithDraggable
