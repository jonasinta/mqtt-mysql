package org.eclipse.pahodemoAsync;

import java.util.Date;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PahoListenPostSynchronus  implements MqttCallback{

  MqttClient client;
  
  
  public PahoListenPostSynchronus() {}

  public static void main(String[] args) {
	  PahoListenPostSynchronus boing = new PahoListenPostSynchronus();
    boing.doDemo();
    
	
  }

  public void doDemo() {
    try {
    	MqttConnectOptions options;
    	
      client = new MqttClient("tcp://192.168.1.71:10002", "JavaAppMonitor");
    	//client = new MqttClient("tcp://jonas-home.duckdns.org:10002", "JavaAppMonitor3");
      client.setCallback(this);
      client.setTimeToWait(10000L);
      options = new MqttConnectOptions();
      options.setWill("pahodemo/clienterrors", "crased".getBytes(),2,true);
      client.connect(options);
      System.out.println("just tried to connect and subscribed \"/mcu/+/heap,volts,stamp/");
      
      MqttMessage message = new MqttMessage();
      message.setPayload("A single message".getBytes());
      //client.publish("pahodemo/test", message);
      //String subscription = new String("pahodemo/test");

      //client.subscribe("/mcu/14056893/heap,volts,stamp/");
      //client.subscribe("/mcu/408776/heap,volts,stamp/");
      client.subscribe("/mcu/+/heap,volts,stamp/");//chip id part is the "+" bit
     //client.disconnect();
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

public void connectionLost(Throwable cause) {
	// TODO Auto-generated method stub
	
}

public void messageArrived(String topic, MqttMessage message) throws Exception {
	String stringChipID;
	String[] chipID = topic.split("/");
	stringChipID = chipID[2];
	tmMysql_obj toMysqlInstance1 = new tmMysql_obj();
	//paho_gui.setTextField_sentValueText(message.toString());
	String Str = new String(message.toString());
	System.out.println("recieved message; "+message+"\n");
	System.out.println("recieved topic; "+topic+"\n");
	System.out.println("=================================================================\n\n");
	
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
		Number voltage = (Number) jsonObject.get("Volts");
		Number heap = (Number) jsonObject.get("HEAP");
		Float v1 = voltage.floatValue();
		Integer v2 = heap.intValue();
		
	toMysqlInstance1.get2Database(stringChipID, 0,0,v2,v1);
		//System.out.println("Must have sent to database -------------------------------------");
	} catch (ParseException e) {
		e.printStackTrace();
	}
    
    
}

public void deliveryComplete(IMqttDeliveryToken token) {
	// TODO Auto-generated method stub
	System.out.println("delivered");
	
}


}
