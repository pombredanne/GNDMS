package de.zib.gndms.GORFX.offer.service.globus;

import de.zib.gndms.GORFX.offer.service.OfferImpl;

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
public class OfferProviderImpl{
	
	OfferImpl impl;
	
	public OfferProviderImpl() throws RemoteException {
		impl = new OfferImpl();
	}
	

    public de.zib.gndms.GORFX.offer.stubs.AcceptResponse accept(de.zib.gndms.GORFX.offer.stubs.AcceptRequest params) throws RemoteException {
    de.zib.gndms.GORFX.offer.stubs.AcceptResponse boxedResult = new de.zib.gndms.GORFX.offer.stubs.AcceptResponse();
    boxedResult.setTaskReference(impl.accept());
    return boxedResult;
  }

}
