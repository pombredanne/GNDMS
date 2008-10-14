package de.zib.gndms.GORFX.service.globus;

import de.zib.gndms.GORFX.service.GORFXImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the GORFXImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class GORFXProviderImpl{
	
	GORFXImpl impl;
	
	public GORFXProviderImpl() throws RemoteException {
		impl = new GORFXImpl();
	}
	

    public de.zib.gndms.GORFX.stubs.CreateOfferRequestResponse createOfferRequest(de.zib.gndms.GORFX.stubs.CreateOfferRequestRequest params) throws RemoteException, de.zib.gndms.GORFX.stubs.types.UnsupportedOfferType {
    de.zib.gndms.GORFX.stubs.CreateOfferRequestResponse boxedResult = new de.zib.gndms.GORFX.stubs.CreateOfferRequestResponse();
    boxedResult.setEndpointReference(impl.createOfferRequest(params.getOfferRequestArguments().getOfferRequestArguments(),params.getContext().getContext()));
    return boxedResult;
  }

    public de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesResponse getSupportedOfferTypes(de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesRequest params) throws RemoteException {
    de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesResponse boxedResult = new de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesResponse();
    boxedResult.setResponse(impl.getSupportedOfferTypes(params.getContext().getContext()));
    return boxedResult;
  }

    public de.zib.gndms.GORFX.stubs.CallMaintenanceActionResponse callMaintenanceAction(de.zib.gndms.GORFX.stubs.CallMaintenanceActionRequest params) throws RemoteException {
    de.zib.gndms.GORFX.stubs.CallMaintenanceActionResponse boxedResult = new de.zib.gndms.GORFX.stubs.CallMaintenanceActionResponse();
    boxedResult.setResponse(impl.callMaintenanceAction(params.getAction(),params.getOptions().getContext()));
    return boxedResult;
  }

}
