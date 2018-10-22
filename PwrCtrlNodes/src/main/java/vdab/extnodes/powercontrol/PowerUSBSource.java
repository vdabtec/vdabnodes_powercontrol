package vdab.extnodes.powercontrol;


import com.lcrc.af.polledsource.PolledServiceSource;
import com.lcrc.af.service.DirectoryService;

public class PowerUSBSource extends PolledServiceSource {

	// CONSTRUCTORS 
	public PowerUSBSource(){	
		super(new PowerUSBMonitorService());	
	}
	
}
