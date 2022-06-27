package DronazonPkg;

import org.eclipse.paho.client.mqttv3.*;
import com.google.gson.Gson;

public class Dronazon {
    public static void main(String[] args) {
        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "dronazon/smartcity/orders/.";
        int qos = 2;

        Gson gson = new Gson();

        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            client.connect(connOpts);
            System.out.println(clientId + " connected to broker " + broker);

            Thread endingTh = new Thread(new EndingThread());
            endingTh.start();

            while(true) {
                try { Thread.sleep(5000); } catch (Exception e) { }

                Order order = new Order();
                String orderJsonString = gson.toJson(order);
                MqttMessage message = new MqttMessage(orderJsonString.getBytes());

                message.setQos(qos);
                client.publish(topic, message);

                System.out.println("Published order " + orderJsonString);
            }

        } catch (MqttException me ) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
