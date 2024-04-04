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
    if (!obj) {
        return "";
    }
    else if (Array.isArray(obj)) {
        return obj.map(ele=>flatten(ele));
    } else if (obj && typeof obj === 'object') {
        let res = {};
        for(const key in obj) {
            const flattenChild = flatten(obj[key]);
            res = {...res, ...helper(key, flattenChild)};
        }
        return res;
    } else {
        return obj;
    }
}

function helper(prefix, obj) {
    if (Array.isArray(obj)) {
        return {[prefix]: obj};
    } else if (obj && typeof obj === 'object') {
        const res = {};
        for(const key in obj) {
            res[`${prefix}_${key}`] = obj[key];
        }
        return res;
    } else {
        return {[prefix]: obj};
    }
}