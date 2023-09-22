package com.pet.petproject.member.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailComponents {

  private final JavaMailSender javaMailSender;

  public boolean sendMail(String email, String subject, String text) {

    boolean result = false;

    MimeMessagePreparator msg = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
      }
    };
    try {
      javaMailSender.send(msg);
      return true;
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return result;
  }

}
