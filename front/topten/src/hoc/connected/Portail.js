import { connect } from 'react-redux'
import {toptenConfigChange, saveConfig, loadToptenList} from '../../components/enterApp/actions/enterApp'
import {initSession} from '../../components/session/authentication'
import Portail from '../../components/enterApp/Portail'
import {httpPost,httpGet} from '../../util/http/axiosBinding'

const mapStateToProps = state => ({
    toptenConfig: state.enterApp.toptenConfig,
    toptens: state.enterApp.toptens,
    hasSession: state.authentication.hasSession,
})

const saveToptenConfig = saveConfig(httpPost)

const mapDispatchToProps = dispatch => ({
    handleChange: obj => field => e => dispatch(toptenConfigChange(field, obj, e)),
    saveConfig: obj => () => {
        return dispatch(saveToptenConfig(obj))        
    },
    initSession: () => dispatch(initSession(httpGet)),
    loadToptenList: () => dispatch(loadToptenList(httpGet))
})

export default connect(mapStateToProps, mapDispatchToProps)(Portail)
