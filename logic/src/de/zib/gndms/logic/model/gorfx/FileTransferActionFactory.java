package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.factory.AbstractRecursiveFactory;
import de.zib.gndms.model.gorfx.OfferType;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.10.2008 Time: 12:50:48
 */
public class FileTransferActionFactory extends AbstractRecursiveFactory<OfferType, ORQTaskAction<?>>  {
    @Override
    public ORQTaskAction<?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return new FileTransferTaskAction();
    }
}
