package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.network.GNDMSFileTransfer;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.ClientException;
import org.joda.time.DateTime;

import java.io.IOException;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 10:51:38
 */
public abstract class AbstractTransferORQCalculator<M extends FileTransferORQ, C extends AbstractORQCalculator<M, C>>
    extends AbstractORQCalculator<M,C> {

    private Long estimatedTransferSize; // estimatedTransferSize
    private Float estimatedBandWidth;


    protected AbstractTransferORQCalculator( Class<M> cls ) {
        super();
        super.setORQModelClass( cls );
    }


    public Contract createOffer() throws ServerException, IOException, ClientException {

        estimateTransferSize();
        estimateBandWidth();
        return calculateOffer( );
    }


    protected Long estimateTransferSize( ) throws ServerException, IOException, ClientException {

        GridFTPClient clnt = null;
        try {
            URI suri =  new URI( getORQArguments().getSourceURI() );
            clnt =  getNetAux().getGridFTPClientFactory().createClient( suri );
            GNDMSFileTransfer ft = new GNDMSFileTransfer();
            ft.setSourceClient( clnt );
            ft.setSourcePath( suri.getPath( ) );
            ft.setFiles( getORQArguments().getFileMap() );
            estimatedTransferSize = ft.estimateTransferSize(  );
        } finally {
            if ( clnt != null )
                clnt.close( true ); // none blocking close op
        }
        return estimatedTransferSize;
    }


    /**
     * PRECONDITION estimateTransferSize must have been called before.
     * @return The estimated transfer size or NULL if it wasn't estimated yet.
     */
    public Long getEstimatedTransferSize( ) throws ServerException, IOException, ClientException {
        return estimatedTransferSize;
    }


    /**
     * Estimates the bandwidth
     */
    protected Float estimateBandWidth( ) throws IOException {
        estimatedBandWidth = getNetAux().getBandWidthEstimater().estimateBandWidthFromTo(
            getORQArguments( ).getSourceURI(), getORQArguments( ).getTargetURI() );

        if( estimatedBandWidth == null )
            throw new IOException( "Couldn't estimate bandwidth." );

        return estimatedBandWidth;
    }


    /**
     * PRECONDITION estimateBandWidth must have been called before.
     * @return The band width NULL if it wasn't estimated yet.
     */
    public Float getEstimatedBandWidth( ) {
        return estimatedBandWidth;
    }


    /**
     * PRECONDITION estimateTransferSize and estimateBandWidth or their associated setters
     *              must have been called before.
     * @return The band width NULL if it wasn't estimated yet.
     */
    protected Contract calculateOffer( ) {
        
        DateTime dat = new DateTime( );
        dat = dat.plusSeconds(
            NetworkAuxiliariesProvider.calculateTransferTime( estimatedTransferSize,
                estimatedBandWidth.floatValue( ) ) );

        Contract ct = new Contract( );

        ct.setDeadline( dat.toGregorianCalendar( )  );
        ct.setDeadlineIsOffset( false );
        ct.setResultValidity( dat.plusHours( ContractConstants.FILE_TRANSFER_RESULT_VALIDITY ).toGregorianCalendar() );

        return ct;
    }


    /**
     * Use this method to set the download size manually.
     *
     * This is the alternativ to calling estimateTransferSize.
     *
     * @param estimatedTransferSize -- what the name implies.
     */
    protected void setEstimatedTransferSize( long estimatedTransferSize ) {
        this.estimatedTransferSize = estimatedTransferSize;
    }


    /**
     * Use this method to set the available band-width manually.
     *
     * This is the alternativ to calling estimateBandWidth.
     *
     * @param estimatedBandWidth -- Guess what.
     */
    protected void setEstimatedBandWidth( float estimatedBandWidth ) {
        this.estimatedBandWidth = estimatedBandWidth;
    }
}