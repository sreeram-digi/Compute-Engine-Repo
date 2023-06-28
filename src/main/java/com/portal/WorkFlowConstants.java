package com.portal;

public class WorkFlowConstants {

	public static final String workFlowConstants = 
			"Applied,PendingShortlist,AdminReject,ShortListed,NotShortListed,ScheduleRound1,ReEvaluateRound1"
			+ "ReScheduleRound1,SelectedRound1,NotSelectedRound1,ScheduleRound2,ReEvaluateRound2"
			+ "ReScheduleRound2,SelectedRound2,NotSelectedRound2,ScheduledHR,ReScheduleHR,Offered"
			+ "AcceptedOffer,Joined,Absconded,NotJoined,RejectedOffer,NotOffered,revertPreviousStage,droppedProfile,onHoldProfile"
			+ "reInitiateProfile,skipAction,reEvaluateAction,Applied-OnHold,Shortlisted-OnHold,SelectedRound1-OnHold,SelectedRound2-OnHold"
			+ "Offered-OnHold,Dropped";
	
	public static final String inputCriteria="1,2,3,4,5";
	
	public static final String inputSelection = "HR,SELECTOR";
	
	/**
     * constants for job graphs    
     */
    public static final String rejectedWorkflowConstants="AdminReject,NotShortListed,NotSelectedRound1,NotSelectedRound2,"
                                                            +"NotJoined,RejectedOffer,NotOffered,Dropped";

    public static final String selectedWorkflowConstants="Offered,Joined";

    public static final String appliedWorkflowConstants="Applied";
	
    public static final String appliedSelectedRejectedValues="Applied,Rejected,Selected";
}
