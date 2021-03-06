package de.zib.gndms.dspace.service;

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

import de.zib.gndms.common.dspace.service.DSpaceService;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.logic.model.dspace.SubspaceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The dspace service implementation.
 * 
 * @author Ulrike Golas
 */

@Controller
@RequestMapping(value = "/dspace")
public class DSpaceServiceImpl implements DSpaceService {
    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * The base url, something like \c http://my.host.org/gndms/grid_id.
     */
    private String baseUrl;
    /**
     * Provider of available subspaces.
     */
    private SubspaceProvider subspaceProvider;
    /**
     * The uri factory.
     */
    private UriFactory uriFactory;

    /**
     * Initialization of the dspace service.
     */
    @PostConstruct
    public void init() {
        uriFactory = new UriFactory(baseUrl);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Secured( "ROLE_USER" )
    public ResponseEntity<List<Specifier<Void>>> listSubspaceSpecifiers(
            @RequestHeader("DN") final String dn) {
        if( subspaceProvider == null ) {
            logger.error( "Subspace provider not initialized" );
            throw new IllegalStateException( "No Subspace provider set in DSpaceService" );
        }

        GNDMSResponseHeader headers = new GNDMSResponseHeader();
        headers.setResourceURL(baseUrl + "/dspace/");
        headers.setParentURL(baseUrl);
        if (dn != null) {
            headers.setDN(dn);
        }
        List<Specifier<Void>> list = new ArrayList<Specifier<Void>>();
        HashMap<String, String> urimap = new HashMap<String, String>(2);
        urimap.put(UriFactory.SERVICE, "dspace");

        for (String subspaceId : subspaceProvider.list()) {
            Specifier< Void > spec = new Specifier<Void>();
            
            spec.setUriMap( new HashMap< String, String >( urimap ) );
            spec.addMapping( UriFactory.SUBSPACE, subspaceId );
            spec.setUrl( uriFactory.subspaceUri( spec.getUriMap(), null ) );
            list.add( spec );
        }
        return new ResponseEntity< List< Specifier< Void > > >(
                list, headers, HttpStatus.OK);
    }

    /**
     * Returns the base url of this dspace service.
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base url of this dspace service.
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns the subspace provider of this dspace service.
     * @return the subspaceProvider
     */
    public SubspaceProvider getSubspaceProvider() {
        return subspaceProvider;
    }

    /**
     * Sets the subspace provider of this dspace service.
     * @param subspaceProvider the subspaceProvider to set
     */
    @Inject
    public void setSubspaceProvider( final SubspaceProvider subspaceProvider ) {
        this.subspaceProvider = subspaceProvider;
    }
}
