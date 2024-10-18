import ServicesStatusBar from "./ServicesStatusBar";
import {DragonsList} from "./DragonsList";
import {useState} from "react";
import {Modal} from "./Modal";
import {CreateDragon} from "./CreateDragon";
import {DragonDto} from "../services/DragonsService";
import {DragonByColorCounter} from "./DragonByColorCounter";
import {DragonWithGreaterCaveFinder} from "./DragonWithGreaterCaveFinder";
import {DragonByDescriptionGrouper} from "./DragonByDescriptionGrouper";

export const HomePage = () => {

    const [modalOpen, setModalOpen] = useState<boolean>(false)
    const [editable, setEditable] = useState<DragonDto | null>(null)

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

    return (
        <>
            <ServicesStatusBar/>
            <DragonsList onNew={onDragonCreate} onEdit={onDragonEdit}/>
            <DragonByColorCounter/>
            <DragonWithGreaterCaveFinder/>
            <DragonByDescriptionGrouper/>
            <Modal isOpen={modalOpen} onClose={closeModal}>
                <CreateDragon onSuccess={closeModal} editable={editable}/>
            </Modal>
        </>
    )
}