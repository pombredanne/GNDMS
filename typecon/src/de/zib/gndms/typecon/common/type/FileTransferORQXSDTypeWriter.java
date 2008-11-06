package de.zib.gndms.typecon.common.type;

import types.FileTransferORQT;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQWriter;
import de.zib.gndms.typecon.common.GORFXClientTools;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.axis.types.URI;
import org.apache.axis.description.FieldDesc;
import org.apache.axis.message.MessageElement;

import javax.xml.soap.SOAPException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 14:37:42
 */
public class FileTransferORQXSDTypeWriter extends AbstractXSDTypeWriter<FileTransferORQT> implements FileTransferORQWriter {

    public void writeSourceURI( String uri ) {
        try {
            getProduct().get_any( )[0].setObjectValue( new URI( uri ) );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( URI.MalformedURIException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void writeDestinationURI( String uri ) {

        try {
            getProduct().get_any( )[1].setObjectValue( new URI( uri ) );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( URI.MalformedURIException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void writeFileMap( Map<String, String> fm ) {

        FieldDesc fd = FileTransferORQT.getTypeDesc().getFieldByName( "files" );
        ArrayList<MessageElement> al = new ArrayList<MessageElement>( Arrays.asList( getProduct().get_any() ) );

        try {
            al.add(
                GORFXClientTools.createElementForField( fd, XSDReadWriteAux.write( fm ) ) );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        getProduct().set_any( al.toArray( new MessageElement[al.size()] ) );

    }


    public void writeJustEstimate( boolean je ) {
        // Not required here
    }


    public void writeContext( HashMap<String, String> ctx ) {
        // Not required here
    }


    public void begin() {
       try {
            setProduct( GORFXClientTools.createEmptyFileTransferORQT() );
        } catch ( SOAPException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch ( InstantiationException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void done() {
        // Not required here
    }
}
