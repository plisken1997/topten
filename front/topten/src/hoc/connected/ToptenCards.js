import { connect } from 'react-redux'
import ToptenCards from '../../components/cards/ToptenCards'
import {onDragEnd, addCard, newCardChange, unpromote, dropCard} from '../../components/cards/actions/toptenCards'
import {getConfig} from '../../reducers/enterApp'
import {httpPost, httpDelete} from '../../util/http/axiosBinding'

const mapStateToProps = (state, {routerParams = {}}) => {
    const {params} = routerParams
    const {toptenId} = params

    return {
        cardsPool: state.toptenCards.cardsPool,
        highlighted: state.toptenCards.highlighted,
        newCard: state.toptenCards.newCard,
        toptenConfig: getConfig(state.enterApp.toptens, toptenId)
    }
}

const saveAddCard = addCard(httpPost)
const saveDropCard = dropCard(httpDelete)
const saveUnpromote = unpromote(httpPost)
const saveListChange = onDragEnd(httpPost)

const mapDispatchToProps = dispatch => ({
    newCardChange: obj => field => e => dispatch(newCardChange(field, obj, e)),
    onDragEnd: (highlightedInput, cardsPoolInput, cardsPoolId) => (result) => dispatch(saveListChange(highlightedInput, cardsPoolInput, cardsPoolId, result)),
    addCard: (newCard, cardsPoolId) => () => dispatch(saveAddCard(newCard, cardsPoolId)),
    unpromote: cardsPoolId => id => () => dispatch(saveUnpromote(id, cardsPoolId)),
    dropCard: cardsPoolId => id => () => dispatch(saveDropCard(id, cardsPoolId)),
})

export default connect(mapStateToProps, mapDispatchToProps)(ToptenCards)
