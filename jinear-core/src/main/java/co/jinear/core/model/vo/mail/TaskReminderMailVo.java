package co.jinear.core.model.vo.mail;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskReminderMailVo {

    @EqualsAndHashCode.Include
    private String email;
    private LocaleType preferredLocale;
    @EqualsAndHashCode.Include
    private String taskId;
    private String taskTag;
    private String taskTitle;
    @ToString.Exclude
    private String taskDetail;
    private TaskReminderType taskReminderType;
    private String workspaceName;
    private String accountLocaleDate;
}
