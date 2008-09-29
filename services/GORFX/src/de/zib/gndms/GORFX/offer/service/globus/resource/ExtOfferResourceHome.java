package de.zib.gndms.GORFX.offer.service.globus.resource;

import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import de.zib.gndms.infra.system.GNDMSystem;

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
public final class ExtOfferResourceHome extends OfferResourceHome {

    private GNDMSystem system;
    
    // logger can be an instance field since resource home classes are instantiated at most once
	@NotNull
	@SuppressWarnings({"FieldNameHidesFieldInSuperclass"})
	private final Log logger = LogFactory.getLog(ExtOfferResourceHome.class);

	@Override
	public synchronized void initialize() throws Exception {
		super.initialize();    // Overridden method
		logger.info("Extension class initializing");
	}


    @Override
    protected Resource createNewInstance() throws ResourceException {

        OfferResource res = ( OfferResource ) super.createNewInstance();
        res.setHome( this );
        return res;
    }

    
    public GNDMSystem getSystem() {
        return system;
    }


    public void setSystem( GNDMSystem system ) {
        this.system = system;
    }
}
