package app.ptd.server.scheduler;

import app.ptd.server.notification.impl.HttpClientNotifier;
import app.ptd.server.notification.impl.StatusUpdateNotificationImpl;
import app.ptd.server.remoteresources.http.HttpResource;
import app.ptd.server.remoteresources.RemoteResourceException;
import app.ptd.server.status.ResourceCache;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GetUrlResourceJob implements Job {

    private static final ResourceCache RESOURCE_CACHE = new ResourceCache();
    private static final int NOTIFICATION_POOL_SIZE = 5; 
    private static final ExecutorService NOTIFICATION_EXECUTOR = Executors.newFixedThreadPool(NOTIFICATION_POOL_SIZE);
    static final String DATA_URL = "url";
    private static final Logger l = LogManager.getLogger(GetUrlResourceJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    l.info("execute:: job started [{}]", context.getJobDetail().getKey());
    var urlParam = context.getJobDetail().getJobDataMap().getString(DATA_URL);
    l.debug("execute:: resource url '{}'", urlParam);
    try {
      var url = new URL(urlParam);
      var cached = RESOURCE_CACHE.resource(url);
      l.debug("execute:: resource '{}' already in cache '{}' with fingerprint '{}'", url, cached.isPresent(), cached.isPresent() ? cached.get().digest() : "");
      l.debug("execute:: getting resource '{}'", url.toExternalForm());
      var resource = new HttpResource().content(url, cached.isPresent() ? cached.get().fingerprint() : Optional.empty(), Optional.empty());
      l.debug("execute:: resource has content '{}'", resource.isPresent());
      resource.ifPresent(r -> {
          notifyServices(r.content().get());
          RESOURCE_CACHE.resource(url, r);
      });
    } catch (MalformedURLException | RemoteResourceException e) {
      l.error("Incorrect URL to download resource '{}'", urlParam);
      throw new JobExecutionException(String.format("Unable to download resource '%s'", urlParam), e);
    } catch (Exception e){
      l.error("Exception has occured", e);
      throw e;
    }
    l.info("execute:: job finished");
  }
  
  private void notifyServices(InputStream content){
      NOTIFICATION_EXECUTOR.submit(() -> new HttpClientNotifier().send(new StatusUpdateNotificationImpl(content)));
  }

}
