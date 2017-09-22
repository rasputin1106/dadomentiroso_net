package distribuidos;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.sun.javafx.webkit.ThemeClientImpl;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class frmservidor extends JFrame implements Runnable{
	
	
	
	
	JTextArea txtmensajes;
	Queue Jugadores = new LinkedList<>();
	int cont = 0;
	private JTextField txtJugadoresOnline;
	
	
	public frmservidor() {
		getContentPane().setLayout(null);
		txtmensajes = new JTextArea();
		txtmensajes.setBounds(10, 47, 464, 403);
		getContentPane().add(txtmensajes);
		
		JLabel lblJugadoresRegistrados = new JLabel("Jugadores Registrados");
		lblJugadoresRegistrados.setBounds(231, 11, 146, 14);
		getContentPane().add(lblJugadoresRegistrados);
		
		txtJugadoresOnline = new JTextField();
		txtJugadoresOnline.setEnabled(false);
		txtJugadoresOnline.setBounds(377, 8, 63, 20);
		getContentPane().add(txtJugadoresOnline);
		txtJugadoresOnline.setColumns(10);
		setSize(500,500);
		setVisible(true);
		
		Thread hilo = new Thread(this);
		hilo.start(); // Llama a Run.
		 
	}

	public static void main(String[] args) {
		new frmservidor();

	}

	@Override
	public void run() {
		try {
			System.out.println("Let's Go");
			txtJugadoresOnline.setText(Integer.toString(cont));
			
			ServerSocket servidor = new ServerSocket(9091);
				
			while(true){
				System.out.println("Esperando Mensaje");
				
				if(cont>0){
					String head = null;
					head = (String) Jugadores.element();
					 Socket cli2=new Socket(head, 9092 ); //ip y puerto que se utiliza
					 System.out.println("Turno Jugador");
					 DataOutputStream flujo2 = new DataOutputStream(cli2.getOutputStream()); //salida del socket cliente
					 flujo2.writeUTF("tu turno"); // envio el string 
					 //si quiero enviar mas cosas, write UTF, si quiero enviar objetco es write Object
					 flujo2.close();
					 cli2.close();
					System.out.println("Cpillin");
					while(true){
						System.out.println("Esperando MensajeX2");
						Socket cliEntrada;
						cliEntrada = servidor.accept();
						
						
						DataInputStream flujoMe = new DataInputStream(cliEntrada.getInputStream());
						String msg = flujoMe.readUTF(); //cierro flujo entrada
						cliEntrada.close(); //cierro el socket de entrada
						flujoMe.close();
						if(msg.isEmpty()) {
							System.out.println("mensaje no tiene nada");
						}else {
							System.out.println("MENSAJE QUE ME LLEGO = "+msg);
						}
					
						
						Pattern pat = Pattern.compile("^[1-6]?+([,][1-9]?)?$");
				         Matcher mat = pat.matcher(msg);
					     if (mat.matches()) {
					         System.out.println("SI");
					     } else { //Entra a desconfiar.
					         System.out.println("NO");
					     }
					     
					      
					     
					}
				}else {
					Socket cli;
					cli = servidor.accept();
					
					
					DataInputStream flujo = new DataInputStream(cli.getInputStream());
					String msg = flujo.readUTF();
					flujo.close();//cierro flujo entrada
					
					
					if(msg.equals("Conectar")){
						InetAddress address = cli.getInetAddress(); 
					    String hostIP = address.getHostAddress() ;
					    System.out.println( "IP: " + hostIP);		
					    if(cont <=6){
					    	System.out.println("Se registra");
					    	Jugadores.offer(hostIP);
					    	cont++;
						    txtJugadoresOnline.setText(Integer.toString(cont));
						    
						    Socket cli2=new Socket(hostIP, 9092 ); //ip y puerto que se utiliza
						    
						    System.out.println("GGGGGGG");
							DataOutputStream flujo2 = new DataOutputStream(cli2.getOutputStream()); //salida del socket cliente
							flujo2.writeUTF("CONECTESE"); // envio el string 
							//si quiero enviar mas cosas, write UTF, si quiero enviar objetco es write Object
							flujo2.close();//cierro flujo 2 de escritura
							cli2.close(); //cierro socket de escritura
						    
					    } else {
					    		System.out.println("No Se registra");
					    		Socket cli2=new Socket(hostIP, 9092 ); //ip y puerto que se utiliza
								DataOutputStream flujo2 = new DataOutputStream(cli2.getOutputStream()); //salida del socket cliente
								flujo2.writeUTF("NOCONECTESE"); // envio el string 
								//si quiero enviar mas cosas, write UTF, si quiero enviar objetco es write Object
								flujo2.close();//cierro flujo escritura
								cli2.close();//cierro socket escritura
						    
						    
					    }			    	
					}	
						
						System.out.println("BaiMen");
						txtmensajes.append("\n"+cli.getInetAddress()+": "+msg);
						cli.close();
					}
					
				}
				
				
				

			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}