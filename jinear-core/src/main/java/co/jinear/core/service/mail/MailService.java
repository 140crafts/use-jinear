package co.jinear.core.service.mail;

import co.jinear.core.config.properties.MailProperties;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.model.vo.mail.AccountEngageMailVo;
import co.jinear.core.model.vo.mail.LoginMailVo;
import co.jinear.core.model.vo.mail.SendMailVo;
import co.jinear.core.model.vo.mail.TaskReminderMailVo;
import co.jinear.core.system.NormalizeHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    @Value("${fe.email-confirmation-url}")
    private String emailConfirmationUrl;

    @Value("${fe.password-reset-url}")
    private String passwordResetUrl;

    @Value("${fe.task-url}")
    private String taskUrl;

    private static final String S3_BUCKET_URL = "https://storage.googleapis.com/bittit-b0/";
    private static final Map<TaskReminderType, LocaleStringType> taskReminderLocaleStringMap =
            Map.of(
                    TaskReminderType.ASSIGNED_DATE, LocaleStringType.TASK_REMINDER_TYPE_ASSIGNED_DATE,
                    TaskReminderType.DUE_DATE, LocaleStringType.TASK_REMINDER_TYPE_DUE_DATE,
                    TaskReminderType.SPECIFIC_DATE, LocaleStringType.TASK_REMINDER_TYPE_SPECIFIC_DATE
            );

    private final JavaMailSender sender;
    private final LocaleStringService localeStringService;
    private final MailProperties mailProperties;

    private void sendMail(SendMailVo sendMailVo) throws MessagingException {
        log.info("Send mail has started. to: {}", sendMailVo.getTo());
        MimeMessage message = sender.createMimeMessage();
        message.setFrom(mailProperties.getMailUserName());
        MimeMessageHelper helper = new MimeMessageHelper(message, Objects.nonNull(sendMailVo.getAttachment()));
        helper.setTo(sendMailVo.getTo());
        helper.setText(sendMailVo.getContext(), true);
        helper.setSubject(sendMailVo.getSubject());
        attachFile(sendMailVo, helper);
        sender.send(message);
        log.info("Send mail has finished. to: {}", sendMailVo.getTo());
    }

    @Async
    public void sendLoginMail(LoginMailVo loginMailVo) throws IOException, MessagingException {
        LocaleType preferredLocale = Optional.ofNullable(loginMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        String title = retrieveLoginMailTitle(loginMailVo, preferredLocale);
        String text = retrieveLoginMailText(preferredLocale);
        String mailBody = retrieveMailTemplate("email-login-token-mail.html");
        mailBody = fillLoginMailTemplate(loginMailVo, title, text, mailBody);
        sendMail(new SendMailVo(loginMailVo.getEmail(), title, mailBody));
    }

    @Async
    public void sendMailConfirmationMail(AccountEngageMailVo accountEngageMailVo) throws IOException, MessagingException {
        LocaleType preferredLocale = Optional.ofNullable(accountEngageMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        String title = localeStringService.retrieveLocalString(LocaleStringType.MAIL_CONFIRMATION_TITLE, preferredLocale);
        String text = localeStringService.retrieveLocalString(LocaleStringType.MAIL_CONFIRMATION_TEXT, preferredLocale);
        String ctaLabel = localeStringService.retrieveLocalString(LocaleStringType.MAIL_CONFIRMATION_CTA_LABEL, preferredLocale);
        String mailBody = retrieveMailTemplate("email-confirm-mail.html");
        String href = emailConfirmationUrl.replaceAll(Pattern.quote("{token}"), accountEngageMailVo.getToken());
        mailBody = mailBody.replaceAll(Pattern.quote("${title}"), title)
                .replaceAll(Pattern.quote("${text}"), text)
                .replaceAll(Pattern.quote("${confirm}"), ctaLabel)
                .replaceAll(Pattern.quote("${href}"), href);
        sendMail(new SendMailVo(accountEngageMailVo.getEmail(), title, mailBody));
    }

    @Async
    public void sendResetPasswordMail(AccountEngageMailVo accountEngageMailVo) throws IOException, MessagingException {
        LocaleType preferredLocale = Optional.ofNullable(accountEngageMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        String title = localeStringService.retrieveLocalString(LocaleStringType.PASSWORD_RESET_TITLE, preferredLocale);
        String text = localeStringService.retrieveLocalString(LocaleStringType.PASSWORD_RESET_TEXT, preferredLocale);
        String ctaLabel = localeStringService.retrieveLocalString(LocaleStringType.PASSWORD_RESET_CTA_LABEL, preferredLocale);
        String mailBody = retrieveMailTemplate("reset-password-mail.html");
        String href = passwordResetUrl.replaceAll(Pattern.quote("{token}"), accountEngageMailVo.getToken());
        mailBody = mailBody.replaceAll(Pattern.quote("${title}"), title)
                .replaceAll(Pattern.quote("${text}"), text)
                .replaceAll(Pattern.quote("${confirm}"), ctaLabel)
                .replaceAll(Pattern.quote("${href}"), href);
        sendMail(new SendMailVo(accountEngageMailVo.getEmail(), title, mailBody));
    }

    @Async
    public void sendNewPasswordMail(AccountEngageMailVo accountEngageMailVo) throws IOException, MessagingException {
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
    public void sendTaskReminderMail(TaskReminderMailVo taskReminderMailVo) throws IOException, MessagingException {
        LocaleType preferredLocale = Optional.ofNullable(taskReminderMailVo.getPreferredLocale()).orElse(LocaleType.EN);
        LocaleStringType taskReminderTypeLocaleString = taskReminderLocaleStringMap.getOrDefault(taskReminderMailVo.getTaskReminderType(), LocaleStringType.TASK_REMINDER_TYPE_SPECIFIC_DATE);
        String taskReminderTypeText = localeStringService.retrieveLocalString(taskReminderTypeLocaleString, preferredLocale);

        String title = localeStringService.retrieveLocalString(LocaleStringType.TASK_REMINDER_TITLE, preferredLocale);
        title = title.replaceAll(Pattern.quote("${taskTag}"), taskReminderMailVo.getTaskTag())
                .replaceAll(Pattern.quote("${taskTitle}"), taskReminderMailVo.getTaskTitle())
                .replaceAll(Pattern.quote("${reminderTypeText}"), taskReminderTypeText);

        String reminderText = localeStringService.retrieveLocalString(LocaleStringType.TASK_REMINDER_TEXT, preferredLocale);

        String taskCtaHref = taskUrl.replaceAll(Pattern.quote("{workspaceName}"), taskReminderMailVo.getWorkspaceName())
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
