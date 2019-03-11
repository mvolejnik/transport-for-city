/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ptd.server.management;

import app.ptd.server.registry.ServiceRegistryClient;
import app.ptd.server.registry.ServiceRegistryClientImpl;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author mvolejnik
 */
@WebListener
public class RegistryInit implements ServletContextListener {

    //private ServiceRegistryClient registryClient;
    private static final String CONTEXT_PARAM_INTERVAL = "schedulerjobinterval";
    private static final String REGISTRY_MULTICAST_IP = "registryMulticastIp";
    private static final String REGISTRY_MULTICAST_PORT = "registryMulticastPort";
    private static final String REGISTRY_STATUS_UPDATE_SERVICE_URI = "serviceStatusUpdate";
    private final ScheduledExecutorService scheduler
            = Executors.newScheduledThreadPool(1);
    private static final Logger l = LogManager.getLogger(RegistryInit.class);

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        l.debug("contextInitialized::");
        try {
            ServletContext context = contextEvent.getServletContext();
            Duration interval = Duration.parse(context.getInitParameter(CONTEXT_PARAM_INTERVAL));
            /*
            String serviceUrl = Arrays.stream(((WebAppContext.Context) context).getContextHandler().getServer().getConnectors())
                    .filter(c -> c.getProtocols().contains("SSL"))
                    .filter(c -> c instanceof ServerConnector)
                    .map(c -> ((ServerConnector) c))
                    .map(c -> String.join(c.getHost(), ":", String.valueOf(c.getPort())))
                    .findFirst().get();
            registryClient = new ServiceRegistryClientImpl(
                    new InetSocketAddress(context.getInitParameter(REGISTRY_MULTICAST_IP).trim(), Integer.parseInt(context.getInitParameter(REGISTRY_MULTICAST_PORT).trim())),// 233.146.53.48 
                    new URI(context.getInitParameter(REGISTRY_STATUS_UPDATE_SERVICE_URI).trim()),
                    new URL(serviceUrl + context.getContextPath() + "/transport"));
             */
            ServiceRegistryClient registryClient = new ServiceRegistryClientImpl(
                    new InetSocketAddress(context.getInitParameter(REGISTRY_MULTICAST_IP).trim(), Integer.parseInt(context.getInitParameter(REGISTRY_MULTICAST_PORT).trim())),// 233.146.53.48 
                    new URI(context.getInitParameter(REGISTRY_STATUS_UPDATE_SERVICE_URI).trim()),
                    new URL("https://localhost:8443" + context.getContextPath() + "/transport"));
            scheduler.scheduleWithFixedDelay(() -> {
                l.debug("Register service {}", registryClient.getServiceUri());
                registryClient.register();
            }, 1, interval.toSeconds(), TimeUnit.SECONDS);
        } catch (MalformedURLException | URISyntaxException ex) {
            l.error("Unable to register service!", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }
    
}
