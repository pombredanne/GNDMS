package de.zib.gndms.model.common;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.model.ModelObject;

import javax.persistence.Embeddable;
import java.util.List;

/**
 * Abstract superclass of "virtual" endpoint references.
 *
 * Points to a resource on a runtime-resolved GridSite.
 *
 * Subclasses specialize by resource key type.
 *
 * No version field since instances are immutable
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 14:34:05
 */
@Embeddable
public abstract class VEPRef extends ModelObject {

    public abstract String getGridSiteId();
    public abstract void setGridSiteId(final String newSiteId);

    public abstract List<String> getResourceNames();
}