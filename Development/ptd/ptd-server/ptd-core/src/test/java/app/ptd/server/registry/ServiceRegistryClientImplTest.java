/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ptd.server.registry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceRegistryClientImplTest {

    private static final URI URN;
    private static URL MSG_URL;
    private static final String MULTICAST_ADDRESS = "233.146.53.49";
    private static final int MULTICAST_PORT = 5678;

    static {
        try {
            URN = new URI("urn:service:test");
            MSG_URL = new URL("https://localhost:8443/test/");
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Test of register method, of class ServiceRegistryClientImpl.
     */
    @Test
    public void testRegister() throws UnknownHostException, IOException, InterruptedException, ExecutionException, TimeoutException {
        System.out.println("register");
        ServiceRegistryClientImpl instance = new ServiceRegistryClientImpl(new InetSocketAddress(MULTICAST_ADDRESS, MULTICAST_PORT), URN, MSG_URL);
        try (final MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);){
            socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
            Future<String> f = Executors.newSingleThreadExecutor().submit(() -> {return receiveMulticastMessage(socket);});
            instance.register();
            String s = f.get(2, TimeUnit.SECONDS);
            String expectedJson = "{\"register\":{\"srv\":\"" + URN + "\",\"url\":\"" + MSG_URL + "\"}}";
            assertEquals(expectedJson, s, "Unexpected multicast message");
            socket.leaveGroup(InetAddress.getByName(MULTICAST_ADDRESS));
        } 
    }

    /**
     * Test of unregister method, of class ServiceRegistryClientImpl.
     */
    @Test
    public void testUnregister() throws UnknownHostException, IOException, InterruptedException, ExecutionException, TimeoutException {
        System.out.println("unregister");
        ServiceRegistryClientImpl instance = new ServiceRegistryClientImpl(new InetSocketAddress(MULTICAST_ADDRESS, MULTICAST_PORT), URN, MSG_URL);
        try (final MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);){
            socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
            Future<String> f = Executors.newSingleThreadExecutor().submit(() -> {return receiveMulticastMessage(socket);});
            instance.unregister();
            String s = f.get(2, TimeUnit.SECONDS);
            String expectedJson = "{\"unregister\":{\"srv\":\"" + URN + "\",\"url\":\"" + MSG_URL + "\"}}";
            assertEquals(expectedJson, s, "Unexpected multicast message");
            socket.leaveGroup(InetAddress.getByName(MULTICAST_ADDRESS));
        } 
    }

    private String receiveMulticastMessage(MulticastSocket socket) throws IOException{
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            socket.receive(recv);
            return new String(recv.getData(), 0, recv.getLength());
    }
    
}
