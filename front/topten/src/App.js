import React from 'react';
import './App.css';
import ToptenCards from './container/ToptenCards';
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
        </header>
        <ToptenCards/>
      </div>
    </Provider>
  );
}

export default App;
