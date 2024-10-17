package com.handson.basic.util;

import com.sendgrid.*;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
public class EmailService {


    @Value("${sendgrid.user}")
    private String USER;

    @Value("${sendgrid.key}")
    private String ACCOUNT_KEY;


    public boolean send(String text, String subject, String email) throws IOException{
        if(email == null) return false;
        Email from = new Email(USER);
        Email to = new Email(email);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(ACCOUNT_KEY);
        String json = "{\"key\":\"" + ACCOUNT_KEY + "\",\"user\":\"" + USER + "recipient\":\"" + email + "\",\"msg\":\"" + text + "\"}";

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.addHeader("content-type", "application/json");
            request.setBody(mail.build());
            Response response = sg.api(request);
            int res = response.getStatusCode();
            System.out.println(res);
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());


            return res < 300  && res >= 200;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
