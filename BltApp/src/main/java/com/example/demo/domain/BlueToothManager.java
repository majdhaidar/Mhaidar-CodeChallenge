package com.example.demo.domain;

import java.awt.AWTException;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BlueToothManager{

	private LocalDevice localDevice; // local Bluetooth Manager
	private DiscoveryAgent discoveryAgent; // discovery agent
	private StreamConnectionNotifier scn;

	// Bluetooth Service name
	private static final String myServiceName = "MHaidarBtService";
	// Bluetooth Service UUID of interest
	//private static final String myServiceUUID = "2d26618601fb47c28d9f10b8ec891363";
	//private UUID MYSERVICEUUID_UUID = new UUID(myServiceUUID, false);
	private String MYSERVICEUUID_UUID = "0000110100001000800000805F9B34FB";

	private StreamConnection sc;
	
	Logger logger = LoggerFactory.getLogger(BlueToothManager.class);

	/**
	 * Initialize
	 */
	public void btInit() throws BluetoothStateException {

			localDevice = null;
			discoveryAgent = null;
			// Retrieve the local device to get to the Bluetooth Manager
			localDevice = LocalDevice.getLocalDevice();
			// Clients retrieve the discovery agent
			discoveryAgent = localDevice.getDiscoveryAgent();
	}

	// create an RFCOMM server connection:
	public void initServerConnection() throws IOException, AWTException {
		String connURL = "btspp://localhost:" + MYSERVICEUUID_UUID + ";name=" + myServiceName;
		scn = (StreamConnectionNotifier) Connector.open(connURL);
		//Thread to keep listening for any future connectivity
		Runnable runnable =createClientThreadRunnable();
		
		new Thread(runnable).start();
	}

	Runnable createClientThreadRunnable(){
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					while(true) {
						createStreamConnection();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	void createStreamConnection() throws Exception{
		//wait device to connect
		StreamConnection connection = scn.acceptAndOpen();
		logger.info("Accept client connection");
		//launch thread to handle requests from mobile
		ClientThread clientThread = new ClientThread(connection);
		clientThread.start();
		RemoteDevice rd = RemoteDevice.getRemoteDevice(connection);
		logger.info("New client connection... " + rd.getFriendlyName(false));
	}
	
	
	public void closeStreamConnection() throws IOException {
		sc.close();
	}

	public LocalDevice getLocalDevice() {
		return localDevice;
	}

	public void setLocalDevice(LocalDevice localDevice) {
		this.localDevice = localDevice;
	}

	public DiscoveryAgent getDiscoveryAgent() {
		return discoveryAgent;
	}

	public void setDiscoveryAgent(DiscoveryAgent discoveryAgent) {
		this.discoveryAgent = discoveryAgent;
	}

}
