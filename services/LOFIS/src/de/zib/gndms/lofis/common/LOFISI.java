package de.zib.gndms.lofis.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public interface LOFISI {

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException ;

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException ;

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException ;

  /**
   * Create LofiSet from slices containing all the files and optional storage slice references
   *
   * @param existingReplicaSlices
   * @param storageSlices
   * @param context
   * @throws MissingSourceFiles
   *	
   * @throws UnsupportedOrInvalidSlice
   *	
   */
  public de.zib.gndms.lofis.lofiset.stubs.types.LofiSetReference createLofiSet(types.ReplicaSlicesT existingReplicaSlices,types.SliceReference[] storageSlices,types.ContextT context) throws RemoteException, de.zib.gndms.lofis.stubs.types.MissingSourceFiles, de.zib.gndms.lofis.stubs.types.UnsupportedOrInvalidSlice ;

  /**
   * Merge multiple lofi sets into one LofiSet
   *
   * @param lofiSetRefs
   * @param context
   * @throws ConflictingLofiSetsInMerge
   *	
   */
  public de.zib.gndms.lofis.lofiset.client.LofiSetClient mergeLofiSets(de.zib.gndms.lofis.lofiset.stubs.types.LofiSetReference[] lofiSetRefs,types.ContextT context) throws RemoteException, org.apache.axis.types.URI.MalformedURIException, de.zib.gndms.lofis.stubs.types.ConflictingLofiSetsInMerge ;

  /**
   * Run maintenance actions
   *
   * @param action
   * @param options
   */
  public java.lang.Object callMaintenanceAction(java.lang.String action,types.ContextT options) throws RemoteException ;

  /**
   * Either retrieve a cached lofiset or create a new one
   *
   * @param cacheKey
   * @param existingReplicaSlices
   * @param storageSlices
   * @param context
   * @throws UnknownOrInvalidCacheKey
   *	
   * @throws MissingSourceFiles
   *	
   * @throws UnsupportedOrInvalidSlice
   *	
   */
  public de.zib.gndms.lofis.lofiset.client.LofiSetClient createOrRetrieveLofiSet(java.lang.Object cacheKey,types.ReplicaSlicesT existingReplicaSlices,types.SliceReference[] storageSlices,types.ContextT context) throws RemoteException, org.apache.axis.types.URI.MalformedURIException, de.zib.gndms.lofis.stubs.types.UnknownOrInvalidCacheKey, de.zib.gndms.lofis.stubs.types.MissingSourceFiles, de.zib.gndms.lofis.stubs.types.UnsupportedOrInvalidSlice ;

  /**
   * Retrieve a cached LofiSet if existing
   *
   * @param cacheKey
   * @param context
   * @throws UnknownOrInvalidCacheKey
   *	
   */
  public de.zib.gndms.lofis.lofiset.client.LofiSetClient retrieveLofiSet(java.lang.Object cacheKey,types.ContextT context) throws RemoteException, org.apache.axis.types.URI.MalformedURIException, de.zib.gndms.lofis.stubs.types.UnknownOrInvalidCacheKey ;

  /**
   * Calls MergeLofiSets and registers the result under the given cacheKey
   *
   * @param cacheKey
   * @param lofiSetRefs
   * @param context
   * @throws UnknownOrInvalidCacheKey
   *	
   * @throws ConflictingLofiSetsInMerge
   *	
   */
  public de.zib.gndms.lofis.lofiset.client.LofiSetClient mergeLofiSetsAndCacheNew(java.lang.Object cacheKey,de.zib.gndms.lofis.lofiset.stubs.types.LofiSetReference[] lofiSetRefs,types.ContextT context) throws RemoteException, org.apache.axis.types.URI.MalformedURIException, de.zib.gndms.lofis.stubs.types.UnknownOrInvalidCacheKey, de.zib.gndms.lofis.stubs.types.ConflictingLofiSetsInMerge ;

}

