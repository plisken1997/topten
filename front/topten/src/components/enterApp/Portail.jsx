import React from 'react'
import PropTypes from 'prop-types'
import ConfigureTopTen from './ConfigureTopTen'
import UserLists from './UserLists'

const hasToken = () => !!localStorage.getItem('access_token')

const Portail = ({toptenConfig, handleChange, saveConfig, toptens, ...props}) => {
    if (!hasToken()) {
        props.initSession()
        return <div>init session...</div>
    }
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
    hasSession: PropTypes.bool.isRequired,
    handleChange: PropTypes.func.isRequired,
    saveConfig: PropTypes.func.isRequired,
    initSession: PropTypes.func.isRequired,
}

export default Portail