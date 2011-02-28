package de.zib.gndms.GORFX.response;
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
 *          Date: 14.01.2011, Time: 17:37:52
 */
public class SimpleResponse {

    private String selfUrl;

    public SimpleResponse() {
    }

    public SimpleResponse( String selfUrl ) {
        this.selfUrl = selfUrl;
    }


    public String getSelfUrl() {
        return selfUrl;
    }


    public void setSelfUrl( String selfUrl ) {
        this.selfUrl = selfUrl;
    }
}