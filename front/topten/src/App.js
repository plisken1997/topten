import React from 'react';
import './App.css';
import ToptenCards from './cards/ToptenCards';
import { Provider } from 'react-redux'
import { createStore } from 'redux'
import rootReducer from './reducers'

const store = createStore(rootReducer)

function App() {
  return (
    <Provider store={store}>
      <div className="App">
        <header className="App-header">
          topten
          <div>
            <button>cancel last</button>
            <button>clear all</button>
          </div>
        </header>
        <ToptenCards/>
      </div>
    </Provider>
  );
}

export default App;
