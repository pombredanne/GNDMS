package de.zib.gndms.dspace.slice.service;

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



import de.zib.gndms.dspace.slice.service.globus.resource.SliceResource;
import  de.zib.gndms.dspace.service.DSpaceConfiguration;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * Provides some simple accessors for the Impl.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public abstract class SliceImplBase {
	
	public SliceImplBase() throws RemoteException {
	
	}
	
	public DSpaceConfiguration getConfiguration() throws Exception {
		return DSpaceConfiguration.getConfiguration();
	}
	
	
	public de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome getResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("home");
		return (de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome)resource;
	}

	
	
	
	public de.zib.gndms.dspace.service.globus.resource.DSpaceResourceHome getDSpaceResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("dSpaceHome");
		return (de.zib.gndms.dspace.service.globus.resource.DSpaceResourceHome)resource;
	}
	
	public de.zib.gndms.dspace.subspace.service.globus.resource.SubspaceResourceHome getSubspaceResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("subspaceHome");
		return (de.zib.gndms.dspace.subspace.service.globus.resource.SubspaceResourceHome)resource;
	}
	
	
	protected ResourceHome getResourceHome(String resourceKey) throws Exception {
		MessageContext ctx = MessageContext.getCurrentContext();

		ResourceHome resourceHome = null;
		
		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/" + resourceKey;
		try {
			javax.naming.Context initialContext = new InitialContext();
			resourceHome = (ResourceHome) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate resource home. : " + resourceKey, e);
		}

		return resourceHome;
	}


}
