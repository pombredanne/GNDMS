#!/bin/sh

# uncomment the following lines, to simulate estimation script failure
#LOCKFILE="/tmp/unestimatable" 
#if [ -e "$LOCKFILE" ]; then
#    echo "OH NOOZ"
#    exit 1
#else 
#    touch $LOCKFILE
#fi

#cat - 

cat << EOF
# Value: ISO formatierte Zeitangabe / long
#c3grid.StageFileRequest.Estimate.IfDecisionBefore=2006-12-01T00:00:00.000Z

# Value: ISO formatierte Zeitangabe / long
# okay for dummy staging
c3grid.StageFileRequest.Estimate.ExecutionLikelyUntil=5000
 
# Value: ISO formatierte Zeitangabe / long
# keep result for 10 minutes
c3grid.StageFileRequest.Estimate.ResultValidUntil=600000
 
# Value: long
c3grid.StageFileRequest.Estimate.MaxSize=123456
EOF

# exit 255 if unfulfillable
# exit 254 if permission denied for DN

exit 0
