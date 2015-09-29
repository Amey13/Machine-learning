package mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EMailNotification
{
	static String USER_NAME="ibsadmininfo@gmail.com";
	static String PASSWORD="Techtyphoon";
	static String SMTP_Host="smtp.gmail.com";
	static String SMTP_PORT = "465";
	static String SSL_FACTORY="javax.net.ssl.SSLSocketFactory";
	
	public static void send(final String FROM,final String pass,String to,String sub,String message)
	{
		Properties props=new Properties();
		props.put("mail.smtp.host",SMTP_Host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");
		try
		{
			Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(FROM,pass);
			}
			});
			System.out.println("loggedIn");
			MimeMessage msg=new MimeMessage(session);
			msg.setText(message);
			msg.setSubject(sub);
			msg.setFrom(new InternetAddress(FROM));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			System.out.println("sending.....");
			Transport.send(msg);
			System.out.println("Message Sent");
		}
		catch(Exception e)
		{
			System.out.println("Error "+e);
		}
	}

	public static void sendEmail(String to,String subject,String message)
	{
		EMailNotification.send(USER_NAME,PASSWORD,to,subject,message);	
	}
	
	public static void main (String[] args)
    {
		//sendEmail();
    }
}