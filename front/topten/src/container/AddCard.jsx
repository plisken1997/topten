import React from 'react'
import PropTypes from 'prop-types'

const AddCard = (props) => (
    <div className="cont-card" id="add-card">
        <span className="card-title"><input type="text" placeholder="title" id="add-card-title" value={props.card.title}/></span>
        <span className="card-desc"><textarea placeholder="desc" id="add-card-desc" value={props.card.desc}></textarea></span>
        <button onClick={props.addCard}>ok</button>
    </div>
)

AddCard.propTypes = {
    card: PropTypes.object.isRequired,
    addCard: PropTypes.func.isRequired
}

export default AddCard
