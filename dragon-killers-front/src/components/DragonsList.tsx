import {ChangeEvent, useEffect, useState} from "react";
import {DragonDto, SearchDragonInfo, searchDragons} from "../services/DragonsService";

export const DragonsList = () => {

    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string>("")
    const [dragons, setDragons] = useState<Array<DragonDto>>([])

    const [prevEnabled, setPrevEnabled] = useState<boolean>(false)
    const [pageSizeStr, setPageSizeStr] = useState<string>("10")
    const [nextEnabled, setNextEnabled] = useState<boolean>(false)

    const [searchInfo, setSearchInfo] = useState<SearchDragonInfo>({
        pageNumber: 0,
        pageSize: 10,
        sortBy: "id",
        sortOrder: "asc"
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
    }, [searchInfo])

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

    return (
        <>
            <div>
                <label style={{fontSize: 20}}>Dragons</label>
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
                        <th>cave</th>
                    </tr>
                    </thead>
                    <tbody>
                    {loading && (<tr>
                        <td colSpan={9}>Loading...</td>
                    </tr>)}
                    {!loading && error !== "" && (<tr>
                        <td colSpan={9}>{error}</td>
                    </tr>)}
                    {!loading && error === "" && (
                        <>
                            {dragons.length === 0 && <tr>
                                <td colSpan={9}>No dragons found</td>
                            </tr>}
                            {dragons.map(dragon =>
                                <tr key={dragon.id}>
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