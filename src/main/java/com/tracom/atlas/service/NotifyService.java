package com.tracom.atlas.service;

/**
 * Used to handle notifications either to email or remote clients
 * @author cornelius
 *
 */
public interface NotifyService {
	
	/**
	 * 
	 * @param emailAddress
	 * @param title
	 * @param message
	 */
	public void sendEmail(String emailAddress, String title, String message);

	public void sendEmailWithAttachment(String emailAddress, String title, String message, String pathToAttachment);
}
