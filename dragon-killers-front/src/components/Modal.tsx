import {ReactNode} from "react";

interface Props {
    isOpen: boolean,
    onClose: () => void,
    children: ReactNode
}

export const Modal = (props: Props) => {
    if (!props.isOpen) return null

    return (
        <div
            onClick={props.onClose}
            style={{
                position: "fixed",
                top: 0,
                left: 0,
                width: "100%",
                height: "100%",
                background: "rgba(0, 0, 0, 0.5)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
            }}
        >
            <div
                style={{
                    background: "white",
                    margin: "auto",
                    padding: "2%",
                }}
                onClick={(e) => e.stopPropagation()}
            >
                {props.children}
            </div>
        </div>
    )
}