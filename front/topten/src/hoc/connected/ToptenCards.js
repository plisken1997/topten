import { connect } from 'react-redux'
import ToptenCards from '../../components/cards/ToptenCards'
import {onDragEnd, addCard, newCardChange, unpromote, dropCard} from '../../components/cards/actions/toptenCards'
import {getConfig} from '../../reducers/enterApp'

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

const mapDispatchToProps = dispatch => ({
    onDragEnd: (highlightedInput, cardsPoolInput) => (result) => dispatch(onDragEnd(highlightedInput, cardsPoolInput, result)),
    addCard: newCard => () => dispatch(addCard(newCard)),
    newCardChange: obj => field => e => dispatch(newCardChange(field, obj, e)),
    unpromote: id => () => dispatch(unpromote(id)),
    dropCard: id => () => dispatch(dropCard(id)),
})

export default connect(mapStateToProps, mapDispatchToProps)(ToptenCards)
