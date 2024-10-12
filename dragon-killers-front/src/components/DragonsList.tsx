import {useEffect, useState} from "react";
import {DragonDto, SearchDragonInfo, searchDragons} from "../services/DragonsService";

export const DragonsList = () => {

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")
    const [dragons, setDragons] = useState<Array<DragonDto>>([])

    const searchInfo: SearchDragonInfo = {
        pageNumber: 0,
        pageSize: 30,
        sortBy: "id",
        sortOrder: "asc"
    }

    const fetchDragons = () => {
        setLoading(true)
        searchDragons(searchInfo, (response) => {
            setDragons(response)
            setError("")
            setLoading(false)
        }, (err) => {
            setError(err.message)
            setLoading(false)
        })
    }

    useEffect(() => fetchDragons(), [])

    return (
        <>
            <div>
                <label style={{fontSize: 20}}>Dragons</label>
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
                            <th>cave</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading && (<tr><td colSpan={9}>Loading...</td></tr>)}
                        {!loading && error !== "" && (<tr><td colSpan={9}>{error}</td></tr>)}
                        {!loading && error === "" && (
                            <>
                                { dragons.length === 0 && <tr><td colSpan={9}>No dragons found</td></tr>}
                                { dragons.map(dragon =>
                                    <tr>
                                        <td>{dragon.id}</td>
                                        <td>{dragon.name}</td>
                                        <td>{dragon.coordinates.x}</td>
                                        <td>{dragon.coordinates.y}</td>
                                        <td>{dragon.age}</td>
                                        <td>{dragon.description}</td>
                                        <td>{dragon.speaking}</td>
                                        <td>{dragon.color}</td>
                                        <td>{dragon.cave?.id}</td>
                                    </tr>
                                ) }
                            </>
                        )}
                    </tbody>
                </table>
            </div>
        </>
    )
}