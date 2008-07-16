package de.zib.gndms.service;

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
public class WHORFXConfiguration implements ServiceConfiguration {

	public static WHORFXConfiguration  configuration = null;

	public static WHORFXConfiguration getConfiguration() throws Exception {
		if (WHORFXConfiguration.configuration != null) {
			return WHORFXConfiguration.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			WHORFXConfiguration.configuration = (WHORFXConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.", e);
		}

		return WHORFXConfiguration.configuration;
	}
	
	private String etcDirectoryPath;
	
	
	private String orfMappingFile;
	
	private String minimumUpdateInterval;
	
	private String gridJNDIEnv;
	
	private String gridName;

	private String gridPath;
	
	
	public String getEtcDirectoryPath() {
		return ContainerConfig.getBaseDirectory() + File.separator + etcDirectoryPath;
	}
	
	public void setEtcDirectoryPath(String etcDirectoryPath) {
		this.etcDirectoryPath = etcDirectoryPath;
	}

	
	public String getOrfMappingFile() {
		return ContainerConfig.getBaseDirectory() + File.separator + orfMappingFile;
	}
	
	
	public void setOrfMappingFile(String orfMappingFile) {
		this.orfMappingFile = orfMappingFile;
	}

	
	public String getMinimumUpdateInterval() {
		return minimumUpdateInterval;
	}
	
	
	public void setMinimumUpdateInterval(String minimumUpdateInterval) {
		this.minimumUpdateInterval = minimumUpdateInterval;
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
