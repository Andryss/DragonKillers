import {useState} from "react";

interface Props {
    onEqualSet: (_: string | null) => void,
}

export const StrEq = (props: Props) => {
    const [equal, setEqual] = useState<string>("")

    const parse = (str: string) => {
        return (str.length !== 0 ? str : null)
    }

    return (
        <>
            <input
                type={"text"}
                value={equal}
                onChange={(e) => {
                    setEqual(e.target.value)
                    props.onEqualSet(parse(e.target.value))
                }}
                placeholder={"equal to"}
            />
        </>
    )
}