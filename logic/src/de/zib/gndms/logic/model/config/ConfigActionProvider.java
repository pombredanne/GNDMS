/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.logic.model.config;

import de.zib.gndms.common.logic.config.ConfigMeta;
import de.zib.gndms.logic.action.ActionProvider;
import de.zib.gndms.logic.model.config.ConfigAction;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 19.07.11  15:35
 * @brief
 */
public interface ConfigActionProvider extends ActionProvider<ConfigMeta, ConfigAction> {

    String callConfigAction( String actionClassName, String args ) throws Exception;
}
