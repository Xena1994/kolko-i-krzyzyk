
package serwer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;


public class ThreadServer implements Runnable{
    
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    
    private int XO;
    
    private int G[][];
    
    private boolean runda;

    private LinkedList<Socket> usersList = new LinkedList<Socket>();
    
    
    public ThreadServer(Socket soc,LinkedList users,int xo,int[][] Ga){
        socket = soc;
        usersList = users;
        XO = xo;
        G = Ga;
    }
    
    
    @Override
    public void run() {
        try {
            
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            runda = XO == 1;
            String msg = "";
            msg += "TY_GRASZ: " + (runda ? "X;":"O;");
            msg += runda;
            out.writeUTF(msg);
            
            while(true){
                
                String odebrane = in.readUTF();
                String odebrany [] = odebrane.split(";");

                
                int f = Integer.parseInt(odebrany[0]);
                int c = Integer.parseInt(odebrany[1]);

                G[f][c] = XO;

                String cad = "";
                cad += XO+";";
                cad += f+";";
                cad += c+";";
                
                boolean winner = wygrana(XO);
                boolean pelny = full();
                
                if(!winner && !pelny){
                    cad += "NIKT";
                }
                else if(!winner && pelny){
                    cad += "REMIS";
                }
                else if(winner){
                    emptyMatrix();
                    cad += XO == 1 ? "X":"O";
                }
                
                
                
                for (Socket uzytkownik : usersList) {
                    out = new DataOutputStream(uzytkownik.getOutputStream());
                    out.writeUTF(cad);
                }
            }
        } catch (Exception e) {
            

            for (int i = 0; i < usersList.size(); i++) {
                if(usersList.get(i) == socket){
                    usersList.remove(i);
                    break;
                } 
            }
            emptyMatrix();
        }
    }
    

    public boolean wygrana(int n){
        for (int i = 0; i < 3; i++) {
            boolean wygrana = true;
            for (int j = 0; j < 3; j++) {
                 wygrana = wygrana && (G[i][j] == n); 
            }
            if(wygrana){
                return true;
            }
        }
        
        for (int i = 0; i < 3; i++) {
            boolean wygrana = true;
            for (int j = 0; j < 3; j++) {
                 wygrana = wygrana && (G[j][i] == n); 
            }
            if(wygrana){
                return true;
            }
        }
        
        if(G[0][0] == n && G[1][1] == n && G[2][2] == n)return true;
        
        return false;
    }
    

    public boolean full(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(G[i][j] == -1)return false;
            }
        }
        
        emptyMatrix();
        return true;
    }
    
    public void emptyMatrix(){
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    G[i][j] = -1;
                }
        }
    }
}
