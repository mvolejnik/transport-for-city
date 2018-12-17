/*
 */
 package app.ptd.server.notification.impl;
 import app.ptd.server.notification.StatusUpdateNotification;
 /**
 *
 * @author mvolejnik
 */
public class StatusUpdateNotificationImpl implements StatusUpdateNotification{
     @Override
    public String content() {
        return "{}";
    }
 }