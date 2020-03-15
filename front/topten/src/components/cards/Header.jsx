import React from 'react'
import PropTypes from 'prop-types'

const Header = ({config}) => (
    <section>
        <div>
            <h1>{config.name}</h1>
            {config.desc && <p>{config.desc}</p>}
        </div>
        {/*<div>
            <button>cancel last</button>
            <button>clear all</button>
        </div>*/}
    </section>
)

Header.propTypes = {
    config: PropTypes.object.isRequired
}

export default Header
