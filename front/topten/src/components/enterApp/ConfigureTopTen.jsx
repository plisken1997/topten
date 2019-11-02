import React from 'react'
import PropTypes from 'prop-types'

const ConfigureTopTen = ({toptenConfig = {name: "", slots: 10}, handleChange, save, ...props}) => (
    <div>
        <h2>Create a new topten list</h2>
        <form onSubmit={e => e.preventDefault()}>
            <input type="test" value={toptenConfig.name} placeholder="topten name" id="topten-config-name" onChange={handleChange("name")}/>
            <br />
            <textarea value={toptenConfig.description} placeholder="description" id="topten-config-desc" onChange={handleChange("description")}/>
            <br />
            <input type="number" step="1" value={toptenConfig.slots} placeholder="highlight slots" id="topten-config-slots" onChange={handleChange("slots")}/>
            <br />
            <input type="submit" value="ok" id="topten-config-submit" onClick={save}/>
        </form>
    </div>
)

ConfigureTopTen.propTypes = {
    toptenConfig: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
    save: PropTypes.func.isRequired,
}

export default ConfigureTopTen
