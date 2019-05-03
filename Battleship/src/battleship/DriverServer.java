/*
 * DriverServer.java
 * @author - Jonathan Lee
 * @date - April 4th, 2019
 * 
 */

package battleship;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriverServer {
	
	// It's the driver method, vroom, vroom!
	public static void main(String[] args) throws Exception {
		try(ServerSocket listener = new ServerSocket(6112)) {
			System.out.println("Battleship Server has started... awaiting connection");
			ExecutorService pool = Executors.newFixedThreadPool(200);
			
			// Generates thread for each client
			while(true) {
				Server server = new Server();
				pool.execute(new Player(listener.accept(), '1'));
				System.out.println("Client Found!");
				pool.execute(new Player(listener.accept(), '2'));
				System.out.println("Client Found!");
			}
		}
	}

}
