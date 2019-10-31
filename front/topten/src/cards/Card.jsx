import React from 'react'
import PropTypes from 'prop-types'

const Card = ({id, title, desc}) => {
    return (
        <div className="cont-card" id={`card-id-${id}`}>
            <span className="card-title">{title}</span>
            <span className="card-desc">{desc}</span>
        </div>
    )
}

Card.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    desc: PropTypes.string.isRequired
}

export default Card
