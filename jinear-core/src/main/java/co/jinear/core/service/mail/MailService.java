package co.jinear.core.service.mail;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.MailProperties;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.model.vo.mail.*;
import co.jinear.core.system.NormalizeHelper;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.nlab.smtp.pool.SmtpConnectionPool;
import org.nlab.smtp.transport.connection.ClosableSmtpConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static co.jinear.core.system.NormalizeHelper.EMPTY_STRING;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private static final String S3_BUCKET_URL = "https://storage.googleapis.com/bittit-b0/";
    private static final Map<TaskReminderType, LocaleStringType> taskReminderLocaleStringMap =
            Map.of(
                    TaskReminderType.ASSIGNED_DATE, LocaleStringType.TASK_REMINDER_TYPE_ASSIGNED_DATE,
                    TaskReminderType.DUE_DATE, LocaleStringType.TASK_REMINDER_TYPE_DUE_DATE,
                    TaskReminderType.SPECIFIC_DATE, LocaleStringType.TASK_REMINDER_TYPE_SPECIFIC_DATE
            );

    private final SmtpConnectionPool smtpConnectionPool;
    private final LocaleStringService localeStringService;
    private final MailProperties mailProperties;
    private final FeProperties feProperties;

    private void sendMail(SendMailVo sendMailVo) throws Exception {
        log.info("Send mail has started. to: {}", sendMailVo.getTo());
        try (ClosableSmtpConnection transport = smtpConnectionPool.borrowObject()) {
            MimeMessage message = new MimeMessage(transport.getSession());
            message.setFrom(mailProperties.getMailUserName());
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendMailVo.getTo()));
            message.setSubject(sendMailVo.getSubject(), "UTF-8");
            message.setContent(sendMailVo.getContext(), "text/html; charset=UTF-8");
            transport.sendMessage(message);
        }
        log.info("Send mail has finished. to: {}", sendMailVo.getTo());
    }

    @Async
    public void sendLoginMail(LoginMailVo loginMailVo) throws Exception {
        LocaleType preferredLocale = Optional.ofNullable(loginMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        String title = retrieveLoginMailTitle(loginMailVo, preferredLocale);
        String text = retrieveLoginMailText(preferredLocale);
        String mailBody = retrieveMailTemplate("email-login-token-mail.html");
        mailBody = fillLoginMailTemplate(loginMailVo, title, text, mailBody);
        sendMail(new SendMailVo(loginMailVo.getEmail(), title, mailBody));
    }

    @Async
    public void sendMailConfirmationMail(AccountEngageMailVo accountEngageMailVo) throws Exception {
        LocaleType preferredLocale = Optional.ofNullable(accountEngageMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        String title = localeStringService.retrieveLocalString(LocaleStringType.MAIL_CONFIRMATION_TITLE, preferredLocale);
        String text = localeStringService.retrieveLocalString(LocaleStringType.MAIL_CONFIRMATION_TEXT, preferredLocale);
        String ctaLabel = localeStringService.retrieveLocalString(LocaleStringType.MAIL_CONFIRMATION_CTA_LABEL, preferredLocale);
        String mailBody = retrieveMailTemplate("email-confirm-mail.html");
        String href = feProperties.getEmailConfirmationUrl().replaceAll(Pattern.quote("{token}"), accountEngageMailVo.getToken());
        mailBody = mailBody.replaceAll(Pattern.quote("${title}"), title)
                .replaceAll(Pattern.quote("${text}"), text)
                .replaceAll(Pattern.quote("${confirm}"), ctaLabel)
                .replaceAll(Pattern.quote("${href}"), href);
        sendMail(new SendMailVo(accountEngageMailVo.getEmail(), title, mailBody));
    }

    @Async
    public void sendResetPasswordMail(AccountEngageMailVo accountEngageMailVo) throws Exception {
        LocaleType preferredLocale = Optional.ofNullable(accountEngageMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        String title = localeStringService.retrieveLocalString(LocaleStringType.PASSWORD_RESET_TITLE, preferredLocale);
        String text = localeStringService.retrieveLocalString(LocaleStringType.PASSWORD_RESET_TEXT, preferredLocale);
        String ctaLabel = localeStringService.retrieveLocalString(LocaleStringType.PASSWORD_RESET_CTA_LABEL, preferredLocale);
        String mailBody = retrieveMailTemplate("reset-password-mail.html");
        String href = feProperties.getPasswordResetUrl().replaceAll(Pattern.quote("{token}"), accountEngageMailVo.getToken());
        mailBody = mailBody.replaceAll(Pattern.quote("${title}"), title)
                .replaceAll(Pattern.quote("${text}"), text)
                .replaceAll(Pattern.quote("${confirm}"), ctaLabel)
                .replaceAll(Pattern.quote("${href}"), href);
        sendMail(new SendMailVo(accountEngageMailVo.getEmail(), title, mailBody));
    }

    @Async
    public void sendNewPasswordMail(AccountEngageMailVo accountEngageMailVo) throws Exception {
        LocaleType preferredLocale = Optional.ofNullable(accountEngageMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        String title = localeStringService.retrieveLocalString(LocaleStringType.NEW_PASSWORD_TITLE, preferredLocale);
        String text = localeStringService.retrieveLocalString(LocaleStringType.NEW_PASSWORD_TEXT, preferredLocale);
        String mailBody = retrieveMailTemplate("new-password-mail.html");
        mailBody = mailBody.replaceAll(Pattern.quote("${title}"), title)
                .replaceAll(Pattern.quote("${text}"), text)
                .replaceAll(Pattern.quote("${token}"), accountEngageMailVo.getToken());
        sendMail(new SendMailVo(accountEngageMailVo.getEmail(), title, mailBody));
    }

    @Async
    public void sendTaskReminderMail(TaskReminderMailVo taskReminderMailVo) throws Exception {
        LocaleType preferredLocale = Optional.ofNullable(taskReminderMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        LocaleStringType taskReminderTypeLocaleString = taskReminderLocaleStringMap.getOrDefault(taskReminderMailVo.getTaskReminderType(), LocaleStringType.TASK_REMINDER_TYPE_SPECIFIC_DATE);
        String taskReminderTypeText = localeStringService.retrieveLocalString(taskReminderTypeLocaleString, preferredLocale);

        String title = localeStringService.retrieveLocalString(LocaleStringType.TASK_REMINDER_TITLE, preferredLocale);
        title = title.replaceAll(Pattern.quote("${taskTag}"), taskReminderMailVo.getTaskTag())
                .replaceAll(Pattern.quote("${taskTitle}"), taskReminderMailVo.getTaskTitle())
                .replaceAll(Pattern.quote("${reminderTypeText}"), taskReminderTypeText);

        String reminderText = localeStringService.retrieveLocalString(LocaleStringType.TASK_REMINDER_TEXT, preferredLocale);

        String taskCtaHref = feProperties.getTaskUrl().replaceAll(Pattern.quote("{workspaceName}"), taskReminderMailVo.getWorkspaceUsername())
                .replaceAll(Pattern.quote("{taskTag}"), taskReminderMailVo.getTaskTag());
        String ctaLabel = localeStringService.retrieveLocalString(LocaleStringType.TASK_REMINDER_GO_TO_TASK, preferredLocale);

        String mailBody = retrieveMailTemplate("task-reminder-mail.html");
        mailBody = mailBody.replaceAll(Pattern.quote("${reminderTypeText}"), taskReminderTypeText)
                .replaceAll(Pattern.quote("${reminderText}"), reminderText)
                .replaceAll(Pattern.quote("${taskTag}"), taskReminderMailVo.getTaskTag())
                .replaceAll(Pattern.quote("${taskTitle}"), taskReminderMailVo.getTaskTitle())
                .replaceAll(Pattern.quote("${taskDescription}"), taskReminderMailVo.getTaskDetail())
                .replaceAll(Pattern.quote("${href}"), taskCtaHref)
                .replaceAll(Pattern.quote("${ctaLabel}"), ctaLabel);

        StringBuilder titleStringBuilder = new StringBuilder(title);
        Optional.of(taskReminderMailVo)
                .map(TaskReminderMailVo::getAccountLocaleDate)
                .ifPresent(date -> titleStringBuilder
                        .append(NormalizeHelper.SPACE_STRING)
                        .append(NormalizeHelper.HYPHEN)
                        .append(NormalizeHelper.SPACE_STRING)
                        .append(date));

        sendMail(new SendMailVo(taskReminderMailVo.getEmail(), titleStringBuilder.toString(), mailBody));
    }

    @Async
    public void sendWorkspaceInvitationMail(WorkspaceInvitationMailVo workspaceInvitationMailVo) throws Exception {
        LocaleType preferredLocale = Optional.ofNullable(workspaceInvitationMailVo.getPreferredLocale()).orElse(LocaleType.EN);

        String titleMail = localeStringService.retrieveLocalString(LocaleStringType.WORKSPACE_INVITATION_TITLE_MAIL, preferredLocale);
        titleMail = titleMail.replaceAll(Pattern.quote("${fromName}"), workspaceInvitationMailVo.getSenderName())
                .replaceAll(Pattern.quote("${workspaceName}"), workspaceInvitationMailVo.getWorkspaceName());

        String titleBody = localeStringService.retrieveLocalString(LocaleStringType.WORKSPACE_INVITATION_TITLE_BODY, preferredLocale);
        titleBody = titleBody.replaceAll(Pattern.quote("${fromName}"), workspaceInvitationMailVo.getSenderName())
                .replaceAll(Pattern.quote("${workspaceName}"), workspaceInvitationMailVo.getWorkspaceName());

        String text = localeStringService.retrieveLocalString(LocaleStringType.WORKSPACE_INVITATION_TEXT, preferredLocale);
        String ctaLabel = localeStringService.retrieveLocalString(LocaleStringType.WORKSPACE_INVITATION_CTA_LABEL, preferredLocale);


        String mailBody = retrieveMailTemplate("email-workspace-invitation-mail.html");

        String href = feProperties.getWorkspaceInvitationUrl().replaceAll(Pattern.quote("{token}"), workspaceInvitationMailVo.getToken());

        mailBody = mailBody.replaceAll(Pattern.quote("${title}"), titleBody)
                .replaceAll(Pattern.quote("${text}"), text)
                .replaceAll(Pattern.quote("${confirm}"), ctaLabel)
                .replaceAll(Pattern.quote("${href}"), href);
        sendMail(new SendMailVo(workspaceInvitationMailVo.getEmail(), titleMail, mailBody));
    }

    @Async
    public void sendGenericInfoWithSubInfoMail(GenericInfoWithSubInfoMailVo genericInfoWithSubInfoMail) throws Exception {
        String mailBody = retrieveMailTemplate("generic-info-with-sub-info-mail.html")
                .replaceAll(Pattern.quote("${mailBodyTitle}"), Optional.of(genericInfoWithSubInfoMail).map(GenericInfoWithSubInfoMailVo::getMailBodyTitle).orElse(EMPTY_STRING))
                .replaceAll(Pattern.quote("${mailBodyText}"), Optional.of(genericInfoWithSubInfoMail).map(GenericInfoWithSubInfoMailVo::getMailBodyText).orElse(EMPTY_STRING))
                .replaceAll(Pattern.quote("${mailBodySubtext}"), Optional.of(genericInfoWithSubInfoMail).map(GenericInfoWithSubInfoMailVo::getMailBodySubtext).orElse(EMPTY_STRING));
        sendMail(new SendMailVo(genericInfoWithSubInfoMail.getEmail(), genericInfoWithSubInfoMail.getMailTitle(), mailBody));
    }

    @Async
    public void sendGenericInfoWithSubInfoWithCtaButtonMail(GenericInfoWithSubInfoMailWithCtaButtonVo genericInfoWithSubInfoMailWithCtaButtonVo) throws Exception {
        String mailBody = retrieveMailTemplate("generic-info-with-sub-info-mail-with-cta.html")
                .replaceAll(Pattern.quote("${mailBodyTitle}"), Optional.of(genericInfoWithSubInfoMailWithCtaButtonVo).map(GenericInfoWithSubInfoMailWithCtaButtonVo::getMailBodyTitle).orElse(EMPTY_STRING))
                .replaceAll(Pattern.quote("${mailBodyText}"), Optional.of(genericInfoWithSubInfoMailWithCtaButtonVo).map(GenericInfoWithSubInfoMailWithCtaButtonVo::getMailBodyText).orElse(EMPTY_STRING))
                .replaceAll(Pattern.quote("${mailBodySubtext}"), Optional.of(genericInfoWithSubInfoMailWithCtaButtonVo).map(GenericInfoWithSubInfoMailWithCtaButtonVo::getMailBodySubtext).orElse(EMPTY_STRING))
                .replaceAll(Pattern.quote("${href}"), Optional.of(genericInfoWithSubInfoMailWithCtaButtonVo).map(GenericInfoWithSubInfoMailWithCtaButtonVo::getHref).orElse(EMPTY_STRING))
                .replaceAll(Pattern.quote("${ctaLabel}"), Optional.of(genericInfoWithSubInfoMailWithCtaButtonVo).map(GenericInfoWithSubInfoMailWithCtaButtonVo::getCtaLabel).orElse(EMPTY_STRING));
        sendMail(new SendMailVo(genericInfoWithSubInfoMailWithCtaButtonVo.getEmail(), genericInfoWithSubInfoMailWithCtaButtonVo.getMailTitle(), mailBody));
    }

    private String retrieveLoginMailTitle(LoginMailVo loginMailVo, LocaleType preferredLocale) {
        String title = localeStringService.retrieveLocalString(LocaleStringType.LOGIN_MAIL_TITLE, preferredLocale);
        return title.replaceAll(Pattern.quote("${code}"), loginMailVo.getEmailCode());

    }

    private String retrieveLoginMailText(LocaleType preferredLocale) {
        return localeStringService.retrieveLocalString(LocaleStringType.LOGIN_MAIL_TEXT, preferredLocale);
    }

    private String fillLoginMailTemplate(LoginMailVo loginMailVo, String title, String text, String mailBody) {
        return mailBody.replaceAll(Pattern.quote("${title}"), title)
                .replaceAll(Pattern.quote("${text}"), text)
                .replaceAll(Pattern.quote("${token}"), loginMailVo.getEmailCode());
    }

    private String retrieveMailTemplate(String fileName) throws IOException {
        InputStream inputStream = new ClassPathResource("mail/" + fileName).getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
        return writer.toString();
    }

    private void attachFile(SendMailVo sendMailVo, MimeMessageHelper helper) throws MessagingException {
        if (Objects.nonNull(sendMailVo.getAttachment())) {
            ByteArrayDataSource attachment = new ByteArrayDataSource(sendMailVo.getAttachment().getBytes(), sendMailVo.getAttachmentContentType());//"application/octet-stream"
            helper.addAttachment(sendMailVo.getAttachmentFileName(), attachment);
        }
    }
}
