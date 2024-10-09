import commonPing from "./CommonService";

const drgBaseUrl = "http://localhost:8080/dragons_service_war"

const drgPing = (onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    commonPing(`${drgBaseUrl}/ping`, onSuccess, onFailure, onException);
}

export default drgPing;