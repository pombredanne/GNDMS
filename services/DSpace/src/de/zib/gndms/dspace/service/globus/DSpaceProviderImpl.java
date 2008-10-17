package de.zib.gndms.dspace.service.globus;

import de.zib.gndms.dspace.service.DSpaceImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the DSpaceImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DSpaceProviderImpl{
	
	DSpaceImpl impl;
	
	public DSpaceProviderImpl() throws RemoteException {
		impl = new DSpaceImpl();
	}
	

    public de.zib.gndms.dspace.stubs.GetSubspaceResponse getSubspace(de.zib.gndms.dspace.stubs.GetSubspaceRequest params) throws RemoteException, de.zib.gndms.dspace.stubs.types.UnknownSubspace {
    de.zib.gndms.dspace.stubs.GetSubspaceResponse boxedResult = new de.zib.gndms.dspace.stubs.GetSubspaceResponse();
    boxedResult.setSubspaceReference(impl.getSubspace(params.getSubspaceSpecifier().getSubspaceSpecifier()));
    return boxedResult;
  }

    public de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse listPublicSubspaces(de.zib.gndms.dspace.stubs.ListPublicSubspacesRequest params) throws RemoteException {
    de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse boxedResult = new de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse();
    boxedResult.setSubspaceReference(impl.listPublicSubspaces(params.getSchemaURI()));
    return boxedResult;
  }

    public de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse listSupportedSchemas(de.zib.gndms.dspace.stubs.ListSupportedSchemasRequest params) throws RemoteException {
    de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse boxedResult = new de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse();
    boxedResult.setResponse(impl.listSupportedSchemas());
    return boxedResult;
  }

    public de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse createSliceInSubspace(de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequest params) throws RemoteException, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.stubs.types.UnknownSubspace, de.zib.gndms.dspace.stubs.types.InternalFailure {
    de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse boxedResult = new de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse();
    boxedResult.setSliceReference(impl.createSliceInSubspace(params.getSubspaceSpecifier().getSubspaceSpecifier(),params.getSliceCreationSpecifier().getSliceCreationSpecifier(),params.getContext().getContext()));
    return boxedResult;
  }

    public de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse callMaintenanceAction(de.zib.gndms.dspace.stubs.CallMaintenanceActionRequest params) throws RemoteException {
    de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse boxedResult = new de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse();
    boxedResult.setResponse(impl.callMaintenanceAction(params.getAction(),params.getOptions().getContext()));
    return boxedResult;
  }

}
