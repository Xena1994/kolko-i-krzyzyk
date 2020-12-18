package gracz;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;


public class Klient implements Runnable{
    
    private Socket klient;
    private DataOutputStream out;
    private DataInputStream in;
    private int port = 2027;
    private String host = "localhost";
    
 
    private String message;
    private Main frame;
    private JButton[][] buttones;
    private ActionListener ac;
    
  
    private Image X;
    private Image O;
    
    private boolean runda;
    

    public Klient(Main frame){
        try {
            this.frame = frame;
            X = ImageIO.read(getClass().getResource("X.png"));
            O = ImageIO.read(getClass().getResource("O.png"));
            klient = new Socket(host,port);
            in = new DataInputStream(klient.getInputStream());
            out = new DataOutputStream(klient.getOutputStream());
            buttones = this.frame.getButtones();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            message =  in.readUTF();
            String split[] = message.split(";");
            frame.changeText(split[0]);
            String XO = split[0].split(" ")[1];
            runda = Boolean.valueOf(split[1]);
           
            while(true){
                message = in.readUTF();
                
                String[] messages = message.split(";");
                int xo = Integer.parseInt(messages[0]);
                int f = Integer.parseInt(messages[1]);
                int c = Integer.parseInt(messages[2]);
                if(xo == 1)
                    buttones[f][c].setIcon(new ImageIcon(X));
                else
                    buttones[f][c].setIcon(new ImageIcon(O));
                buttones[f][c].removeActionListener(buttones[f][c].getActionListeners()[0]);
                runda = !runda;
                if(XO.equals(messages[3])){
                    JOptionPane.showMessageDialog(frame, "WYGRALES :)!");
                    new Main().setVisible(true);
                    frame.dispose();
                }else  if("REMIS".equals(messages[3])){
                    JOptionPane.showMessageDialog(frame, "REMIS!");
                    new Main().setVisible(true);
                    frame.dispose();
                }
                else  if(!"NIKT".equals(messages[3]) && !messages[3].equals(messages[0])){
                    JOptionPane.showMessageDialog(frame, "PRZEGRALES :(!");
                    new Main().setVisible(true);
                    frame.dispose();
                }
                
                
              
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void enviarTurno(int f,int c){

        try {
            if(runda){
                String  data = "";
                data += f + ";";
                data += c + ";";
                out.writeUTF(data);
            }
            else{
                JOptionPane.showMessageDialog(frame, "Czekaj na swoją kolej...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}
