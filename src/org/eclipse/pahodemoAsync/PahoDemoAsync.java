package org.eclipse.pahodemoAsync;

import java.util.Date;

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

public class PahoDemoAsync  implements IMqttAsyncClient, MqttCallback{

  MqttAsyncClient client;
  
  public PahoDemoAsync() {}

  

  public void doDemo() {
    try {
    	MqttConnectOptions options;
    	IMqttToken conToken;
      client = new MqttAsyncClient("tcp://192.168.1.71:10002", "JavaAppMonitor");
    	//client = new MqttAsyncClient("tcp://jonas-home.duckdns.org:10002", "JavaAppMonitor3");
      client.setCallback(this);
      
      options = new MqttConnectOptions();
      options.setWill("pahodemo/clienterrors", "crased".getBytes(),2,true);
     conToken = client.connect(options, "userContext",new IMqttActionListener() {
		
		public void onSuccess(IMqttToken asyncActionToken) {
			// TODO Auto-generated method stub
			System.out.println("Connected");
			
		}
		
		public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
			System.out.println("Connection not happenning");
			
		}
	});
	
     conToken.waitForCompletion(10000L);
      MqttMessage message = new MqttMessage();
      message.setPayload("A single message".getBytes());
      //client.publish("pahodemo/test", message);
      //String subscription = new String("pahodemo/test");
      client.subscribe("/mcu/14056893/heap,volts,stamp/",0);
     //client.disconnect();
    } catch (MqttException e) {
      e.printStackTrace();
    }
    catch (IllegalArgumentException e) {
        e.printStackTrace();
    }
  }

public void connectionLost(Throwable cause) {
	// TODO Auto-generated method stub
	
}

public void messageArrived(String topic, MqttMessage message) throws Exception {
	// TODO Auto-generated method stub
	//paho_gui.setTextField_sentValueText(message.toString());
	String Str = new String(message.toString());
	JSONParser parser = new JSONParser();
	try {

		Object obj = parser.parse( Str);
		JSONObject jsonObject = (JSONObject) obj;
		Str=jsonObject.get("timestamp").toString();
		Date date = new Date((Long) jsonObject.get("timestamp")*1000);
		System.out.println("Volts; "+ jsonObject.get("Volts"));
		System.out.println("Heap; "+ jsonObject.get("HEAP"));
		System.out.println("Date; "+ date.toString());
		
		System.out.println("------------------------------------------");
	} catch (ParseException e) {
		e.printStackTrace();
	}
    
    
}

public void deliveryComplete(IMqttDeliveryToken token) {
	// TODO Auto-generated method stub
	System.out.println("delivered");
	
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

public IMqttToken connect(Object userContext, IMqttActionListener callback)
		throws MqttException, MqttSecurityException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken connect(MqttConnectOptions options, Object userContext,
		IMqttActionListener callback) throws MqttException,
		MqttSecurityException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken disconnect() throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken disconnect(long quiesceTimeout) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken disconnect(Object userContext, IMqttActionListener callback)
		throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken disconnect(long quiesceTimeout, Object userContext,
		IMqttActionListener callback) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public boolean isConnected() {
	// TODO Auto-generated method stub
	
	return false;
}

public String getClientId() {
	// TODO Auto-generated method stub
	return null;
}

public String getServerURI() {
	// TODO Auto-generated method stub
	return null;
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
		Object userContext, IMqttActionListener callback) throws MqttException,
		MqttPersistenceException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken subscribe(String topicFilter, int qos) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken subscribe(String topicFilter, int qos, Object userContext,
		IMqttActionListener callback) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken subscribe(String[] topicFilters, int[] qos)
		throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken subscribe(String[] topicFilters, int[] qos,
		Object userContext, IMqttActionListener callback) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken unsubscribe(String topicFilter) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken unsubscribe(String[] topicFilters) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken unsubscribe(String topicFilter, Object userContext,
		IMqttActionListener callback) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public IMqttToken unsubscribe(String[] topicFilters, Object userContext,
		IMqttActionListener callback) throws MqttException {
	// TODO Auto-generated method stub
	return null;
}

public void setCallback(MqttCallback callback) {
	// TODO Auto-generated method stub
	
}

public IMqttDeliveryToken[] getPendingDeliveryTokens() {
	// TODO Auto-generated method stub
	return null;
}

public void close() throws MqttException {
	// TODO Auto-generated method stub
	client.disconnect();
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
