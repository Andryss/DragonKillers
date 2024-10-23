import axios from "axios";

const drgSoapUrl = "http://localhost:10010/ws"

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