package de.zib.gndms.infra.dspace;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.logic.model.dspace.NoSuchElementException;
import de.zib.gndms.logic.model.dspace.SliceProvider;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.stuff.threading.PeriodicalJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Properties;

/**
 * @date: 01.02.12
 * @time: 11:59
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class SliceReaper extends PeriodicalJob {
    public static final String LIST_ALL_SLICES = "listAllSlices";

    private long period; // in milliseconds
    private String from;
    private String recipient;
    private String subject = "Slice Quota exceeded";
    private String smtpServer = "mailsrv2.zib.de";

    protected EntityManagerFactory emf;
    protected SliceProvider sliceProvider;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "SliceReaper";
    }

    protected void call() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery( LIST_ALL_SLICES );
        for( Object o : query.getResultList() ) {
            final Slice sliceModel = Slice.class.cast( o );
            final de.zib.gndms.infra.dspace.Slice slice = new de.zib.gndms.infra.dspace.Slice( sliceModel );

            if( null == slice ) {
                // this is not happening
                continue;
            }

            // perhaps, slice is too old
            if( slice.getTerminationTime().isBeforeNow() ) {
                if( onSliceTooOld( slice ) )
                    continue;
            }

            if( slice.getDiskUsage() > slice.getTotalStorageSize() )
                if( onSliceTooBig( slice ) )
                    continue;
        }
    }


    private boolean onSliceTooOld( Slice slice ) {
        try {
            sliceProvider.deleteSlice( slice.getSubspace().getId(), slice.getId() );
        } catch( NoSuchElementException e ) {
            logger.error( "Could not delete slice " + slice.getId(), e );
        }

        return true;
    }


    private boolean onSliceTooBig( de.zib.gndms.infra.dspace.Slice slice ) {
        boolean sentToOwner = false;
        try {
            // send that mail to owner too
            final String dn = slice.getOwner();

            int i = dn.indexOf( "E=" );
            if( -1 != i ) {
                i += 2;

                int j = dn.indexOf( "/", i );
                if( -1 == j )
                    j = dn.length();

                final String email = dn.subSequence( i, j ).toString();

                sendQuotaMail( email, slice );

                sentToOwner = true;
            }
        } catch( MessagingException e ) {
            logger.error( "Could not send message to " + slice.getOwner() + ". ", e );
        }

        try {
            if( sentToOwner )
                sendQuotaMail( recipient, slice, "\n\nAlso sent a message to owner." );
            else
                sendQuotaMail( recipient, slice, "\n\nA message has also been sent to system administrator." );
        }
        catch( MessagingException e ) {
            logger.error( "Could not send message to " + recipient + ".", e );
        }

        return false;
    }


    public void sendQuotaMail(
            final String recipient,
            final de.zib.gndms.infra.dspace.Slice slice )
            throws MessagingException
    {
        sendQuotaMail( recipient, slice, "" );
    }


    public void sendQuotaMail(
            final String recipient,
            final de.zib.gndms.infra.dspace.Slice slice,
            final String additionalMessage )
            throws MessagingException
    {
        final Properties props = new Properties();
        final Session session = Session.getDefaultInstance( props );
        final Message msg = new MimeMessage( session );
        props.put( "mail.smtp.host", smtpServer );

        final InternetAddress addressFrom = new InternetAddress( from );
        final InternetAddress addressTo = new InternetAddress( recipient );

        msg.setFrom( addressFrom );
        msg.setRecipient( Message.RecipientType.TO, addressTo );
        msg.setSubject( subject );
        msg.setContent( "Slice " +
                slice.getSubspace().getId() + "/" +
                slice.getKind().getId() + "/" + slice.getId() + " exceeds Quota.\n\n" +
                "Owner: " + slice.getOwner() + "\n" +
                "Size: " + slice.getTotalStorageSize() + " Bytes\n" +
                "DiskUsage: " + slice.getDiskUsage() + " Bytes\n" +
                "TerminationTime: " + slice.getTerminationTime() +
                additionalMessage,
                "text/plain" );
        Transport.send( msg );
    }


    public Long getPeriod() {
        return period;
    }


    public void setPeriod( Long period ) {
        this.period = period;
    }


    public String getSmtpServer() {
        return smtpServer;
    }


    public void setSmtpServer( String smtpServer ) {
        this.smtpServer = smtpServer;
    }


    public String getFrom() {
        return from;
    }


    public void setFrom( String from ) {
        this.from = from;
    }


    public String getRecipient() {
        return recipient;
    }


    public void setRecipient( String recipient ) {
        this.recipient = recipient;
    }


    public String getSubject() {
        return subject;
    }


    public void setSubject( String subject ) {
        this.subject = subject;
    }


    public EntityManagerFactory getEmf( ) {
        return emf;
    }


    @Inject
    public void setEmf( EntityManagerFactory emf ) {
        this.emf = emf;
    }


    public SliceProvider getSliceProvider() {
        return sliceProvider;
    }


    @Inject
    public void setSliceProvider(SliceProvider sliceProvider) {
        this.sliceProvider = sliceProvider;
    }
}
