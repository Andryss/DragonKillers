import {DragonDto} from "../services/DragonsService";

interface DragonItemProps {
    dragon: DragonDto
}

export const DragonItem = (props: DragonItemProps) => {
    return (
        <>
            <div style={{display: "flex", alignItems: "center"}}>
                <label>{props.dragon.id}</label>
                <label>{props.dragon.name}</label>
                <label>{props.dragon.description}</label>
            </div>
        </>
    )
}