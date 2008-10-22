package de.zib.gndms.GORFX.c3grid;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.DeserializationException;
import org.joda.time.DateTime;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import types.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.rmi.RemoteException;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.10.2008 Time: 13:32:25
 */
@SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
public class ProviderStageInClient {
    private static final long MILLIS = 2000L;


    private ProviderStageInClient() {}


    @SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
    public static void main(String[] args) throws Exception {

        // Fetch Args
        final String gorfxEpUrl = args[0];
        final String sfrPropFile = args[1];

        // Create reusable context with pseudo DN
        final ContextT xsdContext = new ContextT();
        final ContextTEntry entry =
                GORFXTools.createContextEntry("Auth.DN", "Mr. Wichtig");
        xsdContext.setEntry(new ContextTEntry[] { entry });

        // Load SFR from Props and convert to XML objects
        final ProviderStageInORQT xsdArgs = loadSFRFromProps(sfrPropFile);

        // Open GORFX and create ORQ
        final EndpointReferenceType orqEPR = createORQ(gorfxEpUrl, xsdContext, xsdArgs);

        // Negotiate with ORQ for Offer
        final EndpointReferenceType offerEpr = createOffer(xsdContext, orqEPR);

        // Accept offer and thus create Task
        final TaskClient taskClient = acceptOfferAndCreateTask(offerEpr);

        waitForTaskToFinishOrFail(taskClient);
    }


    @SuppressWarnings({ "FeatureEnvy" })
    private static void waitForTaskToFinishOrFail(final TaskClient taskClientParam)
            throws RemoteException, DeserializationException {
        TaskExecutionState state;
        boolean finished;
        boolean failed;

        do {
            final GetResourcePropertyResponse rpResponse= taskClientParam.getResourceProperty(
                    TaskConstants.TASKEXECUTIONSTATE);
            state = (TaskExecutionState) ObjectDeserializer.toObject( rpResponse.get_any()[0], TaskExecutionState.class );
            failed = TaskStatusT.failed.equals(state.getStatus());
            finished = TaskStatusT.finished.equals(state.getStatus());
            try {
                Thread.sleep(MILLIS);
            }
            catch (InterruptedException e) {
                // intentionally
            }
        }
            while (! (failed || finished));


        // Write results to console
        if (finished) {
            final GetResourcePropertyResponse rpResponse= taskClientParam.getResourceProperty(TaskConstants.TASKEXECUTIONRESULTS);
            final ProviderStageInResultT result = (ProviderStageInResultT) ObjectDeserializer.toObject( rpResponse.get_any()[0], ProviderStageInResultT.class);
            System.out.println(result);
        }
        else {
            final GetResourcePropertyResponse rpResponse= taskClientParam.getResourceProperty(TaskConstants.TASKEXECUTIONFAILURE);
            final TaskExecutionFailure fail = (TaskExecutionFailure) ObjectDeserializer.toObject(rpResponse.get_any()[0], TaskExecutionFailure.class);
            throw new RuntimeException(fail.toString());
        }
    }


    private static TaskClient acceptOfferAndCreateTask(final EndpointReferenceType offerEprParam)
            throws URI.MalformedURIException, RemoteException {
        final OfferClient offerClient = new OfferClient(offerEprParam);
        final EndpointReferenceType taskEpr = offerClient.accept();

        // Poll task till completion or failure
        return new TaskClient(taskEpr);
    }


    private static EndpointReferenceType createOffer(
            final ContextT xsdContextParam, final EndpointReferenceType orqEPRParam)
            throws URI.MalformedURIException, RemoteException {
        final ORQClient orqPort = new ORQClient(orqEPRParam);

        final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();
        xsdOfferContract.setExecutionLikelyUntil(new DateTime().plusDays(1).toGregorianCalendar());
        xsdOfferContract.setResultValidUntil(new DateTime().plusDays(2).toGregorianCalendar());
        xsdOfferContract.setIfDecisionBefore(new DateTime().plusHours(1).toGregorianCalendar());
        xsdOfferContract.setConstantExecutionTime(true);
        return orqPort.getOfferAndDestroyRequest(xsdOfferContract, xsdContextParam);
    }


    private static EndpointReferenceType createORQ(
            final String gorfxEpUrlParam, final ContextT xsdContextParam,
            final ProviderStageInORQT xsdArgsParam)
            throws URI.MalformedURIException, RemoteException {
        GORFXClient gorfx = new GORFXClient(gorfxEpUrlParam);

        // Create ORQ via GORFX
        return gorfx.createOfferRequest(xsdArgsParam, xsdContextParam);
    }


    private static ProviderStageInORQT loadSFRFromProps(final String sfrPropFileParam)
            throws IOException {// Load SFR property file
        File propFile = new File(sfrPropFileParam);
        Properties sfrProps = new Properties();
        final FileInputStream stream = new FileInputStream(propFile);
        try {
            sfrProps.load(stream);
        }
        finally {
            stream.close();
        }

        // Create XSD ProviderStageInORQT from sfrPropFile
        final ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( sfrProps );
        reader.begin( );
        reader.read();
        ProviderStageInORQ sfrORQ = reader.getProduct();
        ProviderStageInORQXSDTypeWriter orqtWriter = new ProviderStageInORQXSDTypeWriter();
        ProviderStageInORQConverter orqConverter = new ProviderStageInORQConverter( orqtWriter, sfrORQ );
        orqConverter.convert( );
        return orqtWriter.getProduct();
    }
}