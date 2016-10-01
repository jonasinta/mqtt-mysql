package org.eclipse.pahodemoAsync;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Paho_guiAsync extends JFrame implements IMqttAsyncClient,
		MqttCallback {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					Paho_guiAsync frame = new Paho_guiAsync();
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel contentPane;
	private JTextField textField_subscribedTo;
	private JTextField textField_messageTime;
	private JLabel lbl_value;
	private static JTextField textField_sentValue;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private PahoDemoAsync pahoObject;
	private JLabel lblConnecting;
	private MqttAsyncClient client;
	private JComboBox<String> comboBox;
	private JLabel lblPlaceholder;
	private JLabel lblAddressCombo;
	private List<String> mqtt_uri;

	private String willUseUri;

	/**
	 * Create the frame.
	 */
	public Paho_guiAsync() {

		mqtt_uri = new ArrayList<String>(); // array to fit url for mqtt to
											// connect to
		mqtt_uri.add("tcp://192.168.1.71:10002"); // 1 of 2 addresses
		mqtt_uri.add("tcp://jonas-home.duckdns.org:10002"); // 20f 2 addresses
															// to connect
		willUseUri = mqtt_uri.get(0); // initialise with the first element of
										// url array to connect
		setTitle("Mqtt Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 450, 300);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(6, 2, 25, 25));

		JLabel lbl_subscribed = new JLabel("Subscribed to;");
		contentPane.add(lbl_subscribed);

		textField_subscribedTo = new JTextField();
		textField_subscribedTo.setEditable(false);
		contentPane.add(textField_subscribedTo);

		JLabel lbl_messageTime = new JLabel("Time of Message");
		contentPane.add(lbl_messageTime);

		textField_messageTime = new JTextField();
		textField_messageTime.setEditable(false);
		contentPane.add(textField_messageTime);
		textField_messageTime.setColumns(10);

		lbl_value = new JLabel("Value Sent");
		contentPane.add(lbl_value);

		textField_sentValue = new JTextField();
		textField_sentValue.setEditable(false);
		contentPane.add(textField_sentValue);
		textField_sentValue.setColumns(10);

		lblAddressCombo = new JLabel("Select Connect Address");
		contentPane.add(lblAddressCombo);

		comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				willUseUri = comboBox.getSelectedItem().toString(); // get the
																	// selected
																	// address/port
																	// from
																	// combo
																	// select
																	// @param
																	// wilUseUri
																	// will be
																	// for mQTT
																	// connect
				System.out.println(willUseUri);
			}
		});
		// comboBox.setModel(new DefaultComboBoxModel(new String[]
		// {"\"tcp://192.168.1.71:10002\"",
		// "\"tcp://jonas-home.duckdns.org:10002\""}));
		comboBox.setModel(new DefaultComboBoxModel(mqtt_uri.toArray()));
		contentPane.add(comboBox);

		lblConnecting = new JLabel("");
		lblConnecting.setHorizontalAlignment(SwingConstants.CENTER);
		lblConnecting.setForeground(Color.RED);
		contentPane.add(lblConnecting);

		lblPlaceholder = new JLabel("");
		contentPane.add(lblPlaceholder);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					client.disconnect();
				} catch (MqttException e1) {
					System.out
							.println("close connection to server error Ln155 -client.disconnect");
					e1.printStackTrace();

				}
				System.out.println("close connection to server now");

				System.out.println((client.isConnected()) ? "isConnected"
						: "isNot Connected");
				lblConnecting.setForeground(Color.RED);
				lblConnecting.setText("Dis-Connected");
				System.out.println("Dis-Connected");
			}
		});

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("connect clicked");

				try {
					MqttConnectOptions options;
					IMqttToken conToken;
					// client = new MqttAsyncClient("tcp://192.168.1.71:10002",
					// "JavaAppMonitor");
					client = new MqttAsyncClient(willUseUri, "JavaAppMonitor3");

					client.setCallback(Paho_guiAsync.this);

					options = new MqttConnectOptions();
					options.setWill("pahodemo/clienterrors",
							"crased".getBytes(), 2, true);
					conToken = client.connect(options, "userContext",
							new IMqttActionListener() {

								public void onFailure(
										IMqttToken asyncActionToken,
										Throwable exception) {
									lblConnecting.setForeground(Color.YELLOW);
									lblConnecting.setText("Check address etc; "
											+ client.getServerURI());
									System.out
											.println("Connection not happenning");

								}

								public void onSuccess(
										IMqttToken asyncActionToken) {
									// TODO Auto-generated method stub
									lblConnecting.setForeground(Color.GREEN);
									lblConnecting.setText("Connected");
									System.out.println("Connected");
									MqttMessage message = new MqttMessage();
									message.setPayload("A single message"
											.getBytes());
									// client.publish("pahodemo/test", message);
									// String subscription = new
									// String("pahodemo/test");
									try {
										client.subscribe(
												"/mcu/14056893/heap,volts,stamp/",
												0);
									} catch (MqttException e) {
										System.out
												.println("error from ln206 client.subscribe(\"/mcu/14056893/heap,volts,stamp/\", 0);");
										e.printStackTrace();
									}
									textField_subscribedTo
											.setText("/mcu/14056893/heap,volts,stamp/");
								}
							});
					lblConnecting.setForeground(Color.RED);
					lblConnecting.setText("Connecting");
					// conToken.waitForCompletion(10000L);

					// client.disconnect();
				} catch (MqttException ex) {
					ex.printStackTrace();
				} catch (IllegalArgumentException ex2) {
					ex2.printStackTrace();
				}

			}
		});
		contentPane.add(btnConnect);

		contentPane.add(btnDisconnect);
	}

	public void close() throws MqttException {
		// TODO Auto-generated method stub

	}

	public IMqttToken connect() throws MqttException, MqttSecurityException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken connect(MqttConnectOptions options) throws MqttException,
			MqttSecurityException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken connect(MqttConnectOptions options, Object userContext,
			IMqttActionListener callback) throws MqttException,
			MqttSecurityException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken connect(Object userContext, IMqttActionListener callback)
			throws MqttException, MqttSecurityException {
		// TODO Auto-generated method stub
		return null;
	}

	public void connectionLost(Throwable cause) {
		lblConnecting.setForeground(Color.RED);
		lblConnecting.setText("Connection LOST");
		System.out.println("Connection LOST");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	public IMqttToken disconnect() throws MqttException {

		return null;
	}

	public IMqttToken disconnect(long quiesceTimeout) throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken disconnect(long quiesceTimeout, Object userContext,
			IMqttActionListener callback) throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken disconnect(Object userContext,
			IMqttActionListener callback) throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientId() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttDeliveryToken[] getPendingDeliveryTokens() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServerURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		// paho_gui.setTextField_sentValueText(message.toString());
		String Str = new String(message.toString());
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(Str);
			JSONObject jsonObject = (JSONObject) obj;
			Str = jsonObject.get("timestamp").toString();
			Date date = new Date((Long) jsonObject.get("timestamp") * 1000);
			System.out.println("Volts; " + jsonObject.get("Volts"));
			System.out.println("Heap; " + jsonObject.get("HEAP"));
			System.out.println("Date; " + date.toString());
			textField_messageTime.setText(date.toString());
			textField_sentValue.setText(jsonObject.get("Volts").toString());
			System.out.println("------------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public IMqttDeliveryToken publish(String topic, byte[] payload, int qos,
			boolean retained) throws MqttException, MqttPersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttDeliveryToken publish(String topic, byte[] payload, int qos,
			boolean retained, Object userContext, IMqttActionListener callback)
			throws MqttException, MqttPersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttDeliveryToken publish(String topic, MqttMessage message)
			throws MqttException, MqttPersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttDeliveryToken publish(String topic, MqttMessage message,
			Object userContext, IMqttActionListener callback)
			throws MqttException, MqttPersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCallback(MqttCallback callback) {
		// TODO Auto-generated method stub

	}

	public IMqttToken subscribe(String topicFilter, int qos)
			throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken subscribe(String topicFilter, int qos,
			Object userContext, IMqttActionListener callback)
			throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken subscribe(String[] topicFilters, int[] qos)
			throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken subscribe(String[] topicFilters, int[] qos,
			Object userContext, IMqttActionListener callback)
			throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken unsubscribe(String topicFilter) throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken unsubscribe(String topicFilter, Object userContext,
			IMqttActionListener callback) throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken unsubscribe(String[] topicFilters) throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMqttToken unsubscribe(String[] topicFilters, Object userContext,
			IMqttActionListener callback) throws MqttException {
		// TODO Auto-generated method stub
		return null;
	}

	public void disconnectForcibly() throws MqttException {
		// TODO Auto-generated method stub

	}

	public void disconnectForcibly(long arg0) throws MqttException {
		// TODO Auto-generated method stub

	}

	public void disconnectForcibly(long arg0, long arg1) throws MqttException {
		// TODO Auto-generated method stub

	}

}
