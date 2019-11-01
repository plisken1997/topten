import React from 'react'
import './App.css'
import Portail from './enterApp/Portail'
import ToptenCards from './cards/ToptenCards'
import { Provider } from 'react-redux'
import { createStore } from 'redux'
import rootReducer from './reducers'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom"
const store = createStore(rootReducer)

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
