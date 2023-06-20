package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;

public class ActiviMQ_B implements ActionListener {

	private JFrame frame;
	private JTextField txt_Send;
	private JButton btn_Send;
	private JTextArea txtarea_msg;
	private Session session;
	private Destination destinationA;
	private Destination destinationB;
	private Connection con;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ActiviMQ_B window = new ActiviMQ_B();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ActiviMQ_B() throws Exception {
		initialize();
//		cấu hình môi trường cho JMS và JNDI -> tạo 1 kết nối đến ActiviMQ
		// config environment for JMS
		BasicConfigurator.configure();
		// config environment for JNDI
		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		// create context
		Context ctx = new InitialContext(settings);
		// lookup JMS connection factory
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		// lookup destination. (If not exist-->ActiveMQ create once)
		destinationA = (Destination) ctx.lookup("dynamicQueues/TranTruongThien");
		destinationB = (Destination) ctx.lookup("dynamicQueues/NguyenXuanAn");
		// get connection using credential
		con = factory.createConnection("admin", "admin");
		// connect to MOM
		con.start();
		// create session
		session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);

		// tạo consumer nhận tin nhắn
		MessageConsumer receiverB = session.createConsumer(destinationB);
		// blocked-method for receiving message - sync
		// receiver.receive();
		// Cho receiver lắng nghe trên queue, chừng có message thì notify - async
		System.out.println("Tý was listened on queue...");
		receiverB.setMessageListener(new MessageListener() {
//									@Override
			// có message đến queue, phương thức này được thực thi
			public void onMessage(Message msg) {// msg là message nhận được
				try {
					if (msg instanceof TextMessage) {
						TextMessage tm = (TextMessage) msg;
						String txt = tm.getText();
						txtarea_msg.append("Them: " + txt + "\n");
						System.out.println("Nhận được " + txt);
						msg.acknowledge();// gửi tín hiệu ack
					} /*
						 * else if (msg instanceof ObjectMessage) { ObjectMessage om = (ObjectMessage)
						 * msg; System.out.println(om); }
						 */
					// others message type....
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

//		// tạo consumer gửi tin nhắn
//		MessageConsumer receiver = session.createConsumer(destinationA);
//		// blocked-method for receiving message - sync
//		// receiver.receive();
//		// Cho receiver lắng nghe trên queue, chừng có message thì notify - async
//		System.out.println("Tý was listened on queue...");
//		receiver.setMessageListener(new MessageListener() {
////			@Override
//			// có message đến queue, phương thức này được thực thi
//			public void onMessage(Message msg) {// msg là message nhận được
//				try {
//					if (msg instanceof TextMessage) {
//						TextMessage tm = (TextMessage) msg;
//						String txt = tm.getText();
//						txtarea_msg.append("You: " + txt + "\n");
//						System.out.println("Nhận được " + txt);
//						msg.acknowledge();// gửi tín hiệu ack
//					} /*
//						 * else if (msg instanceof ObjectMessage) { ObjectMessage om = (ObjectMessage)
//						 * msg; System.out.println(om); }
//						 */
//					// others message type....
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 575, 443);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel p_North = new JPanel();
		frame.getContentPane().add(p_North, BorderLayout.NORTH);

		JLabel lbl_title = new JLabel("ActiviMQ B");
		lbl_title.setFont(new Font("Tahoma", Font.BOLD, 26));
		p_North.add(lbl_title);

		JPanel p_Center = new JPanel();
		frame.getContentPane().add(p_Center);
		p_Center.setLayout(null);

		txtarea_msg = new JTextArea();
		txtarea_msg.setEnabled(false);
		txtarea_msg.setEditable(false);
		txtarea_msg.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtarea_msg.setLineWrap(true);
		txtarea_msg.setBounds(26, 23, 504, 261);
		p_Center.add(txtarea_msg);

		JPanel p_South = new JPanel();
		p_South.setPreferredSize(new Dimension(10, 50));
		frame.getContentPane().add(p_South, BorderLayout.SOUTH);
		p_South.setLayout(null);

		btn_Send = new JButton("Send");
		btn_Send.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btn_Send.setBounds(455, 10, 85, 30);
		p_South.add(btn_Send);

		txt_Send = new JTextField();
		txt_Send.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txt_Send.setBounds(26, 10, 419, 30);
		p_South.add(txt_Send);
		txt_Send.setColumns(10);
		txt_Send.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {
				String text = txt_Send.getText().trim();
				btn_Send.setEnabled(!text.isEmpty());
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						try {
							// create producer
							MessageProducer producer = session.createProducer(destinationA);
							// create text message
							Message msg = session.createTextMessage(txt_Send.getText());
							producer.send(msg);
							txt_Send.setText("");
							btn_Send.setEnabled(false);
//							Person p = new Person(1001, " Nguyễn Xuân An", new Date());
//							String xml = new XMLConvert<Person>(p).object2XML(p);
//							msg = session.createTextMessage(xml);
//							producer.send(msg);
						} /*
							 * catch (NamingException e1) { e1.printStackTrace(); }
							 */ catch (JMSException e2) {
							e2.printStackTrace();
						} catch (Exception e3) {
							e3.printStackTrace();
						}
					}
				}
			}

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		txt_Send.addActionListener(this);
		btn_Send.addActionListener(this);
		btn_Send.setEnabled(false);
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_Send) {

			try {
				// create producer
				MessageProducer producer = session.createProducer(destinationA);
				// create text message
				Message msg = session.createTextMessage(txt_Send.getText());
				producer.send(msg);
				txt_Send.setText("");
				btn_Send.setEnabled(false);
//				Person p = new Person(1001, " Nguyễn Xuân An", new Date());
//				String xml = new XMLConvert<Person>(p).object2XML(p);
//				msg = session.createTextMessage(xml);
//				producer.send(msg);
			} /*
				 * catch (NamingException e1) { e1.printStackTrace(); }
				 */ catch (JMSException e2) {
				e2.printStackTrace();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}

	}
}
