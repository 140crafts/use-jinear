import React from "react";
import { LuCalendar, LuCalendarPlus } from "react-icons/lu";
import { ButtonVariants } from "@/components/button";
import DatePickerButton from "@/components/datePickerButton/DatePickerButton";
import useTranslation from "@/locals/useTranslation";
import { useUpdateProjectDatesMutation } from "@/api/projectOperationApi";
import { ProjectDto } from "@/be/jinear-core";

interface ProjectTargetDateProps {
  project: ProjectDto;
}

const ProjectTargetDate: React.FC<ProjectTargetDateProps> = ({ project }) => {
  const { t } = useTranslation();

  const [updateProjectDates, { isLoading }] = useUpdateProjectDatesMutation();

  const onTargetDateChange = (targetDate?: Date | null) => {
    updateProjectDates({
      projectId: project.projectId, body: {
        targetDate: targetDate,
        updateTargetDate: true
      }
    });
  };

  return (
    <DatePickerButton
      emptySelectionComponent={<LuCalendarPlus className={"icon"} />}
      initialDate={project.targetDate ? new Date(project.targetDate) : undefined}
      unpickableFromModal={true}
      icon={LuCalendar}
      dataTooltipRight={t("projectRowTargetDate")}
      pickedDateFormat={t("dateFormatShortMonthReadable")}
      buttonVariant={ButtonVariants.default}
      onDateChange={onTargetDateChange}
      loading={isLoading}
    />
  );
};

export default ProjectTargetDate;