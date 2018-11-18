import com.googlecode.jsonrpc4j.JsonRpcBasicServer;
import com.googlecode.jsonrpc4j.StreamServer;
import java.net.ServerSocket;
import java.net.InetAddress;


public class JSonRpc {
    private static UserServiceImpl userService;
    private static JsonRpcBasicServer jsonRpcServer;
    private static StreamServer streamServer;
    
    public JSonRpc() {
        this.userService = new UserServiceImpl();
        jsonRpcServer = new JsonRpcBasicServer(this.userService, UserService.class);
    }

    public static void startServer(int port) throws Exception {
        int maxThreads = 10;
        ServerSocket sct = new ServerSocket(port);
        System.out.println(sct.getInetAddress());
        streamServer = new StreamServer(jsonRpcServer, maxThreads, sct);
        streamServer.start();
    }

    public static void stopServer() throws Exception {
        streamServer.stop();
    }

    public static interface UserService {
        public int getUserCount(int count);
    }

    private static class UserServiceImpl implements UserService {
        public int getUserCount(int count) {
            return count;
        }
    }

    public static void main (String[] args) throws Exception {
        JSonRpc jr = new JSonRpc();
        jr.startServer(Integer.parseInt(args[0]));
    }

}
