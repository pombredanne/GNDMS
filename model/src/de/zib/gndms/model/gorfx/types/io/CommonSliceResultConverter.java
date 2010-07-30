package de.zib.gndms.model.gorfx.types.io;

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



import de.zib.gndms.model.gorfx.types.CommonSliceResult;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 13:35:40
 */
public abstract class CommonSliceResultConverter<W extends CommonSliceResultWriter, R extends CommonSliceResult> extends TaskResultConverter<W,R>{

    protected CommonSliceResultConverter() {
    }


    protected CommonSliceResultConverter( W writer, R model ) {
        super( writer, model );
    }


    /**
     * Note this neither calls begin nor end of the writer.
     * So you can place this call wherever you like.
     */
    public void convert() {

        SliceRefWriter wrt = getWriter( ).getSliceRefWriter();

        getWriter().beginWritingSliceRef( );
        SliceRefConverter src = new SliceRefConverter( wrt, getModel().getSliceRef() );
        src.convert();
        getWriter().doneWritingSliceRef( );
    }
}