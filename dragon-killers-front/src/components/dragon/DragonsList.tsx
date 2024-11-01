import {ChangeEvent, useEffect, useState} from "react";
import {
    BooleanFilter, deleteDragon, dragonColors,
    DragonDto,
    FloatFilter,
    IntFilter,
    SearchDragonInfo,
    searchDragons,
    StringFilter
} from "../../services/DragonsService";
import {IntEqGrLw } from "../filters/IntEqGrLw";
import {StrEq} from "../filters/StrEq";
import {FloatEqGrLw} from "../filters/FloatEqGrLw";
import {BoolEq} from "../filters/BoolEq";
import {EnumEq} from "../filters/EnumEq";

interface Props {
    onNew: () => void,
    onEdit: (_: DragonDto) => void,
    refresh: boolean,
}

export const DragonsList = (props: Props) => {

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")
    const [dragons, setDragons] = useState<Array<DragonDto>>([])

    const [prevEnabled, setPrevEnabled] = useState<boolean>(false)
    const [pageSizeStr, setPageSizeStr] = useState<string>("10")
    const [nextEnabled, setNextEnabled] = useState<boolean>(false)

    const [showFilters, setShowFilters] = useState<boolean>(false)

    const [searchInfo, setSearchInfo] = useState<SearchDragonInfo>({
        pageNumber: 0,
        pageSize: 10,
        sortBy: "id",
        sortOrder: "asc",
        filter: {
            id: { eq: null, gr: null, lw: null },
            name: { eq: null },
            coordinates: {
                x: { eq: null, gr: null, lw: null },
                y: { eq: null, gr: null, lw: null },
            },
            age: { eq: null, gr: null, lw: null },
            color: { eq: null },
            creationDate: { eq: null, gr: null, lw: null },
            description: { eq: null },
            speaking: { eq: null },
            cave: {
                id: { eq: null, gr: null, lw: null },
                numberOfTreasures: { eq: null, gr: null, lw: null }
            },
        },
    })

    const fetchDragons = (info: SearchDragonInfo) => {
        setLoading(true)
        searchDragons(info, (response) => {
            const dtos = response.dragons;
            setPrevEnabled(info.pageNumber > 0)
            setNextEnabled(dtos.length === info.pageSize)
            setDragons(dtos)
            setError("")
            setLoading(false)
        }, (err) => {
            setPrevEnabled(false)
            setNextEnabled(false)
            setError(err.message)
            setLoading(false)
        })
    }

    useEffect(() => {
        fetchDragons(searchInfo)
    }, [searchInfo, props.refresh])

    const onPrevClicked = () => {
        setSearchInfo((prev) => ({...prev, pageNumber: prev.pageNumber - 1}))
    }

    const onPageSizeStrChange = (e: ChangeEvent<HTMLInputElement>) => {
        const inputValue = e.target.value.slice(0, 2);
        if (inputValue.length > 0 && /^\d+$/.test(inputValue)) {
            const pageSize = parseInt(inputValue)
            if (pageSize > 0) setSearchInfo((prev) => ({...prev, pageSize: pageSize}))
        }
        setPageSizeStr(e.target.value)
    };

    const onNextClicked = () => {
        setSearchInfo((prev) => ({...prev, pageNumber: prev.pageNumber + 1}))
    }

    const onColumnSort = (field: string) => {
        const direction = (searchInfo.sortBy !== field ? "asc" : (searchInfo.sortOrder === "asc" ? "desc" : "asc"))
        setSearchInfo((prev) => ({...prev, sortBy: field, sortOrder: direction}))
    }

    const sortingSuffix = (field: string) => {
        if (searchInfo.sortBy !== field) return ""
        return searchInfo.sortOrder === "asc" ? " △" : " ▽"
    }

    const sortableColumnHeader = (name: string) => {
        return (<th onClick={() => onColumnSort(name)}>{name}{sortingSuffix(name)}</th>)
    }

    const setIntFilter = (extractor: (_: SearchDragonInfo) => IntFilter, type: keyof IntFilter, val: number | null) => {
        setSearchInfo((prev) => {
            const si = {...prev}
            extractor(si)[type] = val
            return si
        })
    }

    const setStrFilter = (extractor: (_: SearchDragonInfo) => StringFilter, type: keyof StringFilter, val: string | null) => {
        setSearchInfo((prev) => {
            const si = {...prev}
            extractor(si)[type] = val
            return si
        })
    }

    const setFloatFilter = (extractor: (_: SearchDragonInfo) => FloatFilter, type: keyof FloatFilter, val: number | null) => {
        setSearchInfo((prev) => {
            const si = {...prev}
            extractor(si)[type] = val
            return si
        })
    }

    const setBoolFilter = (extractor: (_: SearchDragonInfo) => BooleanFilter, type: keyof BooleanFilter, val: boolean | null) => {
        setSearchInfo((prev) => {
            const si = {...prev}
            extractor(si)[type] = val
            return si
        })
    }

    const setId = (type: keyof IntFilter, val: number | null) => {
        setIntFilter((sdi) => sdi.filter.id, type, val)
    }

    const setName = (type: keyof StringFilter, val: string | null) => {
        setStrFilter((sdi) => sdi.filter.name, type, val)
    }

    const setX = (type: keyof FloatFilter, val: number | null) => {
        setFloatFilter((sdi) => sdi.filter.coordinates.x, type, val)
    }

    const setY = (type: keyof FloatFilter, val: number | null) => {
        setFloatFilter((sdi) => sdi.filter.coordinates.y, type, val)
    }

    const setAge = (type: keyof IntFilter, val: number | null) => {
        setIntFilter((sdi) => sdi.filter.age, type, val)
    }

    const setDescription = (type: keyof StringFilter, val: string | null) => {
        setStrFilter((sdi) => sdi.filter.description, type, val)
    }

    const setSpeaking = (type: keyof BooleanFilter, val: boolean | null) => {
        setBoolFilter((sdi) => sdi.filter.speaking, type, val)
    }

    const setColor = (type: keyof StringFilter, val: string | null) => {
        setStrFilter((sdi) => sdi.filter.color, type, val)
    }

    const onDeleteClick = (dto: DragonDto) => {
        deleteDragon(dto.id, () => {
            fetchDragons(searchInfo)
        }, (err) => {
            setError(err.message)
        })
    }

    return (
        <>
            <div>
                <label style={{fontSize: 20}}>Dragons</label>
                <button onClick={props.onNew}>New</button>
                <button onClick={() => setShowFilters(!showFilters)}>{showFilters ? "Hide Filters" : "Show Filters"}</button>
                {showFilters && (
                    <>
                        <div style={{display: "flex"}}>
                            <label>id: </label>
                            <IntEqGrLw onEqualSet={(val: number | null) => setId("eq", val)}
                                onGreaterSet={(val: number | null) => setId("gr", val)}
                                onLowerSet={(val: number | null) => setId("lw", val)}/>
                        </div>
                        <div style={{display: "flex"}}>
                            <label>name: </label>
                            <StrEq onEqualSet={(val: string | null) => setName("eq", val)}/>
                        </div>
                        <div style={{display: "flex"}}>
                            <label>x: </label>
                            <FloatEqGrLw onEqualSet={(val: number | null) => setX("eq", val)}
                                onGreaterSet={(val: number | null) => setX("gr", val)}
                                onLowerSet={(val: number | null) => setX("lw", val)}/>
                        </div>
                        <div style={{display: "flex"}}>
                            <label>y: </label>
                            <FloatEqGrLw onEqualSet={(val: number | null) => setY("eq", val)}
                                onGreaterSet={(val: number | null) => setY("gr", val)}
                                onLowerSet={(val: number | null) => setY("lw", val)}/>
                        </div>
                        <div style={{display: "flex"}}>
                            <label>age: </label>
                            <IntEqGrLw onEqualSet={(val: number | null) => setAge("eq", val)}
                                onGreaterSet={(val: number | null) => setAge("gr", val)}
                                onLowerSet={(val: number | null) => setAge("lw", val)}/>
                        </div>
                        <div style={{display: "flex"}}>
                            <label>description: </label>
                            <StrEq onEqualSet={(val: string | null) => setDescription("eq", val)}/>
                        </div>
                        <div style={{display: "flex"}}>
                            <label>speaking: </label>
                            <BoolEq onEqualSet={(val: boolean | null) => setSpeaking("eq", val)}/>
                        </div>
                        <div style={{display: "flex"}}>
                            <label>color: </label>
                            <EnumEq values={dragonColors}
                                onEqualSet={(val: string | null) => setColor("eq", val)}/>
                        </div>
                    </>
                )}
                <table border={1}>
                    <thead>
                    <tr>
                        {sortableColumnHeader("id")}
                        {sortableColumnHeader("name")}
                        {sortableColumnHeader("x")}
                        {sortableColumnHeader("y")}
                        {sortableColumnHeader("age")}
                        {sortableColumnHeader("description")}
                        {sortableColumnHeader("speaking")}
                        {sortableColumnHeader("color")}
                        <th>caveId</th>
                        <th>numberOfTreasures</th>
                        <th>actions</th>
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
                                    <td>
                                        <button onClick={() => props.onEdit(dragon)}>Edit</button>
                                        <button onClick={() => onDeleteClick(dragon)}>Delete</button>
                                    </td>
                                </tr>
                            )}
                        </>
                    )}
                    </tbody>
                </table>
                <button onClick={onPrevClicked} disabled={!prevEnabled}>Prev</button>
                <input type={"text"} value={pageSizeStr} onChange={onPageSizeStrChange} />
                <button onClick={onNextClicked} disabled={!nextEnabled}>Next</button>
            </div>
        </>
    )
}