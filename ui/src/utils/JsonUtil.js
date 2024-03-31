export function unflatten(obj) {
    if (Array.isArray(obj)) {
        return obj.map(ele=>unflatten(ele));
    } else if (typeof obj === 'object') {
        const res = {};
        for (const key in obj) {
            const keyArray = key.split("_");
            let cur = res;
            for (let i = 0; i < keyArray.length; i++) {
                const k = keyArray[i];
                if (i < keyArray.length - 1) {
                    cur[k] = cur[k] || {};
                    cur = cur[k];
                } else {
                    cur[k] = obj[key];
                }
            }
        }
        return res;
    } else {
        return obj;
    }
}

export function flatten(obj) {
    if (Array.isArray(obj)) {
        return obj.map(ele=>flatten(ele));
    } else if (typeof obj === 'object') {
        const res = {};
        for(const key in obj) {
            
        }
        return res;
    } else {
        return obj;
    }
}