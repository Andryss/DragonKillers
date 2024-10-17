import commonPing from "./CommonService";
import axios from "axios";
import {parse} from "js2xmlparser";
import {XMLParser} from "fast-xml-parser";

const drgBaseUrl = "http://localhost:8080/dragons_service_war"

export const drgPing = (onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    commonPing(`${drgBaseUrl}/ping`, onSuccess, onFailure, onException);
}

export interface IntFilter {
    eq: number | null,
    lw: number | null,
    gr: number | null,
}

export interface StringFilter {
    eq: string | null,
}

export interface FloatFilter {
    eq: number | null,
    lw: number | null,
    gr: number | null,
}

export interface CoordinatesFilter {
    x: FloatFilter,
    y: FloatFilter,
}

export interface DateFilter {
    eq: string | null,
    lw: string | null,
    gr: string | null,
}

export interface BooleanFilter {
    eq: boolean | null,
}

export interface DragonCaveFilter {
    id: IntFilter,
    numberOfTreasures: FloatFilter,
}

export interface DragonFilter {
    id: IntFilter,
    name: StringFilter,
    coordinates: CoordinatesFilter,
    creationDate: DateFilter,
    age: IntFilter,
    description: StringFilter,
    speaking: BooleanFilter,
    color: StringFilter,
    cave: DragonCaveFilter,
}

export interface SearchDragonInfo {
    pageNumber: number,
    pageSize: number,
    sortBy: string,
    sortOrder: string,
    filter: DragonFilter,
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

export interface DragonsList {
    dragons: DragonDto[],
}

export const dragonColors = ["YELLOW", "ORANGE", "WHITE", "BROWN"]

export const searchDragons = (searchInfo: SearchDragonInfo, onSuccess: (response: DragonsList) => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons:search`
    const bodyStr = parse("SearchDragonInfo", searchInfo, {
        typeHandlers: {
            "[object Null]": (_: any): string => ""
        }
    })
    console.log(`Send POST ${url} body\n${bodyStr}`)
    const config = { headers: {'Content-Type': 'application/xml'} }
    axios.post(url, bodyStr, config)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new XMLParser({isArray: tagName => tagName === "dragons"}).parse(response.data)
            if (response.status === 200) onSuccess(parsed.DragonsList === "" ? { dragons: [] } : parsed.DragonsList)
            else onFailure(parsed.ErrorObject)
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
        })
}