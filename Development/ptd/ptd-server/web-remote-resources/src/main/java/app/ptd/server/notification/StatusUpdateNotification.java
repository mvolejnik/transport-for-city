/*
 * Status update notification
 */
package app.ptd.server.notification;

import java.io.InputStream;

 /**
 *
 * @author mvolejnik
 */
public interface StatusUpdateNotification {

    public InputStream content();

}
