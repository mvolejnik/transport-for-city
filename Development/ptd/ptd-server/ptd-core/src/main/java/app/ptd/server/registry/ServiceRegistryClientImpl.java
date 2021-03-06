package app.ptd.server.registry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author mvolejnik
 */
public class ServiceRegistryClientImpl implements ServiceRegistryClient {
    
    private InetSocketAddress inetSocketAddress;
    private URI uri;
    private URL url;
    private Charset UTF_8 = Charset.forName("UTF-8");
    private static final Logger l = LogManager.getLogger(ServiceRegistryClientImpl.class);

    public ServiceRegistryClientImpl(InetSocketAddress inetSocketAddress, URI serviceUri, URL url) {
        this.inetSocketAddress = inetSocketAddress;
        this.uri = serviceUri;
        this.url = url;
    }
    
    public URI getServiceUri(){
        return uri;
    }
    
    @Override
    public void register() {
        sendMulticastMessage(new ServiceRegistryMessage(uri, url).toJson("register").getBytes(UTF_8));
    }

    @Override
    public void unregister() {
        sendMulticastMessage(new ServiceRegistryMessage(uri, url).toJson("unregister").getBytes(UTF_8));
    }
    
    private void sendMulticastMessage(byte[] message){
        try (MulticastSocket socket = new MulticastSocket(inetSocketAddress.getPort());){
            socket.joinGroup(inetSocketAddress.getAddress());
            DatagramPacket packet = new DatagramPacket(message, message.length, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            socket.send(packet);
        } catch (IOException ex) {
            l.error("Unable to send UDP datagram!", ex);
        }
    }
    
    
}
