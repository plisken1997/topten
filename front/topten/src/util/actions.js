export const updateValue = (actionType) => (field, obj, e) => {
    return {
        type: actionType,
        payload: {...obj, [field]: e.target.value}
    }
}
