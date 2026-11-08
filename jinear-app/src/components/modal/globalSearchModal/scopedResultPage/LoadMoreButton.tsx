import React from "react";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locales/useTranslation";
import styles from "./ScopedResultPage.module.css";

interface LoadMoreButtonProps {
    onClick: () => void;
}

const LoadMoreButton: React.FC<LoadMoreButtonProps> = ({onClick}) => {
    const {t} = useTranslation();

    return (
        <Button
            className={styles.loadMore}
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
            onClick={onClick}
        >
            {t("globalSearchModalLoadMore")}
        </Button>
    );
};

export default LoadMoreButton;
