import React from 'react'
import PropTypes from 'prop-types'
import ConfigureTopTen from './ConfigureTopTen'
import ToptenLists from './ToptenLists'
import {hasToken} from './../session/accessToken'

const Portail = ({toptenConfig, handleChange, saveConfig, toptens, ...props}) => {
    if (!hasToken()) {
        // @todo use useEffect
        props.initSession()
        return <div>init session...</div>
    }
    if (hasToken() && !toptens) {
        // @todo use useEffect
        props.loadToptenList()
        return (<div>load list...</div>)
    }
    return (
    <div>
        <ConfigureTopTen toptenConfig={toptenConfig} handleChange={handleChange(toptenConfig)} save={saveConfig(toptenConfig)}/>
        {toptens && toptens.length > 0 && <div>OR</div>}
        {toptens && toptens.length > 0 && <ToptenLists toptens={toptens}/>}
    </div>
    )
}

Portail.propTypes = {
    toptenConfig: PropTypes.object.isRequired,
    hasSession: PropTypes.bool.isRequired,
    handleChange: PropTypes.func.isRequired,
    saveConfig: PropTypes.func.isRequired,
    initSession: PropTypes.func.isRequired,
    loadToptenList: PropTypes.func.isRequired,
}

export default Portail