import {DescriptionInfo, groupDragonsByDescription} from "../../services/DragonsService";
import {useState} from "react";

export const DragonByDescriptionGrouper = () => {

    const [groups, setGroups] = useState<Array<DescriptionInfo>>([])

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")

    const onGroupClick = () => {
        setLoading(true)
        groupDragonsByDescription((response) => {
            setGroups(response.descriptions)
            setError("")
            setLoading(false)
        }, (err) => {
            setGroups([])
            setError(err.message)
            setLoading(false)
        })
    }

    return (
        <>
            <div style={{display: "flex"}}>
                <button onClick={onGroupClick}>Groups</button>
            </div>
            <table border={1}>
                <thead>
                <tr>
                    <th>description</th>
                    <th>count</th>
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
                        {groups.length === 0 && <tr>
                            <td colSpan={2}>No groups found</td>
                        </tr>}
                        {groups.map(group =>
                            <tr key={group.description}>
                                <td>{group.description}</td>
                                <td>{group.count}</td>
                            </tr>
                        )}
                    </>
                )}
                </tbody>
            </table>
        </>
    )
}