import React from 'react'
import PropTypes from 'prop-types'

const Card = ({id, title, label}) => {
    return (
        <div className="cont-card" id={`card-id-${id}`}>
            <span className="card-title">{title}</span>
            <span className="card-label">{label}</span>
        </div>
    )
}

Card.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired
}

export default Card
