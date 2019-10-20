import React from 'react'

const Card = ({id, title, label}) => {
    return (
        <div className="cont-card" id={`card-id-${id}`}>
            <span className="card-title">{title}</span>
            <span className="card-label">{label}</span>
        </div>
    )
}

export default Card
