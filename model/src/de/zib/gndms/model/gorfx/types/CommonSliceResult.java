package de.zib.gndms.model.gorfx.types;

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



import de.zib.gndms.common.model.gorfx.types.AbstractTaskFlowResult;
import de.zib.gndms.common.rest.Specifier;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 31.10.2008, Time: 17:23:52
 */
public abstract class CommonSliceResult extends AbstractTaskFlowResult {

    private static final long serialVersionUID = -2235864812049517909L;
    public Specifier<Void> sliceRef;

    
    protected CommonSliceResult( String offerType ) {
        super( offerType );
    }


    public Specifier<Void> getSliceRef() {
        return sliceRef;
    }


    public void setSliceRef( Specifier<Void> sliceRef ) {
        this.sliceRef = sliceRef;
    }
}
