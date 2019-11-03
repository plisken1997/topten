export const post = expected => (path, payload) => new Promise((resolve, reject) => {
    resolve(expected)
})

export const httpDelete = expected => (path) => new Promise((resolve, reject) => {
    resolve(expected)
})
