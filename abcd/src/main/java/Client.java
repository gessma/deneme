import javax.swing.*;
import javax.websocket.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
//import org.glassfish.tyrus.client.ClientManager; glassfis yuk


@ClientEndpoint
public class Client extends JFrame {
    private JButton clientButon;
    private JTextArea clientTextArea;
    private JTextField clientTextField;
    private JPanel clientPanel;
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        System.out.println ("--- Connected " + session.getId());
        this.session=session;
    }
    @OnClose
    public void onClose(Session session)
    {
        System.out.println("Session " + session.getId()   );
        CountDownLatch latch = null; //şu iki satırı kaldırabiliriz bi daha bak
        latch.countDown();

    }
    @OnError
    public void onError()
    {

    }
    @OnMessage
    public void onMessage(String message, Session session){
      /*  BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println ("--- Received " + message);
            String userInput = bufferRead.readLine();
            return userInput; //void de yapabilirsin bi daha bak
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        System.out.println ("--- Mesajjj " + message);
        clientTextArea.append(message + "\n");


    }

    public void sendMessage(String message) {
        // mesaj göndermek için kullanılacak metod
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Client(){

        add(clientPanel);
        clientPanel.setSize(500,500);
        setVisible(true);
        clientButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mesaj1=clientTextField.getText();
                sendMessage(mesaj1);
            }
        });
    }

    public static void main(String[] args) throws URISyntaxException {
        URI u= new URI("ws://localhost:8080/chat");
        Client client = new Client(); //uu
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setSize(500, 500);
        client.setVisible(true);


        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(client, new URI("ws://localhost:8080/chat"));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }


    }



}
