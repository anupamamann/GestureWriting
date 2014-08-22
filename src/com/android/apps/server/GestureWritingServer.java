package com.android.apps.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GestureWritingServer {

	public static void main(String args[]) throws IOException,
			ClassNotFoundException {
		System.setProperty("java.awt.headless", "false");
		int cTosPortNumber = 9876;
		String str;

		ServerSocket servSocket = new ServerSocket(cTosPortNumber);
		System.out.println("Waiting for a connection on " + cTosPortNumber);

		Socket fromClientSocket = servSocket.accept();
		PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(),
				true);
		
		Process process = null;

		BufferedReader br = new BufferedReader(new InputStreamReader(
				fromClientSocket.getInputStream()));

		while ((str = br.readLine()) != null) {

			if (str.equals("bye")) {
				pw.println("bye");
				break;
			} else if (str.trim().equals("key:down")) {
				Robot robot;
				try {
					robot = new Robot();
					// Simulate a mouse click
			        robot.keyPress(KeyEvent.VK_DOWN);
			        robot.keyRelease(KeyEvent.VK_DOWN);
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (str.trim().equals("key:up")) {
				Robot robot;
				try {
					robot = new Robot();
					// Simulate a mouse click
			        robot.keyPress(KeyEvent.VK_UP);
			        robot.keyRelease(KeyEvent.VK_UP);
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (!str.trim().startsWith("msg:")) {
				// time to do some action
				List<String> commands = new ArrayList<String>();
				String[] cmdArgs = str.trim().split(" ");
				for (String cmdArg : cmdArgs) {
					commands.add(cmdArg);
				}
				ProcessBuilder builder = new ProcessBuilder(commands);

				try {
					process = builder.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				OutputStream stdin = process.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(stdin));

				try {
					writer.write("Android!\nAndroid\n\n\nAndroid\nAndroid");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				//System.out.println("The message: " + str);
			}
		}
		pw.close();
		br.close();

		fromClientSocket.close();
		servSocket.close();
	}

}
