import javax.swing.*;
import javax.websocket.*;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.net.URI;

@ServerEndpoint("/chat")
public class Server extends JFrame {
    Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private JButton serverButon;
    private JTextField serverTextField;
    private JTextArea serverTextArea;
    private JPanel serverPanel;
    private Session session;

    public Server() {

        add(serverPanel);
        serverPanel.setSize(500,500);
        setVisible(true);

        serverButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = serverTextField.getText(); // serverTextField is the text field where you type the message
                sendMessage(message);
            }
        });
    }

    @OnOpen
    public void onOpen(Session session){
        System.out.println ("Connected, sessionID = " + session.getId());
        sessions.add(session);
    }
    @OnMessage
    public  void onMessage(String message, Session session) {

        System.out.println("Gelen mesaj: " + message);
        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() );
        sessions.remove(session);
    }
    /*public static void main(String[] args) throws URISyntaxException {
        URI serverURI = new URI("ws://localhost:8080/chat");
        Client clientEndpoint = new Client(serverURI);

    }*/
    public static void main(String[] args) throws URISyntaxException {
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(Server.class, "/chat").build();
        ServerContainer container = (ServerContainer) ContainerProvider.getWebSocketContainer();
        try {
            container.addEndpoint(config);
          //  container.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        // mesaj göndermek için kullanılacak metod
        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    }
