import {useState} from "react";
import {moveKillerTeam} from "../services/KillersService";

export const KillerTeamMover = () => {

    const [id, setId] = useState<string>("")
    const [cave, setCave] = useState<string>("")

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")

    const onMoveClick = () => {
        const idVal = parseInt(id.trim())
        if (isNaN(idVal)) {
            setError("id must be integer")
            return
        }

        const caveVal = parseInt(cave.trim())
        if (isNaN(caveVal)) {
            setError("cave must be integer")
            return
        }

        setLoading(true)
        moveKillerTeam(idVal, caveVal, () => {
            setId("")
            setCave("")
            setError("")
            setLoading(false)
        }, (err) => {
            setError(err.message)
            setLoading(false)
        })
    }

    return (
        <div style={{display: "flex"}}>
            <input
                type={"number"}
                value={id}
                onChange={(e) => setId(e.target.value)}
                placeholder={"team id"}
            />
            <input
                type={"number"}
                value={cave}
                onChange={(e) => setCave(e.target.value)}
                placeholder={"cave id"}
            />
            <button onClick={onMoveClick} disabled={loading}>Move</button>
            {error !== "" && (
                <div><label style={{color: "red"}}>{error}</label></div>
            )}
        </div>
    )
}