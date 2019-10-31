import React from 'react'
import { Droppable, Draggable } from 'react-beautiful-dnd';

const WithDroppable = id => children => props => (
    <Droppable droppableId={id}>
        {(provided, snapshot) => (
            <div ref={provided.innerRef} {...provided.droppableProps} >
            {children({...props, placeholder: provided.placeholder})}
        </div>)}
    </Droppable>
)

const WithDraggable = children => props => (
    <Draggable key={`draggable-${props.id}`} draggableId={props.id} index={props.index}>
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

export { WithDroppable, WithDraggable }
