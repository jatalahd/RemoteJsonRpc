import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

// TCP and UDP socket sender application.
// USAGE: java -jar JSocketSender.jar -hostIp 192.168.2.1 -port 8888 -mode UDP -msg "message to send"


public class JSocketSender {
	
    private static class CmdArguments {
        
        @Parameter(names = "-hostIp", description = "host IP to connect to", required = true)
        private String hostIp = "";
        
        @Parameter(names = "-port", description = "Port to connect to", required = true)
        private Integer port = 8089;   
        
        @Parameter(names = "-mode", description = "TCP or UDP", required = false)
        private String mode = "TCP";  
        
        @Parameter(names = "-msg", description = "message to send", required = true)
        private String msg = "";
    }
    
    public static void main( String[] args ) throws Exception {
    	Socket socket = null;
        DatagramSocket datagram = null;
        CmdArguments ca = new CmdArguments();
        JCommander cmd = new JCommander(ca);
        
        try {
            cmd.parse(args);
            
            if (ca.mode.equals("TCP")) {
                socket = new Socket(ca.hostIp,ca.port);
                BufferedWriter out = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream(), StandardCharsets.UTF_8 ) );
                out.write(ca.msg + '\n');
                out.flush();
                System.out.println("Sent TCP message " + ca.msg + " to " + ca.hostIp + ":" + ca.port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String line = in.readLine();  // reads until '\n'
                System.out.println("Text received: " + line);
                in.close();
                out.close();
            } else if (ca.mode.equals("UDP")) {
                datagram = new DatagramSocket();
                byte[] data = ca.msg.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(ca.hostIp), ca.port);
                datagram.send(sendPacket);
                System.out.println("Sent UDP message " + ca.msg + " to " + ca.hostIp + ":" + ca.port);
            }                

        } catch (Exception ex) {
            cmd.usage();
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (datagram != null) {
                datagram.close();
            }
        }        
    }
}
