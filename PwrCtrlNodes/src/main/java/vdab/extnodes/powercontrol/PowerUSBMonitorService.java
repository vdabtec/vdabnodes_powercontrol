package vdab.extnodes.powercontrol;

import com.lcrc.af.AnalysisCompoundData;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.constants.LogLevel;
import com.lcrc.af.datatypes.AFEnum;
import com.lcrc.af.service.CommandService;
import com.lcrc.af.util.StringUtility;


public class PowerUSBMonitorService extends  CommandService  {
	private Integer c_MonitorItem = MonitorItem.CURRENT ;
	private static AFEnum s_EnumPowerUSBMonitorItems = new AFEnum("MonitorItem")
	.addEntry(MonitorItem.CURRENT, "Current")
	.addEntry(MonitorItem.IOSTATUS, "IO Status")
	.addEntry(MonitorItem.CURRENT_POWERUSED, "Current and Power Used")
	.addEntry(MonitorItem.POWERUSED,"Power Used");
	public static class MonitorItem {
		public final static int CURRENT = 1;
		public final static int IOSTATUS  = 0;
		public final static int POWERUSED = 2;
		public final static int CURRENT_POWERUSED = 3;
	}
	public PowerUSBMonitorService (){
		super();
	}
	// ATTRIBUTE Methods
	public Integer get_MonitorItem(){
		return c_MonitorItem;
	}
	public void set_MonitorItem(Integer item){
		c_MonitorItem = item;
	}
	public synchronized void processEvent(AnalysisEvent ev){
		buildCommand();
		super.processEvent(ev);
	}
	// SUPPORTING Methods --------------------------------------
	private void buildCommand(){
		StringBuilder sb = new StringBuilder("PwrUsbCmd ");
		switch (c_MonitorItem){
		
		case MonitorItem.CURRENT:
		case MonitorItem.POWERUSED:
		case MonitorItem.CURRENT_POWERUSED:
			sb.append("c");
			break;
		
		case MonitorItem.IOSTATUS:
			sb.append("i");
			break;		
		}
		set_DropLines(1L);
		set_Command(sb.toString());
	}
	public void handleCommandReply(Object[] params, String line) {
		if (!isOK())
			return;
		String curStr = null;
		String powerStr = null;
		
		switch (c_MonitorItem){
		case MonitorItem.CURRENT:
		case MonitorItem.CURRENT_POWERUSED:
			curStr = StringUtility.locateBetween(line,"now(ma):","Total Power");
			break;
		}

		switch (c_MonitorItem){
		case MonitorItem.POWERUSED:
		case MonitorItem.CURRENT_POWERUSED:
			powerStr = StringUtility.locateBetween(line,"VAC):",null);
			break;
		}
		if (isLogLevel(LogLevel.TRACE))
			logInfo("PowerUSB return line="+ line+" current="+curStr+" poweruser="+powerStr);
		
		if (curStr != null || powerStr != null){
			AnalysisCompoundData powerData = new AnalysisCompoundData("Power");
			if (curStr != null )
				powerData.addAnalysisData("Current", Double.parseDouble(curStr));
			if (powerStr != null )
				powerData.addAnalysisData("Used", Double.parseDouble(powerStr));
			publish(new AnalysisEvent(this, powerData));
		}
	}
}
