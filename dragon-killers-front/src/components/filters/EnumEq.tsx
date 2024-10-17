import {useState} from "react";

interface Props {
    values: string[],
    onEqualSet: (_: string | null) => void,
}

export const EnumEq = (props: Props) => {
    const [selected, setSelected] = useState<string>("any")

    const parse = (str: string) => {
        if (props.values.includes(str)) return str
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
                {props.values.map((value) =>
                    <option value={value}>{value}</option>
                )}
                <option value="any" selected={true}>any</option>
            </select>
        </>
    )
}