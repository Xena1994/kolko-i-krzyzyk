package serwer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    
    private final int port = 2027;
    private final int noConnections = 2;
    private LinkedList<Socket> users = new LinkedList<Socket>();
    private Boolean runda = true;
    private int G[][] = new int[3][3];
    private int rundy = 1;
       
    public void listing(){
        try {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    G[i][j] = -1;
                }
            }
            ServerSocket serwer = new ServerSocket(port,noConnections);
            System.out.println("Czekam na graczy ...");
            while(true){
                    Socket klient = serwer.accept();
                    users.add(klient);
                    int xo = rundy % 2 == 0 ? 1 : 0;
                    rundy++;
                    Runnable  run = new ThreadServer(klient,users,xo,G);
                    Thread hello = new Thread(run);
                    hello.start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Server serwer1= new Server();
        serwer1.listing();
    }
}
