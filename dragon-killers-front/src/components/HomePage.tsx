import {DragonPage} from "./dragon/DragonPage";
import {KillerPage} from "./killer/KillerPage";
import ServicesStatusBar from "./ServicesStatusBar";
import {DragonSoapPage} from "./dragon/soap/DragonSoapPage";

export const HomePage = () => {

    return (
        <>
            <ServicesStatusBar/>
            <hr/>
            <DragonPage/>
            <hr/>
            <KillerPage/>
            <hr/>
            <DragonSoapPage/>
        </>
    )
}