import {CreateKillerTeam} from "./CreateKillerTeam";
import {KillerTeamMover} from "./KillerTeamMover";

export const KillerPage = () => {

    return (
        <>
            <h2>Killer service</h2>
            <CreateKillerTeam/>
            <br/><br/>
            <KillerTeamMover/>
        </>
    )
}