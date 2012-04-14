/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 11, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.data;

/**
 * This enum provides different Block types for a lecture. <br>
 * For each type a value called refreshSpirit is set. This can be used for the Simulation to decide what to do after
 * this block. <br>
 * For each type a value called simActive is set. So that it is known if for this block a simulation can be calculated
 * (for group work or breaks it is useless).
 * 
 * @author andres
 * 
 */
public enum BlockType {
	theory(true, 0),
	group(false, 1),
	pause(false, 2);
	
	private boolean	simActive;
	private int			refreshSpirit;
	
	
	BlockType(boolean simActive, int refreshSpirit) {
		this.setSimActive(simActive);
		this.setRefreshSpirit(refreshSpirit);
	}
	
	
	/**
	 * @return the refreshSpirit
	 */
	public int getRefreshSpirit() {
		return refreshSpirit;
	}
	
	
	/**
	 * @param refreshSpirit the refreshSpirit to set
	 */
	private void setRefreshSpirit(int refreshSpirit) {
		this.refreshSpirit = refreshSpirit;
	}
	
	
	/**
	 * @return the simActive
	 */
	public boolean getSimActive() {
		return simActive;
	}
	
	
	/**
	 * @param simActive the simActive to set
	 */
	public void setSimActive(boolean simActive) {
		this.simActive = simActive;
	}
}
