import commonPing from "./CommonService";

const krlBaseUrl = "http://localhost:9090/killers-service-1.0-SNAPSHOT"

export const klrPing = (onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    commonPing(`${krlBaseUrl}/ping`, onSuccess, onFailure, onException);
}