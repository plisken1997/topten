import React, {useState, useEffect} from 'react'
import PropTypes from 'prop-types'
import { DragDropContext } from 'react-beautiful-dnd'
import Highlighted from './Highlighted'
import CardsPool from './CardsPool'
import Button from './AddButton'
import AddCard from './AddCard'
import Header from './Header'
import './style/container.css'
import { useParams } from "react-router-dom";

const ToptenCards = (props) => {
    const { toptenId } = useParams('toptenId')
    const [addCardDisplayed, displayAddCardForm] = useState(false)
    const {highlighted, cardsPool, onDragEnd, newCard, addCard, newCardChange, unpromote, dropCard, toptenConfig, loadCards, updateCard} = props
    const loaded = true
    const cardsPoolId = toptenConfig.id || toptenId

    useEffect(() => {
        loadCards(cardsPoolId)
    }, [loaded, loadCards, cardsPoolId])
    
    return (
        <DragDropContext onDragEnd={onDragEnd(highlighted, cardsPool, cardsPoolId)}>
            <Header config={toptenConfig}/>
            <div className="cont-list">
                <Highlighted highlighted={highlighted} unpromote={unpromote(cardsPoolId)} updateCard={updateCard(cardsPoolId)}/>
                <CardsPool cardsPool={cardsPool} dropCard={dropCard(cardsPoolId)} updateCard={updateCard(cardsPoolId)}/>
                <Button onClick={displayAddCardForm}/>
                {addCardDisplayed && <AddCard card={newCard} addCard={addCard(newCard, cardsPoolId)} handleChange={newCardChange(newCard)}/>}
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
    loadCards: PropTypes.func.isRequired,
    updateCard: PropTypes.func.isRequired
}

export default ToptenCards
