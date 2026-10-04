import useWidthLimit from "@/hooks/useWidthLimit";
import {selectAnyMenuVisible} from "@/store/slice/displayPreferenceSlice";
import {
    selectAnyModalVisible,
    selectUploadStatusModalMinimized,
    selectUploadStatusModalMouseOver,
    selectUploadStatusModalVisible
} from "@/store/slice/modalSlice";
import {useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import React, {useEffect} from "react";

interface BodyFixerProps {
}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const logger = Logger("BodyFixer");
const BodyFixer: React.FC<BodyFixerProps> = ({}) => {
    const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
    const isAnyMenuVisible = useTypedSelector(selectAnyMenuVisible);
    const isMobile = useWidthLimit({limit: MOBILE_LAYOUT_BREAKPOINT});
    const uploadStatusModalVisible = useTypedSelector(selectUploadStatusModalVisible);
    const uploadStatusModalMinimized = useTypedSelector(selectUploadStatusModalMinimized) ?? false;
    const uploadStatusModalMouseOver = useTypedSelector(selectUploadStatusModalMouseOver) ?? false;
    const shouldFixBodyDueUploadModalOnMobile = isMobile && uploadStatusModalVisible && !uploadStatusModalMinimized;
    const shouldFixBodyDueUploadModalOnWebAndNotMinimized = !isMobile && uploadStatusModalVisible && !uploadStatusModalMinimized && uploadStatusModalMouseOver;

    logger.log({
        isAnyModalVisible,
        isAnyMenuVisible,
        isMobile,
        uploadStatusModalVisible,
        uploadStatusModalMinimized,
        uploadStatusModalMouseOver
    });

    useEffect(() => {
        if (document && window) {
            if (isAnyModalVisible || (isAnyMenuVisible && isMobile) || shouldFixBodyDueUploadModalOnMobile || shouldFixBodyDueUploadModalOnWebAndNotMinimized) {
                document.body.style.top = `-${window.scrollY}px`;
                document.body.style.width = `100%`;
                document.body.style.position = "fixed";
            } else {
                const scrollY = document.body.style.top;
                document.body.style.position = "";
                document.body.style.top = "";
                // window.scrollTo(0, parseInt(scrollY || "0") * -1);
                document.documentElement.style.scrollBehavior = "auto";
                window.scrollTo({top: parseInt(scrollY || "0") * -1, behavior: "auto"});
                document.documentElement.style.scrollBehavior = "";
            }
        }
    }, [isAnyModalVisible, isAnyMenuVisible, isMobile, shouldFixBodyDueUploadModalOnMobile, shouldFixBodyDueUploadModalOnWebAndNotMinimized]);

    return null;
};

export default BodyFixer;
