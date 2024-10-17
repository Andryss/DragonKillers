import {useState} from "react";

interface Props {
    onEqualSet: (_: boolean | null) => void,
}

export const BoolEq = (props: Props) => {
    const [selected, setSelected] = useState<string>("any")

    const parse = (str: string) => {
        if (str === "true") return true
        if (str === "false") return false
        return null
    }

    return (
        <>
            <select
                value={selected}
                onChange={(e) => {
                    setSelected(e.target.value)
                    props.onEqualSet(parse(e.target.value))
                }}
            >
                <option value="true">true</option>
                <option value="false">false</option>
                <option value="any" selected={true}>any</option>
            </select>
        </>
    )
}