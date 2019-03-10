/*
 */
 package app.ptd.server.notification.impl;
 import app.ptd.server.notification.StatusUpdateNotification;
 import java.io.InputStream;
 
 /**
 *
 * @author mvolejnik
 */
public class StatusUpdateNotificationImpl implements StatusUpdateNotification{
    
    private InputStream content;

    public StatusUpdateNotificationImpl(InputStream content) {
        this.content = content;
    }
    
    
     @Override
    public InputStream content() {
        return content;
    }
 }