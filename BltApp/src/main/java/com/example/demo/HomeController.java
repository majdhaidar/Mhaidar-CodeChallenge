package com.example.demo;

import java.awt.AWTException;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.domain.BlueToothManager;

@Controller
public class HomeController {
	private BlueToothManager bm = new BlueToothManager();
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/")
	public String home() {
		return "home.jsp";
	}
	
	@RequestMapping(value = "/bluetoothConnect", method=RequestMethod.GET)
	public ResponseEntity initBluetoothServer(HttpServletResponse response, HttpServletRequest request) throws IOException {	
		try {
			bm.btInit();
			bm.initServerConnection();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
			String msg = "Bluetooth Not Enabled On Machine";
			logger.error(msg);
			handleExceptionError(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body("Connected");
	}

	private void handleExceptionError(HttpServletRequest request, HttpServletResponse response) throws IOException {
		RequestDispatcher dd = request.getRequestDispatcher("/_error");
		try {
			dd.forward(request, response);
		} catch (ServletException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}	
	}

}
