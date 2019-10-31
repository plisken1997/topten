import React from 'react'
import PropTypes from 'prop-types'
import './style/components.css'

const Button = (props) => (
    <div className="add-button">
        <button onClick={props.onClick}>+ add</button>
    </div>
)

Button.propTypes = {
    onClick: PropTypes.func.isRequired
}

export default Button
