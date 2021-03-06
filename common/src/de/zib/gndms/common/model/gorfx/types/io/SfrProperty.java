package de.zib.gndms.common.model.gorfx.types.io;
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


import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author langhammer 
 *
 */
public enum SfrProperty {
	JUST_ASK("c3grid.StageFileRequest.JustAsk", "true or false"),
	OBJECT_ITEMS("c3grid.StageFileRequest.ObjectList.Item"),
	LON_MIN("c3grid.StageFileRequest.SpaceConstr.Longitude.Min"),
	LON_MAX("c3grid.StageFileRequest.SpaceConstr.Longitude.Max"),
	LAT_MIN("c3grid.StageFileRequest.SpaceConstr.Latitude.Min"),
	LAT_MAX("c3grid.StageFileRequest.SpaceConstr.Latitude.Max"),
	ALT_MIN("c3grid.StageFileRequest.SpaceConstr.Altitude.Min"),
	ALT_MAX("c3grid.StageFileRequest.SpaceConstr.Altitude.Max"),
    ALT_RANGE("c3grid.StageFileRequest.SpaceConstr.Altitude.Range"),
	ALT_MINNAME("c3grid.StageFileRequest.SpaceConstr.Altitude.MinName"),
	ALT_MAXNAME("c3grid.StageFileRequest.SpaceConstr.Altitude.MaxName"),
	ALT_UNIT_MIN("c3grid.StageFileRequest.SpaceConstr.Altitude.MinUnit"),
	ALT_UNIT_MAX("c3grid.StageFileRequest.SpaceConstr.Altitude.MaxUnit"),
	ALT_VCRS("c3grid.StageFileRequest.SpaceConstr.Altitude.VerticalCRS"),
	TIME_MIN("c3grid.StageFileRequest.TimeConstr.MinTime"),
	TIME_MAX("c3grid.StageFileRequest.TimeConstr.MaxTime"),
    TIME_AGGREGATION("c3grid.StageFileRequest.TimeConstr.Aggregation"),
    TIME_MONTHRANGE("c3grid.StageFileRequest.TimeConstr.MonthRange"),
    TIME_DAYRANGE("c3grid.StageFileRequest.TimeConstr.DayRange"),
	CFLIST_OLD("c3grid.StageFileRequest.CFList"),
	CFLIST_ITEMS("c3grid.StageFileRequest.CFList.CFItem"),
	CONSTRAINT_LIST("c3grid.StageFileRequest.ConstraintList","<undocumented>"),
	FILE_FORMAT("c3grid.StageFileRequest.TargetFileFormat"),
	BASE_FILE("c3grid.StageFileRequest.TargetBaseDataFile"),
	META_FILE("c3grid.StageFileRequest.TargetMetaDataFile"),
	STAGING_TIME("c3grid.StageFileResponse.StagingTime","1234"),
    // G2 additions
    META_FILE_FORMAT("c3grid.StageFileRequest.TargetMetaFileFormat"),
    META_FILE_ARCHIVE_FORMAT("c3grid.StageFileRequest.TargetMetaFileArchiveFormat"),
    FILE_ARCHIVE_FORMAT("c3grid.StageFileRequest.TargetFileArchiveFormat"),
    JUST_DOWNLOAD("c3grid.StageFileRequest.JustDownload" ),
    AREA_CRS("c3grid.StageFileRequest.SpaceConstr.AreaCRS"),
    // slice stagein
    GRID_SITE("c3grid.SliceStageInRequest.GridSite"),

    // common data for muliple requests
    CONTEXT( "c3grid.CommonRequest.Context" ),
    GORFX_ID( "c3grid.CommonRequest.GORFXId" ),
	
	EST_MAX_SIZE("c3grid.StageFileRequest.Estimate.MaxSize","10000"),
	EST_IF_DECISION_BEFORE("c3grid.StageFileRequest.Estimate.IfDecisionBefore"),
	EST_EXEC_LIKELY_UNTIL("c3grid.StageFileRequest.Estimate.ExecutionLikelyUntil"),
	EST_RESULT_VALID_UNTIL("c3grid.StageFileRequest.Estimate.ResultValidUntil"),
    EST_REQUEST_INFO("c3grid.StageFileRequest.Estimate.RequestInfo");

    public final String key;
	public final String doc;
    
	SfrProperty(String key, String deflt) {
		this.key = key;
		doc = deflt;
	}
	SfrProperty(String key) {
		this.key = key;
		doc = null;
	}
    
	static void storeTemplateProperties(OutputStream out) {
		PrintStream o = new PrintStream(out);  
		for (SfrProperty s : SfrProperty.values()) {
			if (s.doc!= null) {
				o.println();
				o.println("## "+s.doc.trim().replaceAll("\n", "\n## "));
			}
			o.println("#"+s.key+"=");
		}
	}

}
