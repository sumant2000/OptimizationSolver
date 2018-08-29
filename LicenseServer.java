package com.license.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LicenseServer {

	//  ServerSocket for license monitoring.
	private static ServerSocket server;
	// socket server port on which it will listen
	private static int port = 9999;

	// number of licenses
	private static int licenseCount = 5;

	public static void main(String args[]) throws IOException, ClassNotFoundException {
		
		// create the socket server object
		InetAddress addr = InetAddress.getByName("localhost");
		server = new ServerSocket(port,50,addr);
		
		// keep listens indefinitely until receives 'exit' call or program terminates
		while (true) {
			System.out.println("Listening to client requesting for license....");
			// creating socket and waiting for client connection
			Socket socket = server.accept();
			// read from socket to ObjectInputStream object
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			// convert ObjectInputStream object to String
			String message = (String) ois.readObject();
			System.out.println("Request for License  Received: " + message);
			// create ObjectOutputStream object
			licenseCount--;
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			// write object to Socket
			if (licenseCount <= 0) {
				oos.writeObject("Hi Client , no license available " + message);
			} else
				oos.writeObject("Hi Client serving license" + message);

			// close resources
			ois.close();
			oos.close();
			socket.close();
			// terminate the server if client sends exit request
			if (message.equalsIgnoreCase("exit"))
				break;
		}
		System.out.println("Shutting down Socket server!!");
		// close the ServerSocket object
		server.close();
	}

}
