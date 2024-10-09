import axios from "axios";

const commonPing = (url: string, onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    console.log(`Send GET ${url}`)
    axios.get(url)
        .then((response) => {
            console.log(`Received ${response.status} ${response.data}`)
            if (response.status === 200) onSuccess()
            else onFailure()
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            onException()
        })
}

export default commonPing;