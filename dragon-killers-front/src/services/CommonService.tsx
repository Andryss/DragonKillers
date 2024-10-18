import axios from "axios";

export const commonPing = (url: string, onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    console.log(`Send GET ${url}`)
    axios.get(url)
        .then((response) => {
            console.log(`Received ${response.status} ${response.data}`)
            onSuccess()
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason)) {
                if (reason.response) onFailure()
                else onException()
            }
        })
}

export interface ErrorObject {
    message: string,
}