import React from 'react'
import Highlighted from './Highlighted'
import CardsPool from './CardsPool'
import { DragDropContext } from 'react-beautiful-dnd'
import update from './Reorder'
import './Container.css'

const card = (id, title, label) => ({id, title, label})

const cardsPool = [
    card(1, "Casino", "I whish I could see this soon"), 
    card(2, "Taxi driver", "time to see it again"), 
    card(3, "Aviator", "I'll just need 3 hours free"), 
    card(4, "The wall street wolf", "Saw it only 2 times, needs a third")
]
const highlighted = [
    card(5, "The irishman", "the last Scorsese movie !"), 
    card(6, "Goodfellas", "my favorite")
]

class ListContainer extends React.Component{
    constructor(props) {
        super()
        this.state = {
            highlighted,
            cardsPool,
        }
        this.onDragEnd = this.onDragEnd.bind(this)
    }

    onDragEnd(result) {
        if (!result.destination) {
          return[]
        }
        const {cardsPool, highlighted} = update(result, this.state.highlighted, this.state.cardsPool)
        this.setState({
            cardsPool,
            highlighted
        })
      }

    render (props) {
        const highlighted = this.state.highlighted
        const cardsPool = this.state.cardsPool
        return (
            <DragDropContext onDragEnd={this.onDragEnd}>
            <div className="cont-list">
                <Highlighted highlighted={highlighted}/>
                <CardsPool cardsPool={cardsPool}/>
            </div>
            </DragDropContext>
        )
    }
}

export default ListContainer
