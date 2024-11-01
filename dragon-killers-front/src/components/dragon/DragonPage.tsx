import {DragonsList} from "./DragonsList";
import {DragonByColorCounter} from "./DragonByColorCounter";
import {DragonWithGreaterCaveFinder} from "./DragonWithGreaterCaveFinder";
import {DragonByDescriptionGrouper} from "./DragonByDescriptionGrouper";
import {KillerTeamsList} from "./KillerTeamsList";
import {Modal} from "../Modal";
import {CreateDragon} from "./CreateDragon";
import {useState} from "react";
import {DragonDto} from "../../services/DragonsService";

export const DragonPage = () => {

    const [modalOpen, setModalOpen] = useState<boolean>(false)
    const [editable, setEditable] = useState<DragonDto | null>(null)
    const [refresh, setRefresh] = useState<boolean>(false)

    const openModal = () => setModalOpen(true)

    const closeModal = () => setModalOpen(false)

    const onDragonCreate = () => {
        setEditable(null)
        openModal()
    }

    const onDragonEdit = (dto: DragonDto) => {
        setEditable(dto)
        openModal()
    }

    const onSuccessModal = () => {
        setRefresh((prev) => !prev)
        closeModal()
    }

    return (
        <>
            <h2>Dragon service</h2>
            <DragonsList onNew={onDragonCreate} onEdit={onDragonEdit} refresh={refresh}/>
            <br/><br/>
            <DragonByColorCounter/>
            <br/><br/>
            <DragonWithGreaterCaveFinder/>
            <br/><br/>
            <DragonByDescriptionGrouper/>
            <br/><br/>
            <KillerTeamsList/>
            <Modal isOpen={modalOpen} onClose={closeModal}>
                <CreateDragon onSuccess={onSuccessModal} editable={editable}/>
            </Modal>
        </>
    )
}