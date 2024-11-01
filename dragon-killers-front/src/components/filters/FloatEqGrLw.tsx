import {useState} from "react";

interface Props {
    onEqualSet: (_: number | null) => void,
    onGreaterSet: (_: number | null) => void,
    onLowerSet: (_: number | null) => void,
}

export const FloatEqGrLw = (props: Props) => {
    const [equal, setEqual] = useState<string>("")
    const [greater, setGreater] = useState<string>("")
    const [lower, setLower] = useState<string>("")

    const isValidFloat = (str: string): boolean => {
        let digits = 0
        for (let char of str) {
            if (!((char >= "0" && char <= "9") || char === "." || char === "-")) return false
            if (char >= "0" && char <= "9") digits++
        }
        if (str.includes("-") && str.lastIndexOf("-") !== 0) return false
        if (str.indexOf(".") !== str.lastIndexOf(".")) return false
        return digits < 10
    }

    const parse = (str: string) => {
        const parsed = parseFloat(str)
        return (!isNaN(parsed) ? parsed : null)
    }

    return (
        <>
            <input
                type={"text"}
                value={equal}
                onChange={(e) => {
                    const str = e.target.value;
                    if (isValidFloat(str) || str === "") {
                        setEqual(str)
                        props.onEqualSet(parse(str))
                    }
                }}
                placeholder={"equal to"}
            />
            <input
                type={"text"}
                value={greater}
                onChange={(e) => {
                    const str = e.target.value;
                    if (isValidFloat(str) || str === "") {
                        setGreater(str)
                        props.onGreaterSet(parse(str))
                    }
                }}
                placeholder={"greater then"}
            />
            <input
                type={"text"}
                value={lower}
                onChange={(e) => {
                    const str = e.target.value;
                    if (isValidFloat(str) || str === "") {
                        setLower(str)
                        props.onLowerSet(parse(str))
                    }
                }}
                placeholder={"lower then"}
            />
        </>
    )
}