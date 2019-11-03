import { combineReducers } from 'redux'
import toptenCards from './toptenCards'
import enterApp from './enterApp'

export default combineReducers({
  enterApp,
  toptenCards
})
