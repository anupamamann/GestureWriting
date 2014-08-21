package com.android.apps.server;

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
		int cTosPortNumber = 9876;
		String str;

		ServerSocket servSocket = new ServerSocket(cTosPortNumber);
		System.out.println("Waiting for a connection on " + cTosPortNumber);

		Socket fromClientSocket = servSocket.accept();
		PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(),
				true);

		BufferedReader br = new BufferedReader(new InputStreamReader(
				fromClientSocket.getInputStream()));

		while ((str = br.readLine()) != null) {
			System.out.println("The message: " + str);

			if (str.equals("bye")) {
				pw.println("bye");
				break;
			} else if (!str.trim().startsWith("msg:")) {
				// time to do some action
				List<String> commands = new ArrayList<String>();
				String[] cmdArgs = str.trim().split(" ");
				for (String cmdArg : cmdArgs) {
					commands.add(cmdArg);
				}
				ProcessBuilder builder = new ProcessBuilder(commands);

				Process process = null;
				try {
					process = builder.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				OutputStream stdin = process.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(stdin));

				try {
					writer.write("Android!");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		pw.close();
		br.close();

		fromClientSocket.close();
		servSocket.close();
	}

}
