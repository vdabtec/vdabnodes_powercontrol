/*LICENSE*
 * Copyright (C) 2013 - 2018 MJA Technology LLC 
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
package vdab.extnodes.powercontrol;

import vdab.api.node.HTTPService_A;

import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.datatypes.AFEnum;


public class WebPowerSwitchService extends  HTTPService_A {
	private Integer c_PowerCommand = Integer.valueOf(PowerCommand.CONTROL);
	private Integer c_OutletNumber = Integer.valueOf(Outlet.ALL);
	private int c_ExeCommand;
	@SuppressWarnings("unused")
	private static AFEnum s_EnumPowerUSBCommands = new AFEnum("WPS_PowerCommand")
	.addEntry(PowerCommand.ON, "ON")
	.addEntry(PowerCommand.OFF, "OFF")
	.addEntry(PowerCommand.TOGGLE,"TOGGLE")
	.addEntry(PowerCommand.CONTROL,"CONTROL");
	@SuppressWarnings("unused")
	private static AFEnum s_EnumOutletNumbers = new AFEnum("WPS_OutletNumber")
	.addEntry(Outlet.ALL,"All Outlets")
	.addEntry(Outlet.OUTLET1, "Outlet 1")
	.addEntry(Outlet.OUTLET2, "Outlet 2")
	.addEntry(Outlet.OUTLET3, "Outlet 3")
	.addEntry(Outlet.OUTLET4, "Outlet 4")
	.addEntry(Outlet.OUTLET5, "Outlet 5")
	.addEntry(Outlet.OUTLET6, "Outlet 6")
	.addEntry(Outlet.OUTLET7, "Outlet 7")
	.addEntry(Outlet.OUTLET8, "Outlet 8");

	public static class PowerCommand {
		public final static int ON = 1;
		public final static int OFF = 0;
		public final static int TOGGLE = 2;
		public final static int CONTROL = 3;
	}
	public static class Outlet {
		public final static int ALL = 0;
		public final static int OUTLET1 = 1;
		public final static int OUTLET2 = 2;
		public final static int OUTLET3 = 3;
		public final static int OUTLET4 = 4;
		public final static int OUTLET5 = 5;
		public final static int OUTLET6 = 6;	
		public final static int OUTLET7 = 7;
		public final static int OUTLET8 = 8;	
	}

	// ATTRIBUTE Methods
	public AnalysisDataDef def_SelectedElement(AnalysisDataDef theDataDef){	
		switch (c_PowerCommand.intValue()) {
		case PowerCommand.CONTROL:
			theDataDef.setStandard();
			break;
			
		default:
			theDataDef.setAdvanced();
			
		}
		return super.def_SelectedElement(theDataDef);
	}
	public Integer get_PowerCommand(){
		return c_PowerCommand;
	}
	public void set_PowerCommand(Integer cmd){
		c_PowerCommand = cmd;
	}
	public Integer get_OutletNumber(){
		return c_OutletNumber;
	}
	public void set_OutletNumber(Integer no){
		c_OutletNumber = no;
	}
	
	// SUPPORTING Methods --------------------------------------
	public String buildCompleteURL(AnalysisEvent ev){

		// Find out what the command should be.
		switch (c_PowerCommand.intValue()){
		case PowerCommand.ON:
		case PowerCommand.OFF:
		case PowerCommand.TOGGLE:
			c_ExeCommand = c_PowerCommand.intValue();
			break;				

		case PowerCommand.CONTROL:
			AnalysisData ad = getSelectedData(ev);
			if (ad == null){
				setError("CONTROL Command requires the selection of Boolean data");
				return null;
			}

			Boolean isOn  = ad.getDataAsBoolean();
			if (isOn == null){
				setError("CONTROL Command requires the selection of Boolean data");
				return null ;				
			}

			if (isOn.booleanValue())
				c_ExeCommand = PowerCommand.ON;
			else 
				c_ExeCommand = PowerCommand.OFF;
			break;	
		}
		StringBuilder sb = new StringBuilder(super.get_URL());
		switch (c_OutletNumber.intValue()){
		case Outlet.ALL:
			sb.append("/outlet?a");
			break;
			
		default:
			sb.append("/outlet?").append(c_OutletNumber);
			break;
		}
		
		switch (c_ExeCommand){
		case PowerCommand.OFF:
			sb.append("=OFF");
			break;
			
		case PowerCommand.ON:
			sb.append("=ON");
			break;
			
		case PowerCommand.TOGGLE:
			sb.append("=CCL");
			break;	
		}
		return(sb.toString());
	}

}
