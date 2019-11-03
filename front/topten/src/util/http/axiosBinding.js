import axios from 'axios'

export const httpPost = (path, payload, headers = {}) => 
    axios.post(
        path, 
        payload, 
        {
            'Content-Type': 'application/json', 
            ...headers
        }
    )

export const httpDelete = (path, headers = {}) => axios.delete(path, {...headers})
