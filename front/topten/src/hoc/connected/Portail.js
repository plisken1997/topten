import { connect } from 'react-redux'
import {toptenConfigChange, saveConfig} from '../../components/enterApp/actions/enterApp'
import Portail from '../../components/enterApp/Portail'
//import {post} from '../../util/http/axiosBinding'
import {post} from '../../util/http/fakeHttp'

const mapStateToProps = state => ({
    toptenConfig: state.enterApp.toptenConfig,
    toptens: state.enterApp.toptens
})

const saveToptenConfig = saveConfig(post({data:{id:'5c7eaa0c-35c4-4843-adc8-d759e9e6cc24'}}))

const mapDispatchToProps = dispatch => ({
    handleChange: obj => field => e => dispatch(toptenConfigChange(field, obj, e)),
    saveConfig: obj => () => {
        return dispatch(saveToptenConfig(obj))        
    },
})

export default connect(mapStateToProps, mapDispatchToProps)(Portail)
