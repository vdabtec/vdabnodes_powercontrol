# VDAB Power Control Nodes
### Overview
This package includes Nodes with work with two type of power controller.
* [PowerUSB](http://www.pwrusb.com) 
* [WebPowerSwitch](https://dlidirect.com/) 

| | |
|  --- |  :---: |
| Application Page    | NA |
| Demo Web Link   | NA |

### Features
<ul>
<li>The <i>PowerUSBService</i> controls power for the PowerUSB product.
<li>The <i>PowerUSBMonitorService</i> reads power information from the PowerUSB product.
<li>The <i>WebPowerService</i> controls power for the WebPowerSwitch product.
</ul>

### Loading the the Package
The current or standard version can be loaded directly using the VDAB Android Client following the directions
for [Adding Packages](https://vdabtec.com/vdab/docs/VDABGUIDE_AddingPackages.pdf) 
and selecting the <i>PwrCtrlNodes</i> package.
 
A custom version can be built using Gradle following the direction below.

* Clone or Download this project from Github.
* Open a command windows from the <i>PwrCtrlNodes</i> directory.
* Build using Gradle: <pre>      gradle vdabPackage</pre>

This builds a package zip file which contains the components that need to be deployed. These can be deployed by 
manually unzipping these files as detailed in the [Server Updates](https://vdabtec.com/vdab/docs/VDABGUIDE_ServerUpdates.pdf) 
 documentation.

### Known Issues as of 24 Oct  2018

* Limited testing.


