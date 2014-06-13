/*
 * Copyright (c) 2010, Eugenio Realini, Mirko Reguzzoni, Cryms sagl - Switzerland. All Rights Reserved.
 *
 * This file is part of goGPS Project (goGPS).
 *
 * goGPS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * goGPS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with goGPS.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package org.gogpsproject;

/**
 * <p>
 * Satellite position class
 * </p>
 *
 * @author Eugenio Realini, Cryms.com
 */
public class SatellitePosition extends Coordinates{
	private int satID; /* Satellite ID number */
	private double satelliteClockError; /* Correction due to satellite clock error in seconds*/
	//private double range;
	private long unixTime;
	private boolean predicted;
	private boolean maneuver;

	public SatellitePosition(long unixTime, int satID, double x, double y, double z) {
		super();

		this.unixTime = unixTime;
		this.satID = satID;

		this.setXYZ(x, y, z);
	}

	/**
	 * @return the satID
	 */
	public int getSatID() {
		return satID;
	}

	/**
	 * @param satID the satID to set
	 */
	public void setSatID(int satID) {
		this.satID = satID;
	}

	/**
	 * @return the timeCorrection
	 */
	public double getSatelliteClockError() {
		return satelliteClockError;
	}

	/**
	 * @param timeCorrection the timeCorrection to set
	 */
	public void setSatelliteClockError(double timeCorrection) {
		this.satelliteClockError = timeCorrection;
	}

	/**
	 * @return the time
	 */
	public long getUtcTime() {
		return unixTime;
	}

	/**
	 * @param predicted the predicted to set
	 */
	public void setPredicted(boolean predicted) {
		this.predicted = predicted;
	}

	/**
	 * @return the predicted
	 */
	public boolean isPredicted() {
		return predicted;
	}

	/**
	 * @param maneuver the maneuver to set
	 */
	public void setManeuver(boolean maneuver) {
		this.maneuver = maneuver;
	}

	/**
	 * @return the maneuver
	 */
	public boolean isManeuver() {
		return maneuver;
	}

	public String toString(){
		return "X:"+this.getX()+" Y:"+this.getY()+" Z:"+getZ()+" clkCorr:"+getSatelliteClockError();
	}

	public Object clone(){
		SatellitePosition sp = new SatellitePosition(this.unixTime,this.satID,this.getX(),this.getY(),this.getZ());
		sp.maneuver = this.maneuver;
		sp.predicted = this.predicted;
		sp.satelliteClockError = this.satelliteClockError;
		return sp;
	}
}
