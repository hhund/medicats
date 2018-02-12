package de.gecko.medicats.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import de.gecko.medicats.alphaid.AlphaIdService;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.ops.OpsService;

@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "MEDICATS FHIR Server")
public class FhirServlet extends RestfulServer
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void initialize() throws ServletException
	{
		IcdService icdService = IcdService.getService();
		OpsService opsService = OpsService.getService();
		AlphaIdService alphaIdService = AlphaIdService.getService();
		
		List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
		resourceProviders.add(new CodeSystemProvider(icdService, opsService, alphaIdService));
		
		setResourceProviders(resourceProviders);
	}
}
