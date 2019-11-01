import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import {toptenConfigChange, saveConfig} from './actions/enterApp'
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

const mapStateToProps = state => ({
    toptenConfig: state.enterApp.toptenConfig,
    toptens: state.enterApp.toptens
})

const mapDispatchToProps = dispatch => ({
    handleChange: obj => field => e => dispatch(toptenConfigChange(field, obj, e)),
    saveConfig: obj => () => {
        return dispatch(saveConfig(obj))        
    },
})

export default connect(mapStateToProps, mapDispatchToProps)(Portail)
