import React, {useState} from 'react'
import PropTypes from 'prop-types'

const Card = ({id, title, description, updateCard}) => {
    const [isEditName, setIsEditName] = useState(false)
    const handleSetEditName = v => e => setIsEditName(v)

    const [n, setName] = useState(title)
    const handleChange = field => e => setName(e.target.value)
    const handleUpdate = field => e => {
        if(e.key.toLowerCase() === 'enter') {
            updateCard(id, field, e.target.value)
            setIsEditName(false)
        }
    }

    return (
        <div className="cont-card" id={`card-id-${id}`}>
            {isEditName && <span className="card-title" >
                <input type="text" value={n} autoFocus onBlur={handleSetEditName(false)} onChange={handleChange("title")} onKeyPress={handleUpdate("title")} />
                <br/>
                <span className="save-tip">press enter to save</span>
            </span>}
            {!isEditName && <span className="card-title" onClick={handleSetEditName(true)}>{title}</span>}
            {description && <span className="card-desc">{description}</span>}
        </div>
    )
}

Card.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    updateCard: PropTypes.func.isRequired,
}

export default Card
