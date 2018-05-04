package cauCseNotice;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.*;
import javax.mail.internet.*;

import org.json.JSONException;

public class MailSender {
	private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	public static void main(String [] args){
		System.out.println("Now caucseNotice Mailing Server startup");		
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("sendMail Called at " + LocalDateTime.now());	
					sendMail(JsonParser.getPosts());
				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}
			}
		}, (5 - ((LocalDateTime.now().getMinute() - 2) % 5)), 5, TimeUnit.MINUTES);
	}

	private static void sendMail(ArrayList<Post> posts) throws IOException, JSONException {
		if(posts.isEmpty()) return;
		Properties properties = System.getProperties();
		setPropertiesForNaver(properties);
		final String username = JsonParser.getSenderAddress();
		final String password = JsonParser.getSenderPassword();
		Session session = null;
		// Get the default Session object.
		try{
			session = Session.getDefaultInstance(properties, 
					new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
		}catch(Exception e) {
			e.printStackTrace();
		}

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(username));
			setRecipients(message);
			message.setSubject("컴공 알리미 새 공지사항입니다.");
			message.setContent(generateContent(posts), "text/html; charset=UTF-8");
			Transport.send(message);
			System.out.println("Sent mails successfully!!");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}		
	}

	private static void setRecipients(Message message) throws AddressException, MessagingException, IOException {
		for(String toAddress : getMailingList()) {
			message.setRecipient(Message.RecipientType.BCC, new InternetAddress(toAddress));
		}
	}

	private static ArrayList<String> getMailingList() throws IOException {
		File file = new File("data/mailingList.txt");
		@SuppressWarnings("resource")
		Scanner input = new Scanner(file);
		ArrayList<String> list = new ArrayList<String>();

		while (input.hasNextLine()) {
			list.add(input.nextLine().trim());
		}
		return list;
	}

	private static String generateContent(ArrayList<Post> posts) {
		String result = new String();
		for(Post post : posts) {
			result += "[" + post.getType() + "] "
					+ "<a href='"+ Post.cleanXSS(post.getUrl()) + "'>" + Post.cleanXSS(post.getTitle()) + "</a><br>";
		}
		return result;
	}

	private static void setPropertiesForNaver(Properties properties) {
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.debug", "true");
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	}
}
