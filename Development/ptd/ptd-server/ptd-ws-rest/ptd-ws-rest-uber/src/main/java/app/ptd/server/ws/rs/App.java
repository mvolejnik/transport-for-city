/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ptd.server.ws.rs;

import javax.ws.rs.core.Application;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author mvolejnik
 */
public class App {
    
    private static final String ARG_PORT = "port";
    private static final String DEFAULT_PORT = "8080";
    //private static final String SCHEDULER_JOB_INTERVAL = "schedulerjobinterval";
    //private static final String REGISTRY_MULTICAST_IP = "registryMulticastIp";
    //private static final String REGISTRY_MULGTICAST_PORT = "registryMulticastPort";
    //private static final String SERVICE_STATUS_UPDATE = "serviceStatusUpdate";
            
    public static org.eclipse.jetty.server.Server server(int port)
    {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(port);
        return server;
    }

    private static Options options(){
        var options = new Options();
        options.addOption("p", ARG_PORT, true, "server port");
        //options.addOption("i", SCHEDULER_JOB_INTERVAL, true, "scheduler job interval");
        //options.addOption("ma", REGISTRY_MULTICAST_IP, true, "registry service registry ip address");
        //options.addOption("mp", REGISTRY_MULGTICAST_PORT, true, "registry serivce multicast port");
        //options.addOption("s", SERVICE_STATUS_UPDATE, true, "status update service URN");
        return options;
    }
    
    private static ContextHandler restHandler(){
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        ServletHandler handler = new ServletHandler();
        context.setHandler(handler);
        //ServletHolder servletHolder = new ServletHolder(new ServletContainer());
        ServletHolder servletHolder = handler.addServletWithMapping(ServletContainer.class, "/rest/*");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", "app.ptd.server.ws.rs");
        servletHolder.setInitParameter("jersey.config.server.tracing.type","ALL");
        servletHolder.setInitParameter("jersey.config.server.tracing.threshold","TRACE");
        return context;
    }
    
    public static void main(String[] args) throws Exception
    {
        CommandLine line = new DefaultParser().parse( options(), args );
        org.eclipse.jetty.server.Server server = server(Integer.valueOf(line.getOptionValue(ARG_PORT, DEFAULT_PORT)));
        server.setHandler(restHandler());
        server.start();
        server.join();
    }
    
}
