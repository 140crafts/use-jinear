import MainFeaturesSideMenu from "@/components/mainFeaturesSideMenu/MainFeaturesSideMenu";
import WorkspaceModalProvider from "@/components/modal-provider/WorkspaceModalProvider";
import WorkspaceLayoutHeader from "@/components/workspaceLayoutHeader/WorkspaceLayoutHeader";
import useWidthLimit, {MOBILE_LAYOUT_BREAKPOINT} from "@/hooks/useWidthLimit";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {closeAllMenus} from "@/store/slice/displayPreferenceSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import isPwa from "@/util/pwaHelper";
import cn from "classnames";
import React, {useEffect} from "react";
import styles from "./layout.module.scss";
import {useLazyRetrieveChannelMembershipsQuery, useRetrieveChannelMembershipsQuery} from "@/api/channelMemberApi";
import {
    useLazyRetrieveParticipatedConversationsQuery,
} from "@/api/conversationApi";
import useDetectKeyboardOpen from "@/hooks/useDetectKeyboardOpen";
import {usePageVisibility} from "@/hooks/usePageVisibility";
import {Outlet, useLocation, useParams} from "react-router-dom";

interface WorkspaceLayoutProps {

}

const WorkspaceLayout: React.FC<WorkspaceLayoutProps> = ({}) => {
    const dispatch = useAppDispatch();
    const {workspaceName} = useParams();
    const {pathname} = useLocation();
    const isMobile = useWidthLimit({limit: MOBILE_LAYOUT_BREAKPOINT});
    const onScreenKeyboardOpen = useDetectKeyboardOpen();
    const _isPwa = isPwa();

    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const pageVisibility = usePageVisibility();

    //so we can calculate unread count
    const [retrieveChannelMembershipsQuery] = useLazyRetrieveChannelMembershipsQuery();
    const [retrieveParticipatedConversationsQuery] = useLazyRetrieveParticipatedConversationsQuery();

    useEffect(() => {
        if (pageVisibility && workspace && pageVisibility) {
            retrieveChannelMembershipsQuery({workspaceId: workspace.workspaceId});
            retrieveParticipatedConversationsQuery({workspaceId: workspace.workspaceId});
        }
    }, [retrieveChannelMembershipsQuery, retrieveParticipatedConversationsQuery, workspace, pageVisibility]);

    useEffect(() => {
        if (isMobile) {
            dispatch(closeAllMenus());
        }
    }, [dispatch, pathname, isMobile]);

    return (
        <div id="workspace-layout-container" className={styles.container}>
            <div id="workspace-layout-header" className={styles.header}>
                <WorkspaceLayoutHeader/>
            </div>
            <div id="workspace-layout-content" className={styles.content}>
                <div
                    id="workspace-layout-page-side-menu-container"
                    className={cn(styles.workspaceSideMenuContainer, _isPwa && styles.workspaceSideMenuContainerPwa)}
                >
                    {workspace && <MainFeaturesSideMenu workspace={workspace}/>}
                </div>
                <div id="workspace-layout-page-content" className={styles.pageContent}>
                    <Outlet />
                </div>
            </div>
        </div>
    );
};

export default WorkspaceLayout;
