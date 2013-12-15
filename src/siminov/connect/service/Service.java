package siminov.connect.service;

import java.util.HashMap;
import java.util.Map;

import siminov.connect.model.ServiceDescriptor;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;

public abstract class Service implements IService {

	private String service = null;
	private String api = null;
	
	private Map<String, String> resources = new HashMap<String, String>();
	
	private ServiceDescriptor serviceDescriptor = null;
	
	
	public Service() {
	}

	public String getService() {
		return this.service;
	}
	
	public void setService(final String service) {
		this.service = service;
	}
	
	public String getApi() {
		return this.api;
	}
	
	public void setApi(final String api) {
		this.api = api;
	}
	
	public Map<String, String> getResources() {
		return this.resources;
	}
	
	public String getResource(final String name) {
		return this.resources.get(name);
	}

	public void addResource(final String name, final String value) {
		this.resources.put(name, value);
	}
	
	public boolean containResource(final String name) {
		return this.resources.containsKey(name);
	}

	public ServiceDescriptor getServiceDescriptor() {
		return this.serviceDescriptor;
	}
	
	public void setServiceDescriptor(final ServiceDescriptor serviceDescriptor) {
		this.serviceDescriptor = serviceDescriptor;
	}
	
	public void invoke() {
		
		ServiceHandler serviceHandler = ServiceHandler.getInstance();
		try {
			serviceHandler.handle(this);
		} catch(SiminovException se) {
			Log.loge(Service.class.getName(), "invoke", "SiminovException caught while invoking service, " + se.getMessage());
			
			this.onServiceTerminate(se);
		}
	}
}
