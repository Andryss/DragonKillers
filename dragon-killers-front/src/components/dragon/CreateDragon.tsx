import {useState} from "react";
import {
    createDragon,
    CreateDragonCave,
    CreateDragonRequest,
    dragonColors,
    DragonDto,
    updateDragon
} from "../../services/DragonsService";

interface Props {
    onSuccess: () => void,
    editable: DragonDto | null,
}

export const CreateDragon = (props: Props) => {
    const editDto = props.editable;

    const [name, setName] = useState<string>(editDto ? editDto.name.toString() : "")
    const [x, setX] = useState<string>(editDto ? editDto.coordinates.x.toString() : "")
    const [y, setY] = useState<string>(editDto ? editDto.coordinates.y.toString() : "")
    const [age, setAge] = useState<string>(editDto && editDto.age ? editDto.age.toString() : "")
    const [descr, setDescr] = useState<string>(editDto ? editDto.description.toString() : "")
    const [speaking, setSpeaking] = useState<boolean>(editDto ? editDto.speaking : false)
    const [color, setColor] = useState<string>(editDto ? editDto.color.toString() : "")
    const [treasures, setTreasures] = useState<string>(editDto && editDto.cave ? editDto.cave.numberOfTreasures.toString() : "")

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")

    const onCreateClicked = () => {
        const nameVal = name.trim()
        if (nameVal === "") {
            setError("name must not be blank")
            return
        }

        const xVal = parseFloat(x.trim())
        if (isNaN(xVal)) {
            setError("x must be float")
            return
        }
        if (xVal > 476) {
            setError("x must be <= 476")
            return
        }

        const yVal = parseFloat(y.trim())
        if (isNaN(yVal)) {
            setError("y must be float")
            return
        }
        if (yVal <= -486) {
            setError("x must be > -486")
            return
        }

        const ageParsed = parseInt(age.trim())
        if (age.trim() !== "" && isNaN(ageParsed)) {
            setError("age must be integer or empty")
            return
        }
        const ageVal = (!isNaN(ageParsed) ? ageParsed : null)
        if (ageVal && ageVal < 0) {
            setError("age must be positive")
            return
        }

        const descrVal = descr.trim()
        if (descrVal === "") {
            setError("description must not be blank")
            return
        }

        const speakingVal = speaking

        const colorVal = color.trim()
        if (colorVal === "") {
            setError("color must be selected")
            return
        }

        const treasuresParsed = parseFloat(treasures.trim())
        if (treasures.trim() !== "" && isNaN(treasuresParsed)) {
            setError("number of treasures must be float or empty")
            return
        }
        const treasuresVal = (!isNaN(treasuresParsed) ? treasuresParsed : null)

        const createCave: CreateDragonCave | null = (treasuresVal !== null ? {numberOfTreasures: treasuresVal} : null)
        const request: CreateDragonRequest = {
            name: nameVal,
            coordinates: {
                x: xVal,
                y: yVal
            },
            age: ageVal,
            description: descrVal,
            speaking: speakingVal,
            color: colorVal,
            cave: createCave
        }

        setError("")
        setLoading(true)

        if (editDto) {
            updateDragon(editDto.id, request, () => {
                props.onSuccess()
                setError("")
                setLoading(false)
            }, (err) => {
                setError(err.message)
                setLoading(false)
            })
        } else {
            createDragon(request, () => {
                props.onSuccess()
                setError("")
                setLoading(false)
            }, (err) => {
                setError(err.message)
                setLoading(false)
            })
        }
    }

    return (
        <>
            <h2>{props.editable ? "Edit dragon" : "Create dragon"}</h2>
            <table>
                <tr>
                    <td>Name</td>
                    <td>
                        <input
                            type={"text"}
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </td>
                </tr>
                <tr>
                    <td>X</td>
                    <td>
                        <input
                            type={"text"}
                            value={x}
                            onChange={(e) => setX(e.target.value)}
                        />
                    </td>
                </tr>
                <tr>
                    <td>Y</td>
                    <td>
                        <input
                            type={"text"}
                            value={y}
                            onChange={(e) => setY(e.target.value)}
                        />
                    </td>
                </tr>
                <tr>
                    <td>Age</td>
                    <td>
                        <input
                            type={"number"}
                            value={age}
                            onChange={(e) => setAge(e.target.value)}
                        />
                    </td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td>
                        <input
                            type={"text"}
                            value={descr}
                            onChange={(e) => setDescr(e.target.value)}
                        />
                    </td>
                </tr>
                <tr>
                    <td>Speaking</td>
                    <td>
                        <input
                            type={"checkbox"}
                            checked={speaking}
                            onChange={(e) => setSpeaking(e.target.checked)}
                        />
                    </td>
                </tr>
                <tr>
                    <td>Color</td>
                    <td>
                        <select
                            value={color}
                            onChange={(e) => setColor(e.target.value)}
                        >
                            <option value={""}></option>
                            {dragonColors.map((value) =>
                                <option key={value} value={value}>{value}</option>
                            )}
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Number of treasures</td>
                    <td>
                        <input
                            type={"text"}
                            value={treasures}
                            onChange={(e) => setTreasures(e.target.value)}
                        />
                    </td>
                </tr>
            </table>
            <button onClick={onCreateClicked} disabled={loading}>{props.editable ? "edit" : "create"}</button>
            {error !== "" && (
                <div><label style={{color: "red"}}>{error}</label></div>
            )}
        </>
    )
}