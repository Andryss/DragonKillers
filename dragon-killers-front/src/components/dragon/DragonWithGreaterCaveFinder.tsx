import {countDragonsWithGreaterCave, DragonDto, GreaterCaveRequest} from "../../services/DragonsService";
import {useState} from "react";

export const DragonWithGreaterCaveFinder = () => {

    const [treasures, setTreasures] = useState<string>("")
    
    const [dragons, setDragons] = useState<Array<DragonDto>>([])

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")

    const onFindClick = () => {
        const treasuresVal = parseFloat(treasures.trim())
        if (isNaN(treasuresVal)) {
            setError("number of treasures must be float")
            return
        }

        const request: GreaterCaveRequest = {
            numberOfTreasures: treasuresVal
        }

        setLoading(true)
        countDragonsWithGreaterCave(request, (dragonsList) => {
            setDragons(dragonsList.dragons)
            setError("")
            setLoading(false)
        }, (err) => {
            setDragons([])
            setError(err.message)
            setLoading(false)
        })
    }

    return (
        <>
            <div style={{display: "flex"}}>
                <input
                    type={"text"}
                    value={treasures}
                    onChange={(e) => setTreasures(e.target.value)}
                />
                <button onClick={onFindClick}>Find</button>
            </div>
            <table border={1}>
                <thead>
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>x</th>
                    <th>y</th>
                    <th>age</th>
                    <th>description</th>
                    <th>speaking</th>
                    <th>color</th>
                    <th>caveId</th>
                    <th>numberOfTreasures</th>
                </tr>
                </thead>
                <tbody>
                {loading && (<tr>
                    <td colSpan={10}>Loading...</td>
                </tr>)}
                {!loading && error !== "" && (<tr>
                    <td colSpan={10}>{error}</td>
                </tr>)}
                {!loading && error === "" && (
                    <>
                        {dragons.length === 0 && <tr>
                            <td colSpan={10}>No dragons found</td>
                        </tr>}
                        {dragons.map(dragon =>
                            <tr key={dragon.id}>
                                <td>{dragon.id}</td>
                                <td>{dragon.name}</td>
                                <td>{dragon.coordinates.x}</td>
                                <td>{dragon.coordinates.y}</td>
                                <td>{dragon.age}</td>
                                <td>{dragon.description}</td>
                                <td>{dragon.speaking ? "true" : "false"}</td>
                                <td>{dragon.color}</td>
                                <td>{dragon.cave?.id}</td>
                                <td>{dragon.cave?.numberOfTreasures}</td>
                            </tr>
                        )}
                    </>
                )}
                </tbody>
            </table>
        </>
    )
}