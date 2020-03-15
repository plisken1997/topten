import axios from 'axios'

const getToken = () => localStorage.getItem('access_token')

export const httpPost = (path, payload, headers = {}) =>
    axios.post(
        path, 
        payload, 
        {
            headers: {
                'Content-Type': 'application/json', 
                'Authorization': `Bearer ${getToken()}`,
                ...headers
            }
        }
    )

export const httpDelete = (path, headers = {}) => axios.delete(path, headers)

export const httpGet = (path, headers = {}) => axios.get(path, headers)
