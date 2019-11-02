import {withHost} from './env'

test('withHost should yield the url with the base API host', () => {
    expect(withHost("path/to/resource")).toEqual('http://localhost:8080/path/to/resource')
})
