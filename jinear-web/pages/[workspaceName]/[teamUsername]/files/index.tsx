import GenericBreadcrumb from "@/components/genericBreadcrumb/GenericBreadcrumb";
import TeamFileList from "@/components/teamFilesScreen/TeamFileList";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface FilesScreenProps {}

const FilesScreen: React.FC<FilesScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse, isFetching: isTeamsFetching } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  return (
    <div className={styles.container}>
      {workspace && team && (
        <GenericBreadcrumb
          workspace={workspace}
          team={team}
          label={t("teamFilesScreenBreadcrumbTitle")}
          pathAfterWorkspaceAndTeam="/files"
        />
      )}
      {team && <TeamFileList teamId={team.teamId} />}
    </div>
  );
};

export default FilesScreen;
