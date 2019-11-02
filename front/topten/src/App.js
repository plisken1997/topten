import React from 'react'
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import thunkMiddleware from 'redux-thunk'
import { createLogger } from 'redux-logger'
import Portail from './hoc/connected/Portail'
import ToptenCards from './hoc/connected//ToptenCards'
import rootReducer from './reducers'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom"
import './App.css'

const loggerMiddleware = createLogger()
const store = createStore(rootReducer, applyMiddleware(thunkMiddleware, loggerMiddleware))

function App() {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <header className="App-header">
            <Link to="/">topten</Link>
          </header>
          <Switch>
            <Route path="/topten/:toptenId">
              <ToptenCards/>
            </Route>
            <Route path="/">
              <Portail/>
            </Route>
          </Switch>
        </div>
      </Router>
    </Provider>
  );
}

export default App;
