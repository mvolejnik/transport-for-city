package app.ptd.server.scheduler;

import app.ptd.server.remoteresources.Resource;
import app.ptd.server.remoteresources.http.HttpResource;
import app.ptd.server.remoteresources.RemoteResourceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class GetUrlResourceJob implements Job {
  static final String DATA_URL = "url";
  private static final Logger l = LogManager.getLogger(GetUrlResourceJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    l.info("execute:: job started [{}]", context.getJobDetail().getKey());
    String urlParam = context.getJobDetail().getJobDataMap().getString(DATA_URL);
    l.debug("execute():: resource url '{}'", urlParam);
    try {
      URL url = new URL(urlParam);
      l.debug("execute():: getting resource '{}'", url.toExternalForm());
      Optional<Resource> resource = new HttpResource().content(url);
      l.debug("execute():: resource '{}' has content '{}'", url.toExternalForm(), resource.isPresent());
    } catch (MalformedURLException | RemoteResourceException e) {
      l.error("Incorrect URL to download resource '{}'", urlParam);
      throw new JobExecutionException(String.format("Unable to download resource '%s'", urlParam), e);
    }
    l.info("execute:: job finished");
  }

}
