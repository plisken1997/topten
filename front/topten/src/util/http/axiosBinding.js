import axios from 'axios'

export const post = (path, payload, headers = {}) => 
    axios.post(
        path, 
        payload, 
        {
            'Content-Type': 'application/json', 
            ...headers
        }
    )
