import {useEffect, useState} from "react";
import drgPing from "../services/DragonsService";
import klrPing from "../services/KillersService";

const HomePage = () => {

    const [drgState, setDrgState] = useState<number>(-1)

    useEffect(() => {
        const interval = setInterval(() => {
            drgPing(
                () => setDrgState(1),
                () => setDrgState(0),
                () => setDrgState(-1)
            )
        }, 5000)
        return () => clearInterval(interval)
    })

    const [klrState, setKrlState] = useState<number>(-1)

    useEffect(() => {
        const interval = setInterval(() => {
            klrPing(
                () => setKrlState(1),
                () => setKrlState(0),
                () => setKrlState(-1)
            )
        }, 5000)
        return () => clearInterval(interval)
    })

    const stateColor = (state: number): string => {
        switch (state) {
            case 0:
                return "red"
            case 1:
                return "green"
        }
        return "gray"
    }

    return (
        <>
            <div style={{display: "flex", alignItems: "center"}}>
                <div style={{backgroundColor: stateColor(drgState), width: 10, height: 10, borderRadius: 5}}/>
                <div>DRG</div>
            </div>
            <div style={{display: "flex", alignItems: "center"}}>
                <div style={{backgroundColor: stateColor(klrState), width: 10, height: 10, borderRadius: 5}}/>
                <div>KRL</div>
            </div>
        </>
    )
}

export default HomePage;