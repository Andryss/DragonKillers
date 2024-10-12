import commonPing from "./CommonService";
import axios from "axios";
import {parse} from "js2xmlparser";
import {XMLParser} from "fast-xml-parser";

const drgBaseUrl = "http://localhost:8080/dragons_service_war"

export const drgPing = (onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    commonPing(`${drgBaseUrl}/ping`, onSuccess, onFailure, onException);
}

export interface SearchDragonInfo {
    pageNumber: number,
    pageSize: number,
    sortBy: string,
    sortOrder: string,
}

interface ErrorObject {
    message: string,
}

interface CoordinatesDto {
    x: number,
    y: number,
}

interface DragonCaveDto {
    id: number,
    numberOfTreasures: number,
}

export interface DragonDto {
    id: number,
    name: string,
    coordinates: CoordinatesDto,
    age: number | null,
    description: string,
    speaking: boolean,
    color: string,
    cave: DragonCaveDto | null,
}

export const searchDragons = (searchInfo: SearchDragonInfo, onSuccess: (response: DragonDto[]) => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons:search`
    const bodyStr = parse("SearchDragonInfo", searchInfo)
    console.log(`Send POST ${url} body\n${bodyStr}`)
    const config = { headers: {'Content-Type': 'application/xml'} }
    axios.post(url, bodyStr, config)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new XMLParser().parse(response.data)
            if (response.status === 200) onSuccess(parsed.DragonsList.dragons.dragons)
            else onFailure(parsed.ErrorObject)
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
        })
}