package de.zib.gndms.logic.model.dspace;

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



import de.zib.gndms.common.dspace.SubspaceConfiguration;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.ModelChangedAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * An Action to manage Subspaces with their corresponding MetaSubspaces in the database.
 *
 * The id (defined in GridResource) represents the name of the subspace. It must be set in the configuration map and
 *  will be retrieved during the initialization.
 *
 * <p>When this action is started with <tt>create</tt> or <tt>update</tt> as SetupMode, it will retrieve
 * the MetaSubspace entity and the linked Subspace instance and (re)sets their fields using the getter methods of this class.
 * If necessary, it may create a new MetaSubspace or Subspace instance and link them on <tt>create</tt> mode.
 *
 * <p>Note: <tt>read</tt> mode is not supported.
 *
 * <p>An instance of this class returns a {@code ConfigActionResult} informing about the success of its execution, when
 * the <tt>execute()</tt> method is called.
 *

 * @see Subspace
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 17:37:51
 */
@ConfigActionHelp(shortHelp = "Setup a subspace", longHelp = "Used to prepare the database schema for GNDMS by creating and updating subspaces")
public class SetupSubspaceAction extends SetupAction<ConfigActionResult> {
    private final Logger log = LoggerFactory.getLogger( this.getClass() );

    @ConfigOption(descr="The name/key of the subspace")
    private String subspace;

    @ConfigOption(descr="Local filesystem root path for all slices stored in this subspace")
    private String path;

    @ConfigOption(descr="GridFTP path for all slices stored under path")
    private String gsiFtpPath;

    @ConfigOption(descr="If set, this subspace is included in the public service listing")
    private Boolean visible;

    @ConfigOption(descr="Maximum storage size available in this subspace")
    private Long size;

    public SetupSubspaceAction() {
		super();
    }

    public SetupSubspaceAction(SubspaceConfiguration subspaceConfig) {
		super();
		setPath(subspaceConfig.getPath());
		setIsVisibleToPublic(subspaceConfig.isVisible());
		setGsiFtpPath(subspaceConfig.getGsiFtpPath());
		setMode(subspaceConfig.getMode());
		setSize(subspaceConfig.getSize());
        setSubspace( subspaceConfig.getSubspace() );
	}


/**
    * Calls <tt>super.initialize()</tt> and retrieves several field values from the configuration map,
    * if SetupMode is <tt>create</tt>.
    * The option 'subspace' must be set anyway.
    * 
    */
    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (subspace == null && (isCreating() || hasOption("subspace")))
                setSubspace(getOption("subspace"));
            if (visible == null && (isCreating() || hasOption("visible")))
                setIsVisibleToPublic(isBooleanOptionSet("visible", true));
            if (size == null && (isCreating() || hasOption("size"))) {
                size = new Long( getLongOption("size") );
            }
            if (path == null && (isCreating() || hasOption("path"))) {
                setPath(getOption("path"));
            }
            // TODO: gridftp is not required in future
            if (gsiFtpPath== null && (isCreating() || hasOption("gsiFtpPath"))) {
                setGsiFtpPath(getOption("gsiFtpPath"));
            }
        }
        catch ( MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }

        requireParameter("subspace", subspace);
    }


    /**
     *  Tries to retrieve the MetaSubspace entity with the primary key <tt>getSubspace()</tt> from the entityclass {@code MetaSubspace.class}
     *  managed by {@code em} and a corresponding <tt>Subspace</tt> instance.
     *
     *  <p>If <tt>SetupMode</tt> is set to <tt>create</tt> and if necessary,
     *  it creates a new Subspace or MetaSubspcae instance and link them.
     *  The fields of the entity and the corresponding Subspace instance
     *  are then (re)set using the corresponding getter methods of this class.
     *  Makes both instances managed and persistent by the EntityManager, if not already done.
     *
     *  <p> Removes both instances from the EntityManager, if SetupMode is <tt>delete</tt>
     *
     *  <p> Adds a new <tt>ModelChangedAction(subspace)</tt> to the postponedActions of this action instance. 
     * 
     * @param em an EntityManager managing MetaSubspace and Subspace entities.
     * @param writer
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    @SuppressWarnings({ "FeatureEnvy", "MethodWithMoreThanThreeNegations" })
    @Override
    public ConfigActionResult execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        Subspace space = prepareSubspace(em, subspace);

        try {
        switch (getMode()) {
            case CREATE:
            case UPDATE:
                if (isVisibleToPublic() != null)
                    space.setVisibleToPublic(isVisibleToPublic());

                if (size != null) {
                    final long diff = getSize() - space.getTotalSize();
                    space.setTotalSize( getSize() );
                    space.setAvailableSize( space.getAvailableSize() + diff );
                }

                if (path != null)
                    space.setPath(getPath());

                if (gsiFtpPath != null)
                    space.setGsiFtpPath(getGsiFtpPath());

                if (! em.contains(space))
                    em.persist(space);
                break;
            case DELETE:
                throw new UnsupportedOperationException("Use DeleteSubspaceTaskAction instead of SetupSubspaceAction for deleting subspaces");
            	// em.remove(meta);
                // em.remove(subspace);
                // break;
        }

        } catch ( Exception e ) {
           throw new RuntimeException( e ); 
        }
        // Register resources that require refreshing
        getPostponedEntityActions().addAction(new ModelChangedAction(space));

        return ok();
    }

    /**
     * Tries to retrieve the entity instance with the primary key {@code pkParam} from the entityclass {@code Subspace.class}.
     * If not <tt>null</tt> it will be returned. Otherwise a new <tt>Subspace</tt> instance is created,
     * with <tt>pkParam</tt> as its ScopedName.
     *
     * @param em
     * @param pkParam
     * @return
     */
    private Subspace prepareSubspace(final EntityManager em, final String pkParam) {
        Subspace space = em.find(Subspace.class, pkParam);
        if (space == null) {
            if (! isCreating())
                throw new IllegalStateException("No matching subspace found for update");
            space = new Subspace();
        }

        
        if (isCreating()) {
            space = new Subspace();
            space.setId( pkParam );

            //final StorageSize avail = new StorageSize();
            //avail.setAmount(getSize().getAmount());
            //avail.setUnit(getSize().getUnit());
            //subspace.setAvailableSize(avail);
            final long size = getSize( );
            space.setAvailableSize( size );
            space.setTotalSize( size );
        }

        return space;

    }

    public String getSubspace() {
        return subspace;
    }


    public void setSubspace(final String subspaceParam) {
        subspace = subspaceParam;
    }


    @SuppressWarnings({ "NonBooleanMethodNameMayNotStartWithQuestion" })
    public Boolean isVisibleToPublic() {
        return visible;
    }

    public long getSize() {
        return size;
    }


    public void setIsVisibleToPublic(final Boolean visibleToPublicParam) {
        visible = visibleToPublicParam;
    }


    public void setSize(final long sizeParam) {
        size = sizeParam;
    }


    public String getPath() {
        return path;
    }

    public void setPath(final String pathParam) {
        path = pathParam;
    }


    public String getGsiFtpPath() {
        return gsiFtpPath;
    }


    public void setGsiFtpPath(final String gsiFtpPathParam) {
        gsiFtpPath = gsiFtpPathParam;
    }
}
