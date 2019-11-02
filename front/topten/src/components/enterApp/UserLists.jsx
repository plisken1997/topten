import React from 'react'
import PropTypes from 'prop-types'
import { Link } from "react-router-dom"

const UserLists = ({toptens}) => (
    <div>
        <h2>Pick an existing one</h2>
        <ul>
            {toptens.map((t, pos) => (
                <li key={pos}>
                    <Link to={`/topten/${t.id}`}>{t.name}</Link>
                </li>
            ))}
        </ul>
    </div>
)

UserLists.propTypes = {
    toptens: PropTypes.array.isRequired
}

export default UserLists
