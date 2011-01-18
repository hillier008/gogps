/*
 * Copyright (c) 2010, Lorenzo Patocchi. All Rights Reserved.
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
 */
package org.gogpsproject.parser.ublox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.ejml.data.SimpleMatrix;
import org.gogpsproject.Coordinates;
import org.gogpsproject.Observations;
import org.gogpsproject.ObservationsProducer;

/**
 * <p>
 * Read an UBX File
 * </p>
 *
 * @author Lorenzo Patocchi cryms.com
 */

public class UBXFileReader implements ObservationsProducer {

	private InputStream in;
	private UBXReader reader;
	private File file;
	private Observations obs = null;

	public UBXFileReader(File file) {
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see org.gogpsproject.ObservationsProducer#getApproxPosition()
	 */
	@Override
	public Coordinates getApproxPosition() {
		Coordinates coord = Coordinates.globalXYZInstance(0.0, 0.0, 0.0); //new Coordinates(new SimpleMatrix(3, 1));
		//coord.setXYZ(0.0, 0.0, 0.0 );
		coord.computeGeodetic();
		return coord;
	}

	/* (non-Javadoc)
	 * @see org.gogpsproject.ObservationsProducer#getCurrentObservations()
	 */
	@Override
	public Observations getCurrentObservations() {
		return obs;
	}

	/* (non-Javadoc)
	 * @see org.gogpsproject.ObservationsProducer#hasMoreObservations()
	 */
	public boolean hasMoreObservations() throws IOException {
		return in.available()>0;
	}

	/* (non-Javadoc)
	 * @see org.gogpsproject.ObservationsProducer#init()
	 */
	@Override
	public void init() throws Exception {
		this.in = new FileInputStream(file);

		this.reader = new UBXReader(in, null);
	}

	/* (non-Javadoc)
	 * @see org.gogpsproject.ObservationsProducer#nextObservations()
	 */
	@Override
	public Observations nextObservations() {
		try{
			while(in.available()>0){
				try{
					int data = in.read();
					if(data == 0xB5){
						Object o = reader.readMessagge();
						if(o instanceof Observations){
							return (Observations)o;
						}
					}else{
						//no warning, may be NMEA
						//System.out.println("Wrong Sync char 1 "+data+" "+Integer.toHexString(data)+" ["+((char)data)+"]");
					}
				}catch(UBXException ubxe){
					System.err.println(ubxe);
//					ubxe.printStackTrace();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gogpsproject.ObservationsProducer#release()
	 */
	@Override
	public void release() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}