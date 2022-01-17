package com.simplejcode.commons.misc.util;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class Mail {

    @AllArgsConstructor
    public static final class Attachment {
        String fileName;
        String mimeType;
        byte[] data;
    }

    private String host;

    private String port;

    private String tls;

    private String username;

    private String password;


    private String fromMail;

    private String recipient;
    private String carbonCopy;
    private String blindCarbonCopy;

    private String mailSubject;

    private String textContent;
    private String htmlContent;

    private List<Attachment> attachments;

}
