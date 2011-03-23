package de.zib.gndms.dspace.service;

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



import gov.nih.nci.cagrid.introduce.servicetools.ServiceConfiguration;
import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.config.ContainerConfig;

import javax.naming.InitialContext;
import java.io.File;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 * 
 * This class holds all service properties which were defined for the service to have
 * access to.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DSpaceConfiguration implements ServiceConfiguration {

	public static DSpaceConfiguration  configuration = null;

	public static DSpaceConfiguration getConfiguration() throws Exception {
		if (DSpaceConfiguration.configuration != null) {
			return DSpaceConfiguration.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			DSpaceConfiguration.configuration = (DSpaceConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.", e);
		}

		return DSpaceConfiguration.configuration;
	}
	
	private String etcDirectoryPath;


	private String gridJNDIEnv;

	private String gridName;

	private String gridPath;

	
	public String getEtcDirectoryPath() {
		return ContainerConfig.getBaseDirectory() + File.separator + etcDirectoryPath;
	}
	
	public void setEtcDirectoryPath(String etcDirectoryPath) {
		this.etcDirectoryPath = etcDirectoryPath;
	}

	public String getGridJNDIEnv() {
		return gridJNDIEnv;
	}


	public void setGridJNDIEnv(String gridJNDIEnv) {
		this.gridJNDIEnv = gridJNDIEnv;
	}


	public String getGridName() {
		return gridName;
	}


	public void setGridName(String gridName) {
		this.gridName = gridName;
	}


	public String getGridPath() {
		return ContainerConfig.getBaseDirectory() + File.separator + gridPath;
	}


	public void setGridPath(String gridPath) {
		this.gridPath = gridPath;
	}


}
