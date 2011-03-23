package de.zib.gndms.GORFX.client;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.GORFX.common.GORFXI;
import org.apache.axis.client.Stub;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.gsi.GlobusCredential;

import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS METHODS.
 *
 * This client is generated automatically by Introduce to provide a clean unwrapped API to the
 * service.
 *
 * On construction the class instance will contact the remote service and retrieve it's security
 * metadata description which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class GORFXClient extends GORFXClientBase implements GORFXI {	

	public GORFXClient(String url) throws MalformedURIException, RemoteException {
		this(url,null);
    }

	public GORFXClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(url,proxy);
        setupAuth( );
    }
	
	public GORFXClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
	   	this(epr,null);
	}
	
	public GORFXClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(epr,proxy);
        setupAuth( );
	}


   private void setupAuth() {

    }


    public static void usage(){
		System.out.println(GORFXClient.class.getName() + " -url <service url>");
	}
	
	public static void main(String [] args){
	    System.out.println("Running the Grid Service Client");
		try{
		if(!(args.length < 2)){
			if(args[0].equals("-url")){
			  GORFXClient client = new GORFXClient(args[1]);
			  // place client calls here if you want to use this main as a
			  // test....
			} else {
				usage();
				System.exit(1);
			}
		} else {
			usage();
			System.exit(1);
		}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getMultipleResourceProperties");
    return portType.getMultipleResourceProperties(params);
    }
  }

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getResourceProperty");
    return portType.getResourceProperty(params);
    }
  }

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"queryResourceProperties");
    return portType.queryResourceProperties(params);
    }
  }

  public org.apache.axis.message.addressing.EndpointReferenceType createOfferRequest(types.DynamicOfferDataSeqT offerRequestArguments,types.ContextT context) throws RemoteException, de.zib.gndms.GORFX.stubs.types.UnsupportedOfferType {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"createOfferRequest");
    //    System.out.println( "Stub properties (after configureStubSecurity): ");
    //    showStubProps();
    //    ( (Stub)portType )._setProperty( Constants.GSI_SEC_CONV, Constants.ENCRYPTION );
    //    System.out.println( "Stub properties (after manual propset): ");
    //    showStubProps();
    de.zib.gndms.GORFX.stubs.CreateOfferRequestRequest params = new de.zib.gndms.GORFX.stubs.CreateOfferRequestRequest();
    de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestOfferRequestArguments offerRequestArgumentsContainer = new de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestOfferRequestArguments();
    offerRequestArgumentsContainer.setOfferRequestArguments(offerRequestArguments);
    params.setOfferRequestArguments(offerRequestArgumentsContainer);
    de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestContext contextContainer = new de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestContext();
    contextContainer.setContext(context);
    params.setContext(contextContainer);
    de.zib.gndms.GORFX.stubs.CreateOfferRequestResponse boxedResult = portType.createOfferRequest(params);
    return boxedResult.getEndpointReference();
    }
  }

  public org.apache.axis.types.URI[] getSupportedOfferTypes(types.ContextT context) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getSupportedOfferTypes");
    de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesRequest params = new de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesRequest();
    de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesRequestContext contextContainer = new de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesRequestContext();
    contextContainer.setContext(context);
    params.setContext(contextContainer);
    de.zib.gndms.GORFX.stubs.GetSupportedOfferTypesResponse boxedResult = portType.getSupportedOfferTypes(params);
    return boxedResult.getResponse();
    }
  }

  public java.lang.Object callMaintenanceAction(java.lang.String action,types.ContextT options) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"callMaintenanceAction");
    de.zib.gndms.GORFX.stubs.CallMaintenanceActionRequest params = new de.zib.gndms.GORFX.stubs.CallMaintenanceActionRequest();
    params.setAction(action);
    de.zib.gndms.GORFX.stubs.CallMaintenanceActionRequestOptions optionsContainer = new de.zib.gndms.GORFX.stubs.CallMaintenanceActionRequestOptions();
    optionsContainer.setContext(options);
    params.setOptions(optionsContainer);
    de.zib.gndms.GORFX.stubs.CallMaintenanceActionResponse boxedResult = portType.callMaintenanceAction(params);
    return boxedResult.getResponse();
    }
  }


    public void showStubProps( ) {
        Iterator it =  ( ( Stub ) portType)._getPropertyNames();
        while ( it.hasNext() ) {
            String s = (String) it.next();
            System.out.println( s + " " + ( ( Stub ) portType)._getProperty( s ) );
        }
    }

}
