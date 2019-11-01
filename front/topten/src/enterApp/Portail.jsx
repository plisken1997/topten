import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { Link } from "react-router-dom"
import {toptenConfigChange, saveConfig} from './actions/enterApp'

const ConfigureTopTen = ({toptenConfig = {name: "", slots: 10}, handleChange, save, ...props}) => (
    <div>
        <form onSubmit={e => e.preventDefault()}>
            <input type="test" value={toptenConfig.name} placeholder="topten name" id="topten-config-name" onChange={handleChange("name")}/>
            <input type="number" step="1" value={toptenConfig.slots} placeholder="highlight slots" id="topten-config-slots" onChange={handleChange("slots")}/>
            <input type="submit" value="ok" id="topten-config-submit" onClick={save}/>
        </form>
    </div>
)

ConfigureTopTen.propTypes = {
    toptenConfig: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
    save: PropTypes.func.isRequired,
}

const UserLists = ({toptens}) => (
    <div>
        <h3>Pick an existing list</h3>
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
