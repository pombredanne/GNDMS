package de.zib.gndms.rest;
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
 *          Date: 26.01.11, Time: 12:29
 *
 * @brief Class representing a facet of a rest resource.
 */
public class Facet {

    private String name; ///< The short name of the facet
    private String url;  ///< the complete url


    public Facet() {
    }


    public Facet( String name, String url ) {
        this.name = name;
        this.url = url;
    }


    public String getName() {
        return name;
    }


    public void setName( String name ) {
        this.name = name;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl( String url ) {
        this.url = url;
    }
}
