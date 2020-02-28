package com.certh.iti.easytv.stmm.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailHandler {
	
	public static void sendAttachmenentMail(String from, String to, String msg) throws AddressException, MessagingException, NoSuchAlgorithmException {
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		// Use Athens time zone to format the date in
		df.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
	
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "false");
		prop.put("mail.smtp.starttls.enable", "false");
		prop.put("mail.smtp.host", "193.178.235.20");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", "193.178.235.20");
		//prop.put("mail.debug", "true");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		
/*      final String username = "stan4@ethereal.email";
        final String password = "85hPvSPmP5hgv9KYvG";
		Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(username, password);
		    }
		});*/
		
		Session session = Session.getInstance(prop);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients( Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(df.format(date));
		message.setSentDate(new Date());
				
        // Create the message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        messageBodyPart.setText("resutls_"+df.format(date));

        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new DataSource() {

			@Override
			public String getContentType() {
				return "text/plain";
			}

			@Override
			public InputStream getInputStream() throws IOException {
		        ByteArrayInputStream stream = new ByteArrayInputStream(msg.getBytes());
				return stream;
			}

			@Override
			public String getName() {
				return "resutls_"+df.format(date);
			}

			@Override
			public OutputStream getOutputStream() throws IOException {
				return null;
			}
        	
        };
        
        
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("resutls_"+df.format(date));
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        message.setContent(multipart);
		
		Transport.send(message);
	}

}
