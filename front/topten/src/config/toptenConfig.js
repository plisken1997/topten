import {withHost} from './env'

export default {
    cardspool: {
        path: withHost('cardspool')
    },
    addCard: {
        path: cardsPoolId => withHost(`cardspool/${cardsPoolId}/addCard`)
    },
    dropCard: {
        path: (cardsPoolId, cardId) => withHost(`cardspool/${cardsPoolId}/delete/${cardId}`)
    },
    unpromote: {
        path: cardsPoolId => withHost(`cardspool/${cardsPoolId}/unPromote`)
    },
    promote: {
        path: cardsPoolId => withHost(`cardspool/${cardsPoolId}/promote`)
    },
    updateTop: {
        path: cardsPoolId => withHost(`cardspool/${cardsPoolId}/updateTop`)
    }
}
