package chat;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Declaration of sockets, streams and implementation of methods for sending and receiving messages.
 */

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    // using buffers for efficiency
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch(IOException e) {
            System.out.println("Error creating server.");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);

        }
    }

    /**
     * Using the output stream, the message received as an argument is sent to the client.
     * @param messageToClient - the message to be sent
     */
    public void sendMessageToClient(String messageToClient) {
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to client.");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }



    }

    /**
     * While the client socket is connected, the message from client is read from the input stream and added to GUI
     * @param vbox - vertical box from which messages are extracted
     */

    public void receiveMessageFromClient(VBox vbox) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String messageFromClient = bufferedReader.readLine();
                        ServerController.addLabel(messageFromClient, vbox);
                    } catch(IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message from client.");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }


                }

            }
        }).start();

    }

    /**
     * Closes sockets and streams.
     * @param socket - socket to be closed
     * @param bufferedReader - input stream to be closed
     * @param bufferedWriter - output stream to be closed
     */

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (socket != null) {
                socket.close();
            }

            if (bufferedReader != null) {
                bufferedReader.close(); // also closing the underlying streams
            }

            if (bufferedWriter != null) {
                bufferedWriter.close(); // also closing the underlying streams
            }
        } catch(IOException e) {
            System.out.println("Error closing sockets and streams.");
            e.printStackTrace();
        }

    }
}
