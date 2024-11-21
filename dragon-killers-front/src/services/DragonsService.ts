import {commonPing, ErrorObject} from "./CommonService";
import axios from "axios";
import {Absent, parse} from "js2xmlparser";
import {XMLParser} from "fast-xml-parser";

const drgBaseUrl = "https://localhost:8443"

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

export interface CreateCoordinates {
    x: number,
    y: number,
}

export interface CreateDragonCave {
    numberOfTreasures: number,
}

export interface CreateDragonRequest {
    name: string,
    coordinates: CreateCoordinates,
    age: number | null,
    description: string,
    speaking: boolean,
    color: string,
    cave: CreateDragonCave | null
}

export const dragonColors = ["YELLOW", "ORANGE", "WHITE", "BROWN"]

const parseOptions = {
    typeHandlers: {
        "[object Null]": (_: any) => Absent.instance
    }
}

const xmlReqBodyConf = { headers: {'Content-Type': 'application/xml'} }

export const createDragon = (request: CreateDragonRequest, onSuccess: () => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons`
    const bodyStr = parse("CreateDragonRequest", request, parseOptions)
    console.log(`Send POST ${url} body\n${bodyStr}`)
    axios.post(url, bodyStr, xmlReqBodyConf)
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

export const updateDragon = (id: number, request: CreateDragonRequest, onSuccess: () => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons/${id}`
    const bodyStr = parse("CreateDragonRequest", request, parseOptions)
    console.log(`Send PUT ${url} body\n${bodyStr}`)
    axios.put(url, bodyStr, xmlReqBodyConf)
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

export const deleteDragon = (id: number, onSuccess: () => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons/${id}`
    console.log(`Send DELETE ${url}`)
    axios.delete(url)
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

export const searchDragons = (searchInfo: SearchDragonInfo, onSuccess: (response: DragonsList) => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons:search`
    const bodyStr = parse("SearchDragonInfo", searchInfo, parseOptions)
    console.log(`Send POST ${url} body\n${bodyStr}`)
    axios.post(url, bodyStr, xmlReqBodyConf)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new XMLParser({isArray: tagName => tagName === "dragons"}).parse(response.data)
            onSuccess(parsed.DragonsList === "" ? { dragons: [] } : parsed.DragonsList)
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}

export const countDragonsByColor = (color: string, onSuccess: (response: number) => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons:countByColor`
    console.log(`Send POST ${url} color ${color}`)
    const params = {params: {color: color}}
    axios.post(url, undefined, params)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new XMLParser().parse(response.data)
            onSuccess(parsed.Integer)
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}

export interface GreaterCaveRequest {
    numberOfTreasures: number,
}

export const countDragonsWithGreaterCave = (request: GreaterCaveRequest, onSuccess: (response: DragonsList) => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons:countWithGreaterCave`
    const bodyStr = parse("GreaterCaveRequest", request, parseOptions)
    console.log(`Send POST ${url} body\n${bodyStr}`)
    axios.post(url, bodyStr, xmlReqBodyConf)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new XMLParser({isArray: tagName => tagName === "dragons"}).parse(response.data)
            onSuccess(parsed.DragonsList === "" ? { dragons: [] } : parsed.DragonsList)
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}

export interface DescriptionInfo {
    description: string,
    count: number,
}

export interface GroupByDescriptionResponse {
    descriptions: DescriptionInfo[],
}

export const groupDragonsByDescription = (onSuccess: (response: GroupByDescriptionResponse) => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/dragons:groupByDescription`
    console.log(`Send POST ${url}`)
    axios.post(url)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new XMLParser({isArray: tagName => tagName === "descriptions"}).parse(response.data)
            onSuccess(parsed.GroupByDescriptionResponse === "" ? { descriptions: [] } : parsed.GroupByDescriptionResponse)
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}

export interface KillerTeamDto {
    id: number,
    name: string,
    size: number,
    caveId: number,
}


export interface KillerTeamDtoList {
    teams: KillerTeamDto[],
}

export const getKillerTeams = (onSuccess: (response: KillerTeamDtoList) => void, onFailure: (err: ErrorObject) => void) => {
    const url = `${drgBaseUrl}/killers`
    console.log(`Send GET ${url}`)
    axios.get(url)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new XMLParser({isArray: tagName => tagName === "teams"}).parse(response.data)
            onSuccess(parsed.KillerTeamDtoList === "" ? { teams: [] } : parsed.KillerTeamDtoList)
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}