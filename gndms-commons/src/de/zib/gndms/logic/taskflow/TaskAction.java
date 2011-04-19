package de.zib.gndms.logic.taskflow;
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

import de.zib.gndms.model.gorfx.types.*;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  17:40
 *
 * @brief Provisional interface for TaskActions (old GNDMS already has them)
 */
public interface TaskAction<T extends Order> {

    void setTask( Task<T> t );
    Task getTask( );

    void onInit() throws Exception;
    void onProgress() throws Exception;
    void onFailed( Exception e );
    void run( ) throws Exception;
}