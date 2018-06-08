package com.aloestec.framework.cfg;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloestec.framework.bean.Result;
import com.aloestec.framework.util.ResultUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author caiwl
 * 2018-06-08 17:01:16
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Value("${app.send-error-mail}")
    private boolean sendMail;
    @Autowired
    private JavaMailSender mailSender;
    
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    private Result exception(HttpServletRequest request, Exception e) {
        log.error("GlobalExceptionHandler: {}", e);
        if (sendMail) {
            sendMail(e);
        }
        return ResultUtil.fail();
    }
    
    public void sendMail(Exception e) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("service@aloestec.com");
            helper.setTo("caiwenlong@aloestec.com");
            helper.setSubject("aloestec-framework-exception");
            helper.setText(print(e));
            mailSender.send(message);
        } catch (MessagingException me) {}
    }
    
    public String print(Exception e) {
        StringBuffer buff = new StringBuffer();
        buff.append(e.toString()).append("\r\n");
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            buff.append("        at ").append(traceElement).append("\r\n");
        }
        return buff.toString();
    }
}
