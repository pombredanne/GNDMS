package de.zib.gndms.dspace.service;

import de.zib.gndms.dspace.service.globus.resource.ExtDSpaceResourceHome;
import de.zib.gndms.infra.db.GNDMSystem;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DSpaceImpl extends DSpaceImplBase {

	private static final Logger logger;
	private final GNDMSystem system;

	static {
		logger = Logger.getLogger(DSpaceImpl.class);
	}

	@SuppressWarnings({"FeatureEnvy"})
	public DSpaceImpl() throws RemoteException {
		super();
		try {
			final ExtDSpaceResourceHome home = getResourceHome();
			system = home.getSystem();
			system.addInstance("dspaceImpl", this);
			system.addInstance("dspaceResource", home.find(null));
			system.addInstance("dspaceHome", home);
			system.addInstance("subspaceHome", getSubspaceResourceHome());
			system.addInstance("sliceHome", getSliceResourceHome());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ExtDSpaceResourceHome getResourceHome() throws Exception {
		return (ExtDSpaceResourceHome) super.getResourceHome();    // Overridden method
	}

	public de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference[] listPublicSubspaces(org.apache.axis.types.URI schemaURI) throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public org.apache.axis.types.URI[] listSupportedSchemas() throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference getSubspace(javax.xml.namespace.QName subspaceSpecifier) throws RemoteException, de.zib.gndms.dspace.stubs.types.UnknownSubspace {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public de.zib.gndms.dspace.slice.stubs.types.SliceReference createSliceInSubspace(javax.xml.namespace.QName subspaceSpecifier,types.SliceCreationSpecifier sliceCreationSpecifier,types.ContextT context) throws RemoteException, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.stubs.types.UnknownSubspace, de.zib.gndms.dspace.stubs.types.InternalFailure {
		org.apache.axis.message.addressing.EndpointReferenceType epr = new org.apache.axis.message.addressing.EndpointReferenceType();
		de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome home = null;
		org.globus.wsrf.ResourceKey resourceKey = null;
		org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
		String servicePath = ctx.getTargetService();
		String homeName = org.globus.wsrf.Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/" + "sliceHome";

		try {
			javax.naming.Context initialContext = new javax.naming.InitialContext();
			home = (de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome) initialContext.lookup(homeName);
			resourceKey = home.createResource();
			
			//  Grab the newly created resource
			de.zib.gndms.dspace.slice.service.globus.resource.SliceResource thisResource = (de.zib.gndms.dspace.slice.service.globus.resource.SliceResource)home.find(resourceKey);
			
			//  This is where the creator of this resource type can set whatever needs
			//  to be set on the resource so that it can function appropriatly  for instance
			//  if you want the resouce to only have the query string then there is where you would
			//  give it the query string.
			
			
			// sample of setting creator only security.  This will only allow the caller that created
			// this resource to be able to use it.
			//thisResource.setSecurityDescriptor(gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils.createCreatorOnlyResourceSecurityDescriptor());
			
			

			String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
			transportURL = transportURL.substring(0,transportURL.lastIndexOf('/') +1 );
			transportURL += "Slice";
			epr = org.globus.wsrf.utils.AddressingUtils.createEndpointReference(transportURL,resourceKey);
		} catch (Exception e) {
			throw new RemoteException("Error looking up Slice home:" + e.getMessage(), e);
		}

		//return the typed EPR
		de.zib.gndms.dspace.slice.stubs.types.SliceReference ref = new de.zib.gndms.dspace.slice.stubs.types.SliceReference();
		ref.setEndpointReference(epr);

		return ref;
  }

}

