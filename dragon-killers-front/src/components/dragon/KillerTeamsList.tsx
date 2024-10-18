import {useState} from "react";
import {getKillerTeams, KillerTeamDto} from "../../services/DragonsService";

export const KillerTeamsList = () => {

    const [teams, setTeams] = useState<Array<KillerTeamDto>>([])

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")
    const onFetchClick = () => {
        setLoading(true)
        getKillerTeams( (response) => {
            setTeams(response.teams)
            setError("")
            setLoading(false)
        }, (err) => {
            setTeams([])
            setError(err.message)
            setLoading(false)
        })
    }

    return (
        <>
            <div>
                <label style={{fontSize: 20}}>Killers</label>
                <button onClick={onFetchClick}>Refresh</button>
                <table border={1}>
                    <thead>
                    <tr>
                        <th>id</th>
                        <th>name</th>
                        <th>size</th>
                        <th>caveId</th>
                    </tr>
                    </thead>
                    <tbody>
                    {loading && (<tr>
                        <td colSpan={4}>Loading...</td>
                    </tr>)}
                    {!loading && error !== "" && (<tr>
                        <td colSpan={4}>{error}</td>
                    </tr>)}
                    {!loading && error === "" && (
                        <>
                            {teams.length === 0 && <tr>
                                <td colSpan={4}>No teams found</td>
                            </tr>}
                            {teams.map(team =>
                                <tr key={team.id}>
                                    <td>{team.id}</td>
                                    <td>{team.name}</td>
                                    <td>{team.size}</td>
                                    <td>{team.caveId}</td>
                                </tr>
                            )}
                        </>
                    )}
                    </tbody>
                </table>
            </div>
        </>
    )
}