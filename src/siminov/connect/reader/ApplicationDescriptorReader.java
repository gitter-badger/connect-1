
package siminov.connect.reader;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import siminov.connect.Constants;
import siminov.connect.model.ApplicationDescriptor;
import siminov.connect.model.AuthorizationDescriptor;
import siminov.connect.model.NotificationDescriptor;
import siminov.orm.exception.DeploymentException;
import siminov.orm.log.Log;
import siminov.orm.reader.SiminovSAXDefaultHandler;
import siminov.orm.resource.Resources;
import android.content.Context;


public class ApplicationDescriptorReader extends SiminovSAXDefaultHandler implements siminov.orm.Constants, Constants {

	private ApplicationDescriptor applicationDescriptor = null;
	
	private Resources resources = Resources.getInstance();

	private StringBuilder tempValue = new StringBuilder();
	private String propertyName = null;
	
	private AuthorizationDescriptor authorizationDescriptor = null;

	private NotificationDescriptor notificationDescriptor = null;	
	
	private boolean isAuthenticationDescriptor = false;
	private boolean isNotificationDescriptor = false;	

	public ApplicationDescriptorReader() {
		
		Context context = resources.getApplicationContext();
		if(context == null) {
			Log.error(getClass().getName(), "Constructor", "Invalid Application Context found.");
			throw new DeploymentException(getClass().getName(), "Constructor", "Invalid Application Context found.");
		}

		/*
		 * Parse ApplicationDescriptor.
		 */
		InputStream applicationDescriptorStream = null;
		
		try {
			applicationDescriptorStream = context.getAssets().open(APPLICATION_DESCRIPTOR_FILE_NAME);
		} catch(IOException ioException) {
			Log.error(getClass().getName(), "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.getMessage());
			throw new DeploymentException(getClass().getName(), "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.getMessage());
		}
		
		try {
			parseMessage(applicationDescriptorStream);
		} catch(Exception exception) {
			Log.error(getClass().getName(), "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.getMessage());
			throw new DeploymentException(getClass().getName(), "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.getMessage());
		}
	}
	
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		tempValue = new StringBuilder();
		
		if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_SIMINOV)) {
			applicationDescriptor = new ApplicationDescriptor();
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_PROPERTY)) {
			initializeProperty(attributes);
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_AUTHORIZATION_DESCRIPTOR)) {

			authorizationDescriptor = new AuthorizationDescriptor();
			isAuthenticationDescriptor = true;
		} else if(localName.equalsIgnoreCase(NOTIFICATION_DESCRIPTOR)) {

			isNotificationDescriptor = true;
			notificationDescriptor = new NotificationDescriptor();
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		
		String value = new String(ch,start,length);
		
		if(value == null || value.length() <= 0 || value.equalsIgnoreCase(NEW_LINE)) {
			return;
		}
		
		value = value.trim();
		tempValue.append(value);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		
		if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_PROPERTY)) {
			processProperty();
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR)) {
			applicationDescriptor.addDatabaseDescriptorPath(tempValue.toString());
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_EVENT_HANDLER)) {
			
			if(tempValue == null || tempValue.length() <= 0) {
				return;
			}
			
			applicationDescriptor.addEvent(tempValue.toString());
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_LIBRARY_DESCRIPTOR)) {
			
			if(tempValue == null || tempValue.length() <= 0) {
				return;
			}
			
			applicationDescriptor.addLibrary(tempValue.toString());
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_SERVICE_DESCRIPTOR)) {
			applicationDescriptor.addServiceDescriptorPath(tempValue.toString());
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_AUTHORIZATION_DESCRIPTOR)) {
			applicationDescriptor.setAuthorizationDescriptor(authorizationDescriptor);
			isAuthenticationDescriptor = false;
		} else if(localName.equalsIgnoreCase(SYNC_DESCRIPTOR)) {
			applicationDescriptor.addSyncDescriptorPath(tempValue.toString());
		} else if(localName.equalsIgnoreCase(NOTIFICATION_DESCRIPTOR)) {
			applicationDescriptor.setNotificationDescriptor(notificationDescriptor);
			isNotificationDescriptor = false;
		}
	}
	
	private void initializeProperty(final Attributes attributes) {
		propertyName = attributes.getValue(APPLICATION_DESCRIPTOR_NAME);
	}

	private void processProperty() {

		if(isNotificationDescriptor) {
			notificationDescriptor.addProperty(propertyName, tempValue.toString());
		} else if(isAuthenticationDescriptor) {
			authorizationDescriptor.addProperty(propertyName, tempValue.toString());
		} else {
			applicationDescriptor.addProperty(propertyName, tempValue.toString());
		}
	}	
	
	/**
	 * Get application descriptor object. 
	 * @return Application Descriptor Object.
	 */
	public ApplicationDescriptor getApplicationDescriptor() {
		return this.applicationDescriptor;
	}
}