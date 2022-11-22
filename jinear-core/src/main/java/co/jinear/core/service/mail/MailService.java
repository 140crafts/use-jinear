package co.jinear.core.service.mail;

import co.jinear.core.config.properties.MailProperties;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.mail.LoginMailVo;
import co.jinear.core.model.vo.mail.AccountEngageMailVo;
import co.jinear.core.model.vo.mail.SendMailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
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

    private static final String S3_BUCKET_URL = "https://storage.googleapis.com/bittit-b0/";
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
        String href = emailConfirmationUrl.replaceAll(Pattern.quote("{token}"), accountEngageMailVo.getToken()) ;
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
        String href =  passwordResetUrl.replaceAll(Pattern.quote("{token}"), accountEngageMailVo.getToken());
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
