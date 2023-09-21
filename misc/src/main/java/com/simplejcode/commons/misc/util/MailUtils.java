package com.simplejcode.commons.misc.util;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import java.util.Properties;

public final class MailUtils {

    private MailUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static boolean isValidEmail(String email) {
        return StringUtils.isNotBlank(email) && email.matches("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-z0A-Z-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    public static void sendMail(Mail mail) {
        //Get the session object
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", mail.getHost());

        if (mail.getTls() != null) {
            properties.setProperty("mail.smtp.starttls.enable", mail.getTls());
        }
        if (mail.getPort() != null) {
            properties.setProperty("mail.smtp.port", mail.getPort());
        }

        SMTPAuthenticator authenticator;
        if (mail.getPassword() != null) {
            properties.put("mail.smtp.auth", "true");
            authenticator = new SMTPAuthenticator(mail.getUsername(), mail.getPassword());
        } else {
            authenticator = null;
        }

        Session session = Session.getInstance(properties, authenticator);

        //compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getFromMail()));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getRecipient()));
            if (mail.getCarbonCopy() != null) {
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getCarbonCopy()));
            }
            if (mail.getBlindCarbonCopy() != null) {
                message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail.getBlindCarbonCopy()));
            }
            message.setSubject(mail.getMailSubject());

            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Mail Body");

            if (mail.getAttachments() != null) {
                for (Mail.Attachment attachment : mail.getAttachments()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.setFileName(MimeUtility.encodeText(attachment.fileName));
                    ByteArrayDataSource ds = new ByteArrayDataSource(attachment.data, attachment.mimeType);
                    attachmentPart.setDataHandler(new DataHandler(ds));
                    multipart.addBodyPart(attachmentPart);
                }
            }

            MimeBodyPart bodyPart = new MimeBodyPart();
            if (mail.getTextContent() != null) {
                bodyPart.setText(mail.getTextContent());
            }
            if (mail.getHtmlContent() != null) {
                bodyPart.setContent(mail.getHtmlContent(), "text/html; charset=utf-8");
            }
            multipart.addBodyPart(bodyPart);

            message.setContent(multipart);

            // Send message
            Transport.send(message);
        } catch (Exception e) {
            throw convert(e);
        }
    }

    private static class SMTPAuthenticator extends jakarta.mail.Authenticator {

        private String username;

        private String password;

        SMTPAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
