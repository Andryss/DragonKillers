import axios from "axios";
import {ErrorObject} from "./CommonService";
import {XMLParser} from "fast-xml-parser";

const drgSoapUrl = "https://localhost:10010/ws"

const soapReqConfig = {headers: {"Content-Type": "text/xml"}};

export const drsPing = (onSuccess: () => void, onFailure: () => void, onException: () => void) => {
    const bodyStr = "" +
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
        "                  xmlns:gs=\"http://dragons.andryss.ru/soap/gen\">\n" +
        "    <soapenv:Header/>\n" +
        "    <soapenv:Body>\n" +
        "        <gs:pingRequest>\n" +
        "            <gs:message>ping</gs:message>\n" +
        "        </gs:pingRequest>\n" +
        "    </soapenv:Body>\n" +
        "</soapenv:Envelope>"
    console.log(`Send POST ${drgSoapUrl} body\n${bodyStr}`)
    axios.post(drgSoapUrl, bodyStr, soapReqConfig)
        .then((response) => {
            console.log(`Received ${response.status}\n${response.data}`)
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

export const drsCountDragonsByColor = (color: string, onSuccess: (response: number) => void, onFailure: (err: ErrorObject) => void) => {
    const bodyStr = "" +
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
        "                  xmlns:gs=\"http://dragons.andryss.ru/soap/gen\">\n" +
        "    <soapenv:Header/>\n" +
        "    <soapenv:Body>\n" +
        "        <gs:countDragonsByColorRequest>\n" +
        `            <gs:color>${color}</gs:color>\n` +
        "        </gs:countDragonsByColorRequest>\n" +
        "    </soapenv:Body>\n" +
        "</soapenv:Envelope>\n"
    console.log(`Send POST ${drgSoapUrl} body\n${bodyStr}`)
    axios.post(drgSoapUrl, bodyStr, soapReqConfig)
        .then((response) => {
            console.log(`Received: ${response.status}\n${response.data}`)
            const parsed = new DOMParser().parseFromString(response.data, "text/xml")
            console.log(parsed)
            const countStr = parsed.getElementsByTagNameNS("*", "count")[0].textContent
            if (countStr) {
                onSuccess(parseInt(countStr))
            }
        })
        .catch((reason) => {
            console.log(`Caught ${reason}`)
            if (axios.isAxiosError(reason) && reason.response) {
                const parsed = new XMLParser().parse(reason.response.data)
                onFailure(parsed.ErrorObject)
            }
        })
}