package de.zib.gndms.model.gorfx.repository;

import java.util.UUID;
/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

/**
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 10.01.2011, Time: 16:56:49
 */
public class TypedUUId {

    private final String typeSpecifier;
    private final UUID id;


    public TypedUUId( String typeSpecifier ) {
        this.typeSpecifier = typeSpecifier;
        this.id = UUID.randomUUID();
    }
    

    public TypedUUId( String typeSpecifier, UUID id ) {
        this.typeSpecifier = typeSpecifier;
        this.id = id;
    }


    public String getTypeSpecifier() {
        return typeSpecifier;
    }


    public UUID getId() {
        return id;
    }
}
