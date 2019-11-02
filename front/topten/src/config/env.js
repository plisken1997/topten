const domain = 'localhost'
const port = 8080
const host = `${domain}:${port}`
const isSecure = false

export const withHost = path => `http${isSecure ? 's' : ''}://${host}/${path}`
