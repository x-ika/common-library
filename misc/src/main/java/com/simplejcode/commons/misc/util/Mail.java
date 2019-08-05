package com.simplejcode.commons.misc.util;

import lombok.*;

@Getter
@Setter
public class Mail {

    private String host;

    private String port;

    private String tls;

    private String username;

    private String password;


    private String fromMail;

    private String recipient;

    private String mailSubject;

    private String content;

}
