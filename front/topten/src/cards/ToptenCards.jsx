import React from 'react'
import PropTypes from 'prop-types'
import Highlighted from './Highlighted'
import CardsPool from './CardsPool'
import { DragDropContext } from 'react-beautiful-dnd'
import Button from './AddButton'
import AddCard from './AddCard'
import Header from './Header'
import './style/container.css'

class ToptenCards extends React.Component{

    constructor() {
        super()
        //console.log(`loading list ${useParams('toptenId')}`)
        this.state = {showAddCard: false}
    }

    displayAdd() {
        this.setState({showAddCard: true})
    }

    render() {
        const {highlighted, cardsPool, onDragEnd, newCard, addCard, newCardChange, unpromote, dropCard, toptenConfig} = this.props
        const {showAddCard} = this.state
        return (
            <DragDropContext onDragEnd={onDragEnd(highlighted, cardsPool)}>
                <Header config={toptenConfig}/>
                <div className="cont-list">
                    <Highlighted highlighted={highlighted} unpromote={unpromote}/>
                    <CardsPool cardsPool={cardsPool} dropCard={dropCard}/>
                    <Button onClick={this.displayAdd.bind(this)}/>
                    {showAddCard && <AddCard card={newCard} addCard={addCard(newCard)} handleChange={newCardChange(newCard)}/>}
                </div>
            </DragDropContext>
        )
    }
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
