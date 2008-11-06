#!/bin/sh

STAGING_COMMAND="%{C3GRID_SOURCE}/scripts/ist-staging.sh"
ESTIMATION_COMMAND="%{C3GRID_SOURCE}/scripts/dummy-estimation.sh"
STAGING_AREA_PATH="/tmp/testss"
STAGING_AREA_SIZE="2000000" # Currently unused

source $(dirname $0)/check-hostname.sh
# One can set the $hn variable manually in the check-hostname script,
# if the returned value isn't the desired one.
#hn=csr-pc25.zib.de
STAGING_AREA_GSI_FTP_URL="gsiftp://$hn""$STAGING_AREA_PATH"



# Do not edit below this line unless very sure ---------------------------------------------------------------------------------------------------------------------------------------------------

MODE=$1

[ -z "$MODE" ] && echo "Must specify mode (CREATE; UPDATE; DELETE)!" && exit 1

ADDMODE=ADD
[ "$MODE" = "DELETE" ] && ADDMODE=REMOVE


# Variables in action parameters denoted using %{VARNAME} will be expanded 
# *at* *runtime* by the globus container using its regular shell environment

# Setup subspace and slicekinds

moni call -v .dspace.SetupSubspace "subspace:'{http://www.c3grid.de/G2/Subspace}SubspaceTests'; path:'$STAGING_AREA_PATH'; gsiFtpPath: '$STAGING_AREA_GSI_FTP_URL'; visible:true; size:'$STAGING_AREA_SIZE'; mode:'$MODE'"
moni call -v .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}SubspaceTests'; sliceKind: http://www.c3grid.de/G2/SliceKind/Staging; mode:'$ADDMODE'"
moni call -v .dspace.AssignSliceKind "subspace:'{http://www.c3grid.de/G2/Subspace}SubspaceTests'; sliceKind: http://www.c3grid.de/G2/SliceKind/DMS; mode:'$ADDMODE'"

moni call -v .gorfx.ConfigOfferType "offerType: 'http://www.c3grid.de/ORQTypes/ProviderStageIn';\
cfgOutFormat: 'PRINT_OK'; subspace: '{http://www.c3grid.de/G2/Subspace}SubspaceTests';\
sliceKind: 'http://www.c3grid.de/G2/SliceKind/Staging';\
stagingClass: 'de.zib.gndms.logic.model.gorfx.c3grid.ExternalProviderStageInAction';\
estimationClass: 'de.zib.gndms.logic.model.gorfx.c3grid.ExternalProviderStageInORQCalculator';\
stagingCommand='$STAGING_COMMAND';\
estimationCommand='$ESTIMATION_COMMAND'"
