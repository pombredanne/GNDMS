package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:33:47
 */
public interface EntityUpdateListener<M extends GridResource> {
	void onModelChange( M model );
}