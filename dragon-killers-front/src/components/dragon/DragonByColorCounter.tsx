import {countDragonsByColor, dragonColors} from "../../services/DragonsService";
import {useState} from "react";

export const DragonByColorCounter = () => {

    const [color, setColor] = useState<string>("")

    const [count, setCount] = useState<number | null>(null)

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")

    const onCountClick = () => {
        if (!dragonColors.includes(color)) {
            setError("invalid color")
            return
        }

        setLoading(true)
        countDragonsByColor(color, (cnt) => {
            setCount(cnt)
            setError("")
            setLoading(false)
        }, (err) => {
            setCount(null)
            setError(err.message)
            setLoading(false)
        })
    }

    return (
        <div style={{display: "flex"}}>
            <select
                value={color}
                onChange={(e) => setColor(e.target.value)}
            >
                <option value={""}></option>
                {dragonColors.map((value) =>
                    <option key={value} value={value}>{value}</option>
                )}
            </select>
            <button onClick={onCountClick}>Count</button>
            {loading && (<label>Loading...</label>)}
            {!loading && error !== "" && (<label style={{color: "red"}}>{error}</label>)}
            {!loading && error === "" && count && (<label>{count}</label>)}
        </div>
    )
}