import { connect } from 'react-redux'
import {toptenConfigChange, saveConfig} from '../../components/enterApp/actions/enterApp'
import Portail from '../../components/enterApp/Portail'
import {post} from '../../util/http/axiosBinding'

const mapStateToProps = state => ({
    toptenConfig: state.enterApp.toptenConfig,
    toptens: state.enterApp.toptens
})

const saveToptenConfig = saveConfig(post)

const mapDispatchToProps = dispatch => ({
    handleChange: obj => field => e => dispatch(toptenConfigChange(field, obj, e)),
    saveConfig: obj => () => {
        return dispatch(saveToptenConfig(obj))        
    },
})

export default connect(mapStateToProps, mapDispatchToProps)(Portail)
