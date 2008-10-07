package de.zib.gndms.GORFX.ORQ.service.globus.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axis.message.addressing.AttributedURI;
import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.GNDMSTools;
import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.GORFX.service.globus.resource.ExtGORFXResourceHome;

import javax.naming.NamingException;

/**
 * This class overrides the ResourceHome that is automatically generated by introduce for Globus
 * Toolkit. In GNDMS this is mainly necessary to provide RDBMS/JPA-based resource persistence.
 * In order to use the extended resource home they have to be configured in jndi-config.xml.
 * If this has been done properly, you should see an info-level log message during the start up
 * of the web service container that notifies succesfull initialization of the extended resource
 * home.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 16.07.2008 Time: 12:35:27
 */
public final class ExtORQResourceHome extends ORQResourceHome {

    private boolean initialized;

    @NotNull
    private GNDMSystem system;

    @NotNull
    private AttributedURI serviceAddress;

    // logger can be an instance field since resource home classes are instantiated at most once
	@NotNull
	@SuppressWarnings({"FieldNameHidesFieldInSuperclass"})
	private final Log logger = LogFactory.getLog(ExtORQResourceHome.class);

	@Override
	public synchronized void initialize() throws Exception {
        if (! initialized) {
			logger.info("Extended ORQ home initializing");
			try { try {
                final GridConfig gridConfig = ExtGORFXResourceHome.getGridConfig();
                logger.debug("Config: " + gridConfig.asString());
                system = gridConfig.retrieveSystemReference();
				serviceAddress = GNDMSTools.getServiceAddressFromContext();

				initialized = true;

				super.initialize();    // Overridden method
			}
			catch ( NamingException e) {
				throw new RuntimeException(e);
			} }
            catch (RuntimeException e) {
                initialized = false;
                logger.error("Initialization failed", e);
                e.printStackTrace(System.err);
                throw e;
            }
		}
    }


    private void ensureInitialized() {
        try
        { initialize();	}
        catch (Exception e) {
            logger.error("Unexpected initialization error", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    protected Resource createNewInstance() throws ResourceException {

        ORQResource res = (ORQResource) super.createNewInstance();
        res.setHome( this );
        return res;
    }


    public GNDMSystem getSystem() {
        return system;
    }
}
