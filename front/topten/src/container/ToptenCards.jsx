import React from 'react'
import { connect } from 'react-redux'
import Highlighted from './Highlighted'
import CardsPool from './CardsPool'
import { DragDropContext } from 'react-beautiful-dnd'
import './Container.css'
import {onDragEnd} from '../actions/toptenCards'

const ToptenCards = (props) => {
        const {highlighted, cardsPool, onDragEnd} = props
    return (
        <DragDropContext onDragEnd={onDragEnd(highlighted, cardsPool)}>
        <div className="cont-list">
            <Highlighted highlighted={highlighted}/>
            <CardsPool cardsPool={cardsPool}/>
        </div>
        </DragDropContext>
    )
}

const mapStateToProps = state => ({
    cardsPool: state.toptenCards.cardsPool,
    highlighted: state.toptenCards.highlighted
})

const mapDispatchToProps = dispatch => ({
    onDragEnd: (highlightedInput, cardsPoolInput) => (result) => dispatch(onDragEnd(highlightedInput, cardsPoolInput, result))
})

export default connect(mapStateToProps, mapDispatchToProps)(ToptenCards)
