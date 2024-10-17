import {useState} from "react";

interface Props {
    onEqualSet: (_: number | null) => void,
    onGreaterSet: (_: number | null) => void,
    onLowerSet: (_: number | null) => void,
}

export const IntEqGrLw = (props: Props) => {
    const [equal, setEqual] = useState<string>("")
    const [greater, setGreater] = useState<string>("")
    const [lower, setLower] = useState<string>("")

    const parse = (str: string) => {
        const parsed = parseInt(str)
        return (parsed && !isNaN(parsed) ? parsed : null)
    }

    return (
        <>
            <input
                type={"number"}
                value={equal}
                onChange={(e) => {
                    setEqual(e.target.value)
                    props.onEqualSet(parse(e.target.value))
                }}
                placeholder={"equal to"}
            />
            <input
                type={"number"}
                value={greater}
                onChange={(e) => {
                    setGreater(e.target.value)
                    props.onGreaterSet(parse(e.target.value))
                }}
                placeholder={"greater then"}
            />
            <input
                type={"number"}
                value={lower}
                onChange={(e) => {
                    setLower(e.target.value)
                    props.onLowerSet(parse(e.target.value))
                }}
                placeholder={"lower then"}
            />
        </>
    )
}