import React from 'react';
import {LuFoldHorizontal, LuUnfoldHorizontal} from "react-icons/lu";
import Button, {ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";
import {useAppDispatch, useTypedSelector} from "@/store";
import {selectNoteTextWidth, toggleNoteTextWidth} from "@/slice/displayPreferenceSlice.ts";

const NoteTextWidthButton: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const isFullWidth = useTypedSelector(selectNoteTextWidth) === "full";

    return (
        <Button
            onClick={() => dispatch(toggleNoteTextWidth())}
            variant={ButtonVariants.hoverFilled}
            data-tooltip-top={t(isFullWidth ? "noteTextWidthPage" : "noteTextWidthFull")}
        >
            {isFullWidth ? <LuFoldHorizontal/> : <LuUnfoldHorizontal/>}
        </Button>
    );
}

export default NoteTextWidthButton;
