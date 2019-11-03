import React, {useState} from 'react'
import PropTypes from 'prop-types'
import { DragDropContext } from 'react-beautiful-dnd'
import Highlighted from './Highlighted'
import CardsPool from './CardsPool'
import Button from './AddButton'
import AddCard from './AddCard'
import Header from './Header'
import './style/container.css'

const ToptenCards = (props) => {
    const [addCardDisplayed, displayAddCardForm] = useState(false)
    const {highlighted, cardsPool, onDragEnd, newCard, addCard, newCardChange, unpromote, dropCard, toptenConfig} = props
    return (
        <DragDropContext onDragEnd={onDragEnd(highlighted, cardsPool)}>
            <Header config={toptenConfig}/>
            <div className="cont-list">
                <Highlighted highlighted={highlighted} unpromote={unpromote}/>
                <CardsPool cardsPool={cardsPool} dropCard={dropCard}/>
                <Button onClick={displayAddCardForm}/>
                {addCardDisplayed && <AddCard card={newCard} addCard={addCard(newCard)} handleChange={newCardChange(newCard)}/>}
            </div>
        </DragDropContext>
    )
}

ToptenCards.propTypes = {
    cardsPool: PropTypes.array.isRequired,
    highlighted: PropTypes.array.isRequired,
    newCard: PropTypes.object.isRequired,
    toptenConfig: PropTypes.object.isRequired,
    onDragEnd: PropTypes.func.isRequired,
    addCard: PropTypes.func.isRequired,
    newCardChange: PropTypes.func.isRequired,
    unpromote: PropTypes.func.isRequired,
    dropCard: PropTypes.func.isRequired,
}

export default ToptenCards
