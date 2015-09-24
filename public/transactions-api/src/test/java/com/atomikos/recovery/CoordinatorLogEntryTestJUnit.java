package com.atomikos.recovery;

import static org.junit.Assert.*;

import org.junit.Test;

import com.atomikos.icatch.TxState;

public class CoordinatorLogEntryTestJUnit {

	String tid = "TID";
	
	private CoordinatorLogEntry coordinatorLogEntry;

	
	 
	private void givenCoordinatorLogEntryWithParticipantStates(TxState... states) {
		coordinatorLogEntry = createCoordinatorLogEntryWithParticipantsInState(states);
	}
	
	
	
	private void thenCombinedStateIs(TxState expected) {
		TxState combined = coordinatorLogEntry.getCommitResult();
		assertEquals(expected, combined);
	}
	
	@Test
	public void atLeastOneCommittingParticipantMeansCommitResultCommitting() {
		givenCoordinatorLogEntryWithParticipantStates(TxState.HEUR_MIXED, TxState.COMMITTING);
		thenCombinedStateIs(TxState.COMMITTING);
	}
	
	@Test
	public void allParticipantsTerminatedMeansCommitResultTerminated() throws Exception {
		givenCoordinatorLogEntryWithParticipantStates(TxState.TERMINATED, TxState.TERMINATED);
		thenCombinedStateIs(TxState.TERMINATED);
	}
	
	
	@Test
	public void allParticipantsHeurHazardMeansCommitResultHeurHazard() throws Exception {
		givenCoordinatorLogEntryWithParticipantStates(TxState.HEUR_HAZARD, TxState.HEUR_HAZARD);
		thenCombinedStateIs(TxState.HEUR_HAZARD);
	}

	@Test
	public void allParticipantsHeurAbortedMeansCommitResultHeurAborted() throws Exception {
		givenCoordinatorLogEntryWithParticipantStates(TxState.HEUR_ABORTED, TxState.HEUR_ABORTED);
		thenCombinedStateIs(TxState.HEUR_ABORTED);
	}

	
	@Test
	public void defaultMeansCommitResultHeurMixed() throws Exception {
		
		givenCoordinatorLogEntryWithParticipantStates(TxState.TERMINATED, TxState.HEUR_HAZARD,TxState.HEUR_ABORTED);
		thenCombinedStateIs(TxState.HEUR_MIXED);
	}
	
	
	

	private CoordinatorLogEntry createCoordinatorLogEntryWithParticipantsInState(TxState... states) {
		ParticipantLogEntry[] participantDetails = new ParticipantLogEntry[states.length];
		for (int i = 0; i < participantDetails.length; i++) {
			participantDetails[i] = new ParticipantLogEntry(tid, "uri", 0,"description", states[i]);
		}
		return new CoordinatorLogEntry(tid,participantDetails);
	}
}
