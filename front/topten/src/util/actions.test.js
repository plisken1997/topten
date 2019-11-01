import {updateValue} from './actions'
import expectExport from 'expect'

test("should update a givent object", () => {
    const actionType = "TEST_ACTION_TYPE"
    const action = updateValue(actionType)

    const baseObject = {
        name: "test name",
        value: "test value",
        position: 3
    }
    const event = {
        target: {
            value: "new test name"
        }
    }
    const expected = {
        type: actionType,
        payload: {
            name: "new test name",
            value: "test value",
            position: 3
        }
        
    }
    const res = action("name", baseObject, event)
    expect(res).toEqual(expected)
})
