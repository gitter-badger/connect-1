package siminov.connect.service.design;

import siminov.connect.model.ServiceDescriptor;

public interface IService extends IServiceEvents, IResource {

	public long getRequestId();

	
	public void setRequestId(long requestId);
	
	
	public String getService();
	
	
	public void setService(final String service);
	
	
	public String getApi();
	
	
	public void setApi(final String api);
	
	
	public ServiceDescriptor getServiceDescriptor();
	
	
	public void setServiceDescriptor(final ServiceDescriptor serviceDescriptor);

	
	public void invoke();
	
	
	public void terminate();
}