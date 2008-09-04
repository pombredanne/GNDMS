package de.zib.gndms.dspace.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public interface DSpaceI {

  /**
   * Run maintenance actions
   *
   * @param action
   * @param options
   */
  public java.lang.Object callMaintenanceAction(java.lang.String action,types.ContextT options) throws RemoteException ;

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException ;

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException ;

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException ;

  /**
   * Returns the subspace for the given specifier
   *
   * @param subspaceSpecifier
   * @throws UnknownSubspace
   *	
   */
  public de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference getSubspace(javax.xml.namespace.QName subspaceSpecifier) throws RemoteException, de.zib.gndms.dspace.stubs.types.UnknownSubspace ;

  /**
   * Returns list of all publically anounced subspaces for the given schema
   *
   * @param schemaURI
   */
  public de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference[] listPublicSubspaces(org.apache.axis.types.URI schemaURI) throws RemoteException ;

  /**
   * Returns list of supported schemas
   *
   */
  public org.apache.axis.types.URI[] listSupportedSchemas() throws RemoteException ;

  /**
   * Create slice in subspace. Shortcut to avoid unneccesary extra communication roundtrip
   *
   * @param subspaceSpecifier
   * @param sliceCreationSpecifier
   * @param context
   * @throws OutOfSpace
   *	
   * @throws UnknownOrInvalidSliceKind
   *	
   * @throws UnknownSubspace
   *	
   * @throws InternalFailure
   *	
   */
  public de.zib.gndms.dspace.slice.client.SliceClient createSliceInSubspace(javax.xml.namespace.QName subspaceSpecifier,types.SliceCreationSpecifier sliceCreationSpecifier,types.ContextT context) throws RemoteException, org.apache.axis.types.URI.MalformedURIException, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.stubs.types.UnknownSubspace, de.zib.gndms.dspace.stubs.types.InternalFailure ;

}

