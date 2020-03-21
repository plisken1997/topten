const getToken = () => localStorage.getItem('access_token')

const hasToken = () => !!getToken()

export { hasToken }
