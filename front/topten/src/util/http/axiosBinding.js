import axios from 'axios'

const getToken = () => localStorage.getItem('access_token')

const addCustomHeaders = (headers) => ({
    'Content-Type': 'application/json', 
    'Authorization': `Bearer ${getToken()}`,
    ...headers
})

export const httpPost = (path, payload, headers = {}) =>
    axios.post(
        path, 
        payload, 
        {headers: addCustomHeaders(headers)}
    )

export const httpDelete = (path, headers = {}) => axios.delete(path, {headers: addCustomHeaders(headers)})

export const httpGet = (path, headers = {}) => axios.get(path, {headers: addCustomHeaders(headers)})
