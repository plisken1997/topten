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
            <div>
                <h3>affichage</h3>
                <label htmlFor="topten-config-display-asc">
                    <input checked={toptenConfig.display === "asc"} type="radio" name="topten-config-display" id="topten-config-display-asc" value="asc" step="2" onChange={handleChange("display")}/>croissant
                </label>
                <label htmlFor="topten-config-display-desc">
                    <input checked={toptenConfig.display === "desc"} type="radio" name="topten-config-display" id="topten-config-display-desc" value="desc" step="3" onChange={handleChange("display")}/>décroissant
                </label>
            </div>
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
