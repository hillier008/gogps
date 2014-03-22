/*
 * Copyright (c) 2010 Eugenio Realini, Mirko Reguzzoni, Cryms sagl - Switzerland. All Rights Reserved.
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
package org.gogpsproject.parser.nvs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;

import org.gogpsproject.EphGps;
import org.gogpsproject.IonoGps;
import org.gogpsproject.Observations;
import org.gogpsproject.StreamEventListener;
import org.gogpsproject.StreamEventProducer;
/**
 * <p>
 * Read and parse UBX messages
 * </p>
 *
 * @author Lorenzo Patocchi cryms.com
 */
public class NVSReader implements StreamEventProducer {
	private InputStream in;
	private Vector<StreamEventListener> streamEventListeners = new Vector<StreamEventListener>();
//	private StreamEventListener streamEventListener;

	public NVSReader(InputStream is){
		this(is,null);
	}
	public NVSReader(InputStream is, StreamEventListener eventListener){
		this.in = is;
		addStreamEventListener(eventListener);
	}

	public Object readMessagge() throws IOException, NVSException{

//		int data = in.read();
//		if(data == 0xB5){
			int data = in.read();
			//int data2 = in2.read();
			if(data == 0xf7){
				System.out.println("F7h");
				DecodeF7 decodeF7 = new DecodeF7(in);
				EphGps eph = decodeF7.decode();
				if(streamEventListeners!=null && eph!=null){
					for(StreamEventListener sel:streamEventListeners){
						sel.addEphemeris(eph);
					}
				}
				return eph;
						
			}else
			if (data == 0xf5){
				System.out.println("F5h");
				DecodeF5 decodeF5 = new DecodeF5(in);				
				Observations o = decodeF5.decode(null);
				if(streamEventListeners!=null && o!=null){
					for(StreamEventListener sel:streamEventListeners){
						Observations oc = (Observations)o.clone();
						sel.addObservations(oc);
					}
				}
				return o;
				
			}else
			if (data == 0x4a){
				System.out.println("4Ah");
				Decode4A decode4A = new Decode4A(in);
				IonoGps iono = decode4A.decode();
				if(streamEventListeners!=null && iono!=null){
					for(StreamEventListener sel:streamEventListeners){
						sel.addIonospheric(iono);
					}
				}
				return iono;

				
			}else
			if (data == 0x62){
				System.out.println("62h");
		
				
			}else{
				//System.out.println("Wrong Sync char 2 "+data+" "+Integer.toHexString(data)+" ["+((char)data)+"]");
			}
//		}else{
//			//no warning, may be NMEA
//			//System.out.println("Wrong Sync char 1 "+data+" "+Integer.toHexString(data)+" ["+((char)data)+"]");
//		}
			return null;
	}
	/**
	 * @return the streamEventListener
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<StreamEventListener> getStreamEventListeners() {
		return (Vector<StreamEventListener>)streamEventListeners.clone();
	}
	/**
	 * @param streamEventListener the streamEventListener to set
	 */
	@Override
	public void addStreamEventListener(StreamEventListener streamEventListener) {
		if(streamEventListener==null) return;
		if(!streamEventListeners.contains(streamEventListener))
			this.streamEventListeners.add(streamEventListener);
	}
	/* (non-Javadoc)
	 * @see org.gogpsproject.StreamEventProducer#removeStreamEventListener(org.gogpsproject.StreamEventListener)
	 */
	@Override
	public void removeStreamEventListener(
			StreamEventListener streamEventListener) {
		if(streamEventListener==null) return;
		if(streamEventListeners.contains(streamEventListener))
			this.streamEventListeners.remove(streamEventListener);
	}

}
