package distribuidos;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.xalan.internal.xsltc.dom.AbsoluteIterator;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class Client extends JFrame implements Runnable{
	static int num_dados=5;
	int dados_actuales=5;
	private JPanel contentPane;
	private int dados[]=new int [num_dados];
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */int registro = 0;
	private JTextField dadoTxtField;
	private JTextField numeroDadosTxtField;
	private JButton buttonConnect;
	private JTextArea areaDeMensajes ;
	private JLabel lablDado ;
	private JLabel lblNumero ;
	private JButton btnSupos;
	
	
	
	
	
	public Client() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		getContentPane().setLayout(null);
		contentPane.setLayout(null);
		
		buttonConnect = new JButton("CONECTARSE");
		areaDeMensajes = new JTextArea();
		lablDado = new JLabel("Dado");
		dadoTxtField = new JTextField();
		lblNumero = new JLabel("Numero");
		numeroDadosTxtField = new JTextField();
		btnSupos = new JButton("Suponer");
		
		
		buttonConnect.setBounds(22, 11, 143, 31);
		contentPane.add(buttonConnect);
		
		areaDeMensajes.setBounds(225, 14, 149, 309);
		contentPane.add(areaDeMensajes);
		
		lablDado.setBounds(22, 76, 85, 14);
		contentPane.add(lablDado);
		
		
		dadoTxtField.setBounds(21, 96, 144, 20);
		contentPane.add(dadoTxtField);
		dadoTxtField.setColumns(10);
		
		
		lblNumero.setBounds(22, 127, 85, 25);
		contentPane.add(lblNumero);
		
		
		numeroDadosTxtField.setBounds(22, 152, 143, 20);
		contentPane.add(numeroDadosTxtField);
		numeroDadosTxtField.setColumns(10);
		
		
		btnSupos.setBounds(22, 197, 143, 23);
		contentPane.add(btnSupos);
		setSize(400, 400);
		setVisible(true);
		
		buttonConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					
					if(registro!=1){
					
					Socket cli=new Socket("127.0.0.1", 9091 ); //ip y puerto que se utiliza
					
					DataOutputStream flujoSalida = new DataOutputStream(cli.getOutputStream()); //salida del socket cliente
					flujoSalida.writeUTF("Conectar"); // envio el string 
					flujoSalida.close(); //cierro flujo salida
					cli.close(); //cierro socket salida
					
						//aca creamos un socket como si fuera servidor 
						//para escuchar lo que me diga el server
						ServerSocket serv= new ServerSocket(9092);
						Socket cliRecibo;
						cliRecibo=serv.accept();
						DataInputStream flujoEntrada = new DataInputStream(cliRecibo.getInputStream());
						//leo lo que me diga el server y dependiendo de eso me conecto o no 
						String mens=flujoEntrada.readUTF().toString();
						flujoEntrada.close();
						if(mens.equals("CONECTESE")) {
							System.out.println("me conecte");
							registro = 1;
							//mensajes de juego 
							//utlizo cli para enviar mensajes al server 
							//y cli1 o server para recibir mensajes
							for(int i=0;i<dados_actuales;i++) {
								int randomNum = ThreadLocalRandom.current().nextInt(1, num_dados + 1);
								dados[i]=randomNum;
								System.out.println("dado en "+i+" es  :"+ dados[i]); //arreglo de dados  con el numero random
							
							}
							
							while(true) {
								cliRecibo=serv.accept();
								System.out.println("Esperando Mensaje");
								//me llega un mensaje y evaluo que es
								DataInputStream flujoEntrada2 = new DataInputStream(cliRecibo.getInputStream());
								String msg = flujoEntrada2.readUTF();
								flujoEntrada2.close(); //cierro flujo entrada 2
								cliRecibo.close(); //cierro socket entrada 
								//si el mensaje dice que es mi turno ... 
								if(msg.equals("tu turno")) { //haga una suposicion pues es el primero
									//habilito los textfields para que pueda escribir el dado y el numero de dados
									JOptionPane.showMessageDialog(null, "Su turno", "TURNO", JOptionPane.INFORMATION_MESSAGE);
									
									
									btnSupos.setEnabled(true);
									dadoTxtField.setEnabled(true);
									numeroDadosTxtField.setEnabled(true);
									
									btnSupos.addMouseListener(new MouseAdapter() {
										@Override
										public void mouseClicked(MouseEvent e) {
											//verifico que lo que me ingrese no 
											if(dadoTxtField.getText().equals("") && numeroDadosTxtField.getText().equals("")) {
												JOptionPane.showMessageDialog(null, "Ingrese el dado y el numero de dados en los campos ", "TURNO", JOptionPane.INFORMATION_MESSAGE);
												
											}else {
												String dadoEnviar=dadoTxtField.getText();
												String numDadEnviar= numeroDadosTxtField.getText();
												String enviar=dadoEnviar+","+numDadEnviar;
												try {
													DataOutputStream flujoSalida2;
													Socket cli=new Socket("127.0.0.1", 9091 );
													flujoSalida2 = new DataOutputStream(cli.getOutputStream());
													System.out.println("Enviare el mensaje : "+enviar);
													flujoSalida2.writeUTF(enviar); // envio el string 
													flujoSalida2.close(); //cerre el flujo de salida
													System.out.println("supuse");
													cli.close(); //cerre socket de salida
												} catch (IOException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												} //salida del socket cliente
											}
											
											
										}
									});

								}else if(msg.matches("^[1-6]?+([,][1-9]?)?$")) { // hicieron una suposicion
									areaDeMensajes.append(msg);
									System.out.println("es una jugada ");
									
									
									
								}else if(msg.equals("Destaparse")){ //mostrar los dados 
									
								}
		
							}
							
						}else if(mens.equals("NOCONECTESE")){
							System.out.println("No me conecte");
							serv.close();

						}else{
							System.out.println("No ES EL MENSAJE ");
							serv.close();
						}
						System.out.println("Press Any Key To Continue...");
				          new java.util.Scanner(System.in).nextLine();
					} else{
						JOptionPane.showMessageDialog(null, "Ya se habia registrado al servidor", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		
		
	
		
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}

	public void run() { //esto se ejecuta en un segundo plano
		// TODO Auto-generated method stub
		
	}
}