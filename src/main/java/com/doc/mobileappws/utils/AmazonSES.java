package com.doc.mobileappws.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.doc.mobileappws.dto.UserDto;

import javax.validation.constraints.Email;

/**
 * Amazon simple email service
 */
public class AmazonSES {
    // This address must be verified with Amazon SES.
    final String FROM = "4amazonws@gmail.com";

    // The subject line for the email.
    final String SUBJECT = "One last step to complete your registration with PhotoApp";

    // The HTML body for the email.
    final String HTMLBODY = "<h1>Please verify your email address</h1>"
            + "<p>Thank you for registering with our mobile app. To complete registration process and be able to log in,"
            + " click on the following link: "
            + "<a href='http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
            + "Final step to complete your registration" + "</a><br/><br/>"
            + "Thank you! And we are waiting for you inside!";

    // The email body for recipients with non-HTML email clients.
    final String TEXTBODY = "Please verify your email address. "
            + "Thank you for registering with our mobile app. To complete registration process and be able to log in,"
            + " open then the following URL in your browser window: "
            + " http://localhost:8080/verification-service/email-verification.html?token=$tokenValue"
            + " Thank you! And we are waiting for you inside!";

    /**
     * Send request to Amazon simple email Service (AWS SES) to send email (user must be authenticated on AWS)
     * @param userDto
     */
    public void verifyEmail(UserDto userDto) {

        // YOU SHOULD SET AWS PATH_VARIABLE. Or create .aws folder with credentials file. Or You can also set your keys this way. And it will work!
        //System.setProperty("aws.accessKeyId", "");
        //System.setProperty("aws.secretKey", "");

        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_2) // Regions is important
                .build();

        String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
        String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
                                .withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);

        client.sendEmail(request);

        System.out.println("Email sent!");

    }
}
