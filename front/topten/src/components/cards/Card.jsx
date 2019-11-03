import React from 'react'
import PropTypes from 'prop-types'

const Card = ({id, title, description}) => {
    return (
        <div className="cont-card" id={`card-id-${id}`}>
            <span className="card-title">{title}</span>
            <span className="card-desc">{description}</span>
        </div>
    )
}

Card.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired
}

export default Card
