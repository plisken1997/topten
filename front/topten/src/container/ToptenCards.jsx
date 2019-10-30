import React from 'react'
import { connect } from 'react-redux'
import Highlighted from './Highlighted'
import CardsPool from './CardsPool'
import { DragDropContext } from 'react-beautiful-dnd'
import './Container.css'
import {onDragEnd, addCard, newCardChange, unpromote} from '../actions/toptenCards'
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
        const {highlighted, cardsPool, onDragEnd, newCard, addCard, newCardChange, unpromote} = this.props
        const {showAddCard} = this.state
        return (
            <DragDropContext onDragEnd={onDragEnd(highlighted, cardsPool)}>
            <div className="cont-list">
                <Highlighted highlighted={highlighted} unpromote={unpromote}/>
                <CardsPool cardsPool={cardsPool}/>
                <Button onClick={this.displayAdd.bind(this)}/>
                {showAddCard && <AddCard card={newCard} addCard={addCard(newCard)} handleChange={newCardChange(newCard)}/>}
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
    addCard: newCard => () => dispatch(addCard(newCard)),
    newCardChange: obj => field => e => dispatch(newCardChange(field, obj, e)),
    unpromote: id => () => dispatch(unpromote(id))
})

export default connect(mapStateToProps, mapDispatchToProps)(ToptenCards)
