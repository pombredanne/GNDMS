package de.zib.gndms.logic.model.dspace;

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

import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.kit.config.ParameterTools;
import de.zib.gndms.logic.action.ActionConfigurer;
import de.zib.gndms.logic.model.ModelUpdateListener;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.GridResourceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * @date: 09.12.11
 * @time: 10:57
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public abstract class GridResourceDAO< G extends GridResource > {
    final private EntityManagerFactory emf;
    final private ActionConfigurer actionConfigurer;
    final private GridResourceCache< G > cache;

    final private Class< ? extends SetupAction< ConfigActionResult > > c;

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    public GridResourceDAO(
            final EntityManagerFactory emf,
            final GridResourceCache< G > provider,
            final Class< ? extends SetupAction< ConfigActionResult > > setupClazz ) {
        this.emf = emf;
        this.actionConfigurer = new ActionConfigurer( emf );
        this.actionConfigurer.setEntityUpdateListener( new Invalidator() );
        this.cache = provider;
        this.c = setupClazz;
    }

    public boolean exists( String id, Map< String, String > params ) {
        return cache.exists( id );
    }

    public G get( final String id ) {
        return cache.get( id );
    }

    public void create( final String id, final String config ) {
        setup( id, config, SetupMode.CREATE );
    }

    public void update( final String id, final String config ) {
        setup( id, config, SetupMode.UPDATE );
    }

    public List< G > list( Map< String, String > params ) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery( getListQuery() );
        return query.getResultList();
    }

    protected abstract String getListQuery( );

    private String setup( final String id, final String config, final SetupMode mode ) {
        try {
            final StringWriter sw = new StringWriter();

            final SetupAction< ? extends ConfigActionResult > setup_action = c.newInstance();
            setup_action.setPrintWriter(new PrintWriter(sw));
            setup_action.parseLocalOptions(config);
            setup_action.setMode( mode );

            ConfigActionResult result = setup_action.call();

            logger.info( sw.toString() );

            return result.toString();
        }
        catch( InstantiationException e ) {
            throw new IllegalStateException( "Cannot instantiate abstract class " + c.getCanonicalName() + ".", e );
        }
        catch( IllegalAccessException e ) {
            throw new IllegalStateException( "Cannot instantiate abstract class." + c.getCanonicalName() + ".", e );
        }
        catch (ParameterTools.ParameterParseException e) {
            throw new IllegalStateException( "Error on parsing parameter string '" + config + "'.", e );
        }
    }

    private class Invalidator implements ModelUpdateListener< GridResource > {
        public void onModelChange( GridResource model ) {
            GridResourceDAO.this.cache.invalidate( model.getId() );
        }
    }
}
