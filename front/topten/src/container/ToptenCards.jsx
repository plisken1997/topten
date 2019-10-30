import React from 'react'
import { connect } from 'react-redux'
import Highlighted from './Highlighted'
import CardsPool from './CardsPool'
import { DragDropContext } from 'react-beautiful-dnd'
import './Container.css'
import {onDragEnd, addCard} from '../actions/toptenCards'
import Button from './AddButton'
import AddCard from './AddCard'

class ToptenCards extends React.Component{

    constructor() {
        super()
        this.state = {showAddCard: false}
    }

    displayAdd() {
        this.setState({showAddCard: true})
    }

    render() {
        const {highlighted, cardsPool, onDragEnd, newCard, addCard} = this.props
        const {showAddCard} = this.state
        return (
            <DragDropContext onDragEnd={onDragEnd(highlighted, cardsPool)}>
            <div className="cont-list">
                <Highlighted highlighted={highlighted}/>
                <CardsPool cardsPool={cardsPool}/>
                <Button onClick={this.displayAdd.bind(this)}/>
                {showAddCard && <AddCard card={newCard} addCard={addCard(newCard)}/>}
            </div>
            </DragDropContext>
        )
    }
}

const mapStateToProps = state => ({
    cardsPool: state.toptenCards.cardsPool,
    highlighted: state.toptenCards.highlighted,
    newCard: state.toptenCards.newCard
})

const mapDispatchToProps = dispatch => ({
    onDragEnd: (highlightedInput, cardsPoolInput) => (result) => dispatch(onDragEnd(highlightedInput, cardsPoolInput, result)),
    addCard: newCard => () => dispatch(addCard(newCard))
})

export default connect(mapStateToProps, mapDispatchToProps)(ToptenCards)
