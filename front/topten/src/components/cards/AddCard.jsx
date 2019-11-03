import React from 'react'
import PropTypes from 'prop-types'

const AddCard = (props) => (
    <form className="cont-card" id="add-card" onSubmit={e => e.preventDefault()}>
        <span className="card-title">
            <input type="text" placeholder="title" id="add-card-title" value={props.card.title} onChange={props.handleChange("title")}/>
        </span>
        <span className="card-desc">
            <textarea placeholder="desc" id="add-card-desc" value={props.card.description} onChange={props.handleChange("description")}/>
        </span>
        <input type="submit" onClick={props.addCard} value="ok"/>
    </form>
)

AddCard.propTypes = {
    card: PropTypes.object.isRequired,
    addCard: PropTypes.func.isRequired,
    handleChange: PropTypes.func.isRequired
}

export default AddCard
