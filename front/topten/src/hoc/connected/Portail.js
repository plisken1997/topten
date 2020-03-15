import { connect } from 'react-redux'
import {toptenConfigChange, saveConfig} from '../../components/enterApp/actions/enterApp'
import {initSession} from '../../components/session/authentication'
import Portail from '../../components/enterApp/Portail'
import {httpPost,httpGet} from '../../util/http/axiosBinding'
//import {httpPost} from '../../util/http/fakeHttp'

const mapStateToProps = state => ({
    toptenConfig: state.enterApp.toptenConfig,
    toptens: state.enterApp.toptens,
    hasSession: state.authentication.hasSession,
})

//const saveToptenConfig = saveConfig(httpPost({data:{id:'5c7eaa0c-35c4-4843-adc8-d759e9e6cc24'}}))
const saveToptenConfig = saveConfig(httpPost)

const mapDispatchToProps = dispatch => ({
    handleChange: obj => field => e => dispatch(toptenConfigChange(field, obj, e)),
    saveConfig: obj => () => {
        return dispatch(saveToptenConfig(obj))        
    },
    initSession: () => dispatch(initSession(httpGet)())
})

export default connect(mapStateToProps, mapDispatchToProps)(Portail)
