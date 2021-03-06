package org.eclipse.pahodemoAsync;

import java.util.Date;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PahoListenPostSynchronus  implements MqttCallback{

  MqttClient client;
  
  
  public PahoListenPostSynchronus() {}

  public static void main(String[] args) throws InterruptedException {
	  mqttClientInit();
    
	
  }

public static void mqttClientInit() throws InterruptedException {
	PahoListenPostSynchronus boing = new PahoListenPostSynchronus();
    while (boing.doDemo() ==0 ) {
		System.err.println("network problem");
		Thread.sleep(10000);
	}
}

  public int doDemo() {
    try {
    	MqttConnectOptions options;
    	
      client = new MqttClient("tcp://192.168.1.71:10002", "Mqtt-mysql-JavaMonitor");
    	//client = new MqttClient("tcp://jonas-home.duckdns.org:10002", "Mqtt-mysql-JavaMonitor");
      //blank line
      client.setCallback(this);
      client.setTimeToWait(10000L);
      options = new MqttConnectOptions();
      options.setWill("pahodemo/clienterrors", "mqtt-mysql-crashed".getBytes(),2,true);
      client.connect(options);
      System.out.println("just tried to connect and subscribed \"/mcu/+/heap,volts,stamp/");
      
    
      client.subscribe("/mcu/+/heap,volts,stamp/");//chip id part is the "+" bit
     
    } catch (MqttException e) {
    	System.err.println("inside catch MqttExeption");
      e.printStackTrace();
        System.err.println(e.getMessage());
        return 0;
    }
	return 1;
    
  }

@Override
public void connectionLost(Throwable cause) {
	System.err.println("inside connection lost callback");
	System.err.print(cause.getMessage());
	try {
		mqttClientInit();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}

@Override
public void messageArrived(String topic, MqttMessage message) throws Exception {
	String stringChipID;
	String[] chipID = topic.split("/");
	stringChipID = chipID[2];
	tmMysql_obj toMysqlInstance1 = new tmMysql_obj();
	//paho_gui.setTextField_sentValueText(message.toString());
	String Str = new String(message.toString());
	System.out.println("recieved message; "+message+"\n");
	System.out.println("recieved topic; "+topic+"\n");
	
	
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
		System.err.println("inside json parser catch- musthave been a json parse problem");
		e.printStackTrace();
	}
    
    
}

@Override
public void deliveryComplete(IMqttDeliveryToken token) {
	// TODO Auto-generated method stub
	System.out.println("delivered to mosquitto server");
	
}


}
