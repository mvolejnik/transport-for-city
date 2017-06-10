package app.tfc.server.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class GetUrlResourceJob implements Job {
  
  private static final Logger l = LogManager.getLogger(GetUrlResourceJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    l.info("execute:: job started");
System.out.println("============ JOB ===========");
    l.info("execute:: job finished");
  }

}
