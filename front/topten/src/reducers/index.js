import { combineReducers } from 'redux'
import toptenCards from './toptenCards'
import enterApp from './enterApp'
import authentication from './authentication'

export default combineReducers({
  enterApp,
  toptenCards,
  authentication
})
