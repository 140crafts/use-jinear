import type React from "react";
import type {TaskDto} from "@/model/be/jinear-core";
import {popTaskContextMenu} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";

export const useTaskContextMenu = (task?: TaskDto | null) => {
    const dispatch = useAppDispatch();

    return (event: React.MouseEvent<HTMLElement>) => {
        if (!task) {
            return;
        }
        event.preventDefault();
        event.stopPropagation();
        const openedFromKeyboard = event.clientX == 0 && event.clientY == 0;
        const rect = event.currentTarget.getBoundingClientRect();
        const x = openedFromKeyboard ? rect.left : event.clientX;
        const y = openedFromKeyboard ? rect.bottom : event.clientY;
        dispatch(popTaskContextMenu({visible: true, task, x, y}));
    };
};
