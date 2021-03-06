package de.zib.gndms.common.mockup.logic.action;
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

import de.zib.gndms.common.logic.action.ActionMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 16.02.11  12:33
 * @brief Mockup-implementation
 *
 * @deprecated
 */
public class ActionProviderMockUp<M extends ActionMeta, A extends Action> implements ActionProvider<M,A> {

    private HashMap<String,M> actions;


    public List<String> listAvailableActions() {

        return new ArrayList<String>( actions.keySet() );
    }


    public M getMeta( String config ) {
        return actions.get( config );
    }


    public void setActions( List<M> actions ) {

        this.actions = new HashMap<String, M>( actions.size() );

        for( M cm : actions )
            this.actions.put( cm.getName(), cm );
    }


    public A getAction( String actionName ) {
        return null;
    }
}
