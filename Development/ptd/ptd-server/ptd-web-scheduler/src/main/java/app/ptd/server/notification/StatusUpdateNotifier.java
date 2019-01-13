/*
 * Status update notifier
 */
package app.ptd.server.notification;
 /**
 *
 * @author mvolejnik
 */
public interface StatusUpdateNotifier {

    void send(StatusUpdateNotification notification);

}