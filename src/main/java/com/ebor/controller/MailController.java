package com.ebor.controller;

import com.ebor.common.R;
import com.ebor.entity.MailRequest;
import com.ebor.service.MailService;
import com.ebor.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/sendEmail")
//@Api(value = "发送邮件接口",tags = {"发送邮件接口"})
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping("/simple")
    public void SendSimpleMessage(@RequestBody MailRequest mailRequest, HttpSession session) {
        String email = mailRequest.getSendTo();
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        mailRequest.setText("Welcome to use Ebor Food, your validation code is: "+ code);
        mailRequest.setSubject("Validation Code for Ebor Food");
        mailService.sendSimpleMail(mailRequest);

        session.setAttribute(mailRequest.getSendTo(), code);

        R.success("Successfully sent the validation code to your email, please check");
    }

    @PostMapping("/html")
    public void SendHtmlMessage(@RequestBody MailRequest mailRequest) { mailService.sendHtmlMail(mailRequest);}
}
