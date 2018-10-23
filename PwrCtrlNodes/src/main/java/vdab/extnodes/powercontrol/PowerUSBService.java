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

import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.datatypes.AFEnum;
import com.lcrc.af.service.CommandService;

public class PowerUSBService extends  CommandService  {
	private int c_PowerCommand = PowerCommand.CONTROL;
	private int c_OutletNumber = Outlet.ALL;
	@SuppressWarnings("unused")
	private static AFEnum s_EnumPowerUSBCommands = new AFEnum("PowerCommand")
	.addEntry(PowerCommand.ON, "ON")
	.addEntry(PowerCommand.OFF, "OFF")
//	.addEntry(PowerCommand.TOGGLE,"TOGGLE")
	.addEntry(PowerCommand.CONTROL,"CONTROL");
	@SuppressWarnings("unused")
	private static AFEnum s_EnumOutletNumbers = new AFEnum("OutletNumber")
	.addEntry(Outlet.OUTLET1, "Outlet 1")
	.addEntry(Outlet.OUTLET2, "Outlet 2")
	.addEntry(Outlet.OUTLET3, "Outlet 3")
	.addEntry(Outlet.ALL,"All Outlets");

	public static class PowerCommand {
		public final static int ON = 1;
		public final static int OFF = 0;
		public final static int TOGGLE = 2;
		public final static int CONTROL = 3;
	}
	public static class Outlet {
		public final static int OUTLET1 = 1;
		public final static int OUTLET2 = 2;
		public final static int OUTLET3 = 3;
		public final static int ALL = 0;
	}
	public PowerUSBService (){
		super();
	}
	// ATTRIBUTE Methods
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
	public synchronized void processEvent(AnalysisEvent ev){
		AnalysisData ad = ev.getAnalysisData();
		switch (c_PowerCommand){
		case PowerCommand.ON:
			buildCommand(PowerCommand.ON);
			super.processEvent(ev);
			break;
			
		case PowerCommand.OFF:
			buildCommand(PowerCommand.OFF);
			super.processEvent(ev);
			break;		
			
		case PowerCommand.CONTROL:
			Boolean isOn  = ad.getDataAsBoolean();
			if (isOn != null){
				if (isOn.booleanValue())
					buildCommand(PowerCommand.ON);
				else 
					buildCommand(PowerCommand.OFF);
				super.processEvent(ev);
			}
			break;	
		}
	}

	// SUPPORTING Methods --------------------------------------
	private void buildCommand(int command){
		StringBuilder sb = new StringBuilder("PwrUsbCmd ");
		switch (c_OutletNumber){
		
		case Outlet.ALL:
			switch (command){
			case PowerCommand.ON:
				sb.append("1 1 1");
				break;
			case PowerCommand.OFF:
				sb.append("0 0 0");
				break;
			}
			break;
			
		case Outlet.OUTLET1:
		case Outlet.OUTLET2:
		case Outlet.OUTLET3:
			switch (command){
			case PowerCommand.ON:
				sb.append("s 1 ").append(c_OutletNumber).append(" 1");
				break;
			case PowerCommand.OFF:
				sb.append("s 1 ").append(c_OutletNumber).append(" 0");
				break;	
			}
			break;			
		}
		
		set_Command(sb.toString());
	}
	
}
