import {DragonPage} from "./dragon/DragonPage";
import {KillerPage} from "./killer/KillerPage";
import ServicesStatusBar from "./ServicesStatusBar";

export const HomePage = () => {

    return (
        <>
            <ServicesStatusBar/>
            <hr/>
            <DragonPage/>
            <hr/>
            <KillerPage/>
        </>
    )
}