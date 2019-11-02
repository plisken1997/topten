export const post = expected => (path, payload) => new Promise((resolve, reject) => {
    resolve(expected)
})
