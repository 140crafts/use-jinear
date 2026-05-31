import CircularLoading from "@/components/circularLoading/CircularLoading";
import React, {useEffect} from "react";
import styles from "./index.module.css";
import {useNavigate, useParams} from "react-router-dom";

interface WorkspacePageProps {
}

const WorkspacePage: React.FC<WorkspacePageProps> = ({}) => {
    const navigate = useNavigate();
    const {workspaceName} = useParams();

    useEffect(() => {
        if (workspaceName) {
            const timeout = setTimeout(() => {
                navigate(`/${workspaceName}/calendar`, {replace: true});
            }, 1500);
            return (() => clearTimeout(timeout));
        }
    }, [navigate, workspaceName]);

    return (
        <div className={styles.container}>
            <CircularLoading/>
        </div>
    );
};
export default WorkspacePage;
