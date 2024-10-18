import {useState} from "react";
import {createKillerTeam} from "../../services/KillersService";

export const CreateKillerTeam = () => {

    const [id, setId] = useState<string>("")
    const [name, setName] = useState<string>("")
    const [size, setSize] = useState<string>("")
    const [cave, setCave] = useState<string>("")

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")

    const onCreateClicked = () => {
        const idVal = parseInt(id.trim())
        if (isNaN(idVal)) {
            setError("id must be integer")
            return
        }

        const nameVal = name.trim()
        if (nameVal === "") {
            setError("name must not be blank")
            return
        }

        const sizeVal = parseInt(size.trim())
        if (isNaN(sizeVal)) {
            setError("size must be integer")
            return
        }
        if (sizeVal < 1) {
            setError("size must be positive")
            return
        }

        const caveVal = parseInt(cave.trim())
        if (isNaN(caveVal)) {
            setError("cave must be integer")
            return
        }

        setError("")
        setLoading(true)
        createKillerTeam(idVal, nameVal, sizeVal, caveVal, () => {
            setId("")
            setName("")
            setSize("")
            setCave("")
            setError("")
            setLoading(false)
        }, (err) => {
            setError(err.message)
            setLoading(false)
        })
    }

    return (
        <>
            <label style={{fontSize: 20}}>Create killer team</label>
            <div>
                <label>Id</label>
                <input
                    type={"number"}
                    value={id}
                    onChange={(e) => setId(e.target.value)}
                />
            </div>
            <div>
                <label>Name</label>
                <input
                    type={"text"}
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </div>
            <div>
                <label>Size</label>
                <input
                    type={"number"}
                    value={size}
                    onChange={(e) => setSize(e.target.value)}
                />
            </div>
            <div>
                <label>Cave</label>
                <input
                    type={"number"}
                    value={cave}
                    onChange={(e) => setCave(e.target.value)}
                />
            </div>
            <button onClick={onCreateClicked} disabled={loading}>create</button>
            {error !== "" && (
                <div><label style={{color: "red"}}>{error}</label></div>
            )}
        </>
    )
}