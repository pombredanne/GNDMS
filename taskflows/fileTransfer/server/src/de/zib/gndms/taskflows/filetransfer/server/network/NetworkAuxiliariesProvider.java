package de.zib.gndms.taskflows.filetransfer.server.network;

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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 10:51:58
 *
 * Usage of the network package for a file transfer:
 *
 * Use the GridFTPClientFactory provided by this class to create an GridFTPClient.
 * Given that this class is correctly set up and returns the desired factory, the GridFTPClient, should be configured to
 * your needs, e.g. with the correct security setup.
 *
 * Then create a GNDMSFileTransfer, and add a map with the files which should be transferred, the source and destination
 * directory and client to the instance.
 *
 * If it is of interest how big the download is, you can uses the GNDMSFileTransfer::estimateSize method to acquire the
 * size of the files in byte. Together with a descent implementation of the BandWidthEstimater interface this can be
 * used to predict the duration of a transfer, using the calculateTransferTime method of this class.
 *
 * To make your transfer reliable you should instantiate the persistentMarkerListener, supply it with a fresh instance of
 * a FTPTransferState, which must have a unique transferId. Then use the performPersistentFileTransfer method of your
 * GNDMSFileTransfer instance to trigger the transfer.
 *
 * An aborted transfer can be easily resumed, by setting the same GNDMSFileTransfer up again,
 * Loading the transfer state object from the database, setting it into a new persistentMarkerListener and calling
 * performPersistenFileTransfer with this listener again.
 *
 * See TransferStatTest::testIt for an code example of the described procedure.
 */
public class NetworkAuxiliariesProvider {

    private NetworkAuxiliariesProvider( )  { }

    // private final static Class<StrictNonblockingClientFactory> gridFTPClientFactoryClass = StrictNonblockingClientFactory.class;
    private final static Class<NonblockingClientFactory> gridFTPClientFactoryClass = NonblockingClientFactory.class;
    private final static GridFTPClientFactory gridFTPClientFactory;
    private final static BandWidthEstimater bandWidthEstimater = new StaticBandWidthEstimater();
    protected static Logger logger = LoggerFactory.getLogger( NetworkAuxiliariesProvider.class );


    private static Integer bufferSize = null;

    static {
        GridFTPClientFactory gridFTPClientFactory1;
        try {
            gridFTPClientFactory1 = gridFTPClientFactoryClass.newInstance();
        } catch ( InstantiationException e ) {
            gridFTPClientFactory1 = new NonblockingClientFactory(); // fallback
            logger.warn( "using fallback", e );
        } catch ( IllegalAccessException e ) {
            gridFTPClientFactory1 = new NonblockingClientFactory(); // fallback
            logger.warn( "using fallback", e );
        }
        gridFTPClientFactory = gridFTPClientFactory1;
    }

    public static GridFTPClientFactory getGridFTPClientFactory() {
        return gridFTPClientFactory;
    }


    public static BandWidthEstimater getBandWidthEstimater() {
        return bandWidthEstimater;
    }


    public static GNDMSFileTransfer newGNDMSFileTransfer() {
        GNDMSFileTransfer ft = new GNDMSFileTransfer();
        ft.setBufferSize( bufferSize );

        return ft;
    }

    /**
     * Calculated the transfer-time in milli-seconds.
     *
     * To catch possible rounding errors, which may occur when the transfer size is to small and the connection is
     * very fast. A minimal value for the transfer time can be provided (see the <EM>min</EM> parameter).
     *
     * @param size The transfer file size in byte.
     * @param bandWidth The bandwidth in byte/s
     * @param min The minimum time which is returned if the calculated time is smaller.
     *            If min is < 1 it will be ignored.
     *
     * @return The transfer-time in ms.
     */
    public static long calculateTransferTime( long size, float bandWidth, int min ){

        long tt = Float.valueOf( size * 1000 / bandWidth ).longValue( );

        if ( min > 0 && tt < min )
            tt = min;

        return tt;
    }


    public static Integer getBufferSize() {
        return bufferSize;
    }


    public static void setBufferSize( Integer bufferSize ) {

        logger.info( "received buffersize: "+ bufferSize );
        NetworkAuxiliariesProvider.bufferSize = bufferSize;
    }
}
