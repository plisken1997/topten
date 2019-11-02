import {post} from './fakeHttp'

test('fake post method should return the provided value', () => {
    const expectedResult = {data: {id: '5c7eaa0c-35c4-4843-adc8-d759e9e6cc24'}}
    const emptyPayload = {}

    post(expectedResult)('localhost/path/to/resource', emptyPayload)
        .then(result => {
            expect(result).toEqual(expectedResult)
        })
})
