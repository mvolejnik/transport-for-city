package app.ptd.server.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.URL;
import javax.json.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author mvolejnik
 */
public class ServiceRegistryClientImpl implements ServiceRegistryClient {
    
    private InetSocketAddress inetSocketAddress;
    private URI uri;
    private URL url;
    private static final String JSON_PROPERTY_SERVICE = "srv";
    private static final String JSON_PROPERTY_URL = "url";
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
        sendMulticastMessage(message("register").getBytes());
    }

    @Override
    public void unregister() {
        sendMulticastMessage(message("unregister").getBytes());
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
    
    String message(String action){
        l.debug("registryMessage()::");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Json.createGenerator(os).writeStartObject()
                .writeStartObject(action)
                .write(JSON_PROPERTY_SERVICE, uri.toString())
                .write(JSON_PROPERTY_URL, url.toString())
                .writeEnd()
                .writeEnd()
                .close();
        return os.toString();
    }
    
}
