import React from 'react'
import PropTypes from 'prop-types'
import ConfigureTopTen from './ConfigureTopTen'
import UserLists from './UserLists'

const Portail = ({toptenConfig, handleChange, saveConfig, toptens, ...props}) => {
    return (
    <div>
        <ConfigureTopTen toptenConfig={toptenConfig} handleChange={handleChange(toptenConfig)} save={saveConfig(toptenConfig)}/>
        {toptens.length > 0 && <div>OR</div>}
        {toptens.length > 0 && <UserLists toptens={toptens}/>}
    </div>
    )
}

Portail.propTypes = {
    toptenConfig: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
    saveConfig: PropTypes.func.isRequired,
}

export default Portail