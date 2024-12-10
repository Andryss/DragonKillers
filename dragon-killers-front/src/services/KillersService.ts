import {commonPing, ErrorObject} from "./CommonService";
import axios from "axios";
import {XMLParser} from "fast-xml-parser";

const krlBaseUrl = "https://localhost:9457"

export const klrPing = (onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    commonPing(`${krlBaseUrl}/ping`, onSuccess, onFailure, onException);
}

export const createKillerTeam = (id: number, name: string, size: number, startCave: number, onSuccess: () => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${krlBaseUrl}/killer/teams/create/${id}/${name}/${size}/${startCave}`
    console.log(`Send POST ${url}`)
    axios.post(url)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            onSuccess()
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}

export const moveKillerTeam = (id: number, cave: number, onSuccess: () => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${krlBaseUrl}/killer/team/${id}/move-to-cave/${cave}`
    console.log(`Send POST ${url}`)
    axios.post(url)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            onSuccess()
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}