package de.zib.gndms.gritserv.typecon.types;

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



import de.zib.gndms.model.dspace.types.SliceRef;
import types.SliceReference;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.MessageElement;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 14:30:28
 */
public class SliceRefXSDReader  {

    public static SliceRef read( SliceReference rf ) {

        return fromEPR( rf.getEndpointReference() );
    }


    public static SliceRef fromEPR( EndpointReferenceType epr  ) {

        SliceRef sr = new SliceRef();
        sr.setGridSiteId( epr.getAddress().toString( ) );
        MessageElement me = epr.getProperties().get( sr.getResourceKeyName() );
        try {
            sr.setResourceKeyValue( ( String ) me.getObjectValue( String.class ) );
        } catch ( Exception e ) {
            throw new IllegalArgumentException( e.getMessage(), e );
        }

        return sr;
    }
}
