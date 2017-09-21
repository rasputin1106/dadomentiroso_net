package distribuidos;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.net.Socket;;

public class Client extends JFrame {

	private JPanel contentPane;
	JTextField txtmensaje;
	JButton btnenviar;
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
	 */
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		txtmensaje= new JTextField();
		txtmensaje.setBounds(10, 10, 200, 20);
		getContentPane().add(txtmensaje);
		
		btnenviar= new JButton();
		btnenviar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Socket cli=new Socket("192.168.0.4", 9090 ); //ip y puerto que se utiliza
					DataOutputStream flujo = new DataOutputStream(cli.getOutputStream()); //salida del socket cliente
					flujo.writeUTF(txtmensaje.getText()); // envio el string 
					//si quiero enviar mas cosas, write UTF, si quiero enviar objetco es write Object
					
					cli.close();
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Error en Cliente "+ e);
					e.getMessage();
				}
			}
		});
		btnenviar.setText("ENVIAR");
		btnenviar.setBounds(10, 40,150,20);
		getContentPane().add(btnenviar);
		
		getContentPane().setLayout(null);
		setSize(400, 400);
		setVisible(true);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
