package Connection;


import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by cfgs on 16/05/17.
 */

public class Connection {

    private Socket socket;

    public Connection() {
    }

    public Socket connectSocket(){
        try {
            socket = IO.socket("http://192.168.2.36:8080");
            socket.connect();
        }catch (Exception e){
            System.out.print(e);
        }
        return socket;
    }
}
