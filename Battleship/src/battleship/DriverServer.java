package battleship;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriverServer {
	
	public static void main(String[] args) throws Exception {
		try(ServerSocket listener = new ServerSocket(6112)) {
			System.out.println("Battleship Server has started... awaiting connection");
			ExecutorService pool = Executors.newFixedThreadPool(200);
			
			while(true) {
				Server server = new Server();
				pool.execute(server.new Player(listener.accept(), '1'));
				pool.execute(server.new Player(listener.accept(), '2'));
			}
		}
	}

}
