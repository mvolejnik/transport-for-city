package app.ptd.server.scheduler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@WebListener
public class QuartzInit implements ServletContextListener {

  private static final String CONTEXT_PARAM_DELAY = "schedulerdelay";
  private static final String CONTEXT_PARAM_INTERVAL = "schedulerjobinterval";
  private static final String CONTEXT_PARAM_RND = "schedulerjobintervalrandom";

  private static final Logger l = LogManager.getLogger(QuartzInit.class);
  
   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

  @Override
  public void contextInitialized(ServletContextEvent contextEvent) {
    l.debug("contextInitialized::");
    ServletContext context = contextEvent.getServletContext();
    Duration delay = Duration.parse(context.getInitParameter(CONTEXT_PARAM_DELAY));
    Duration interval = Duration.parse(context.getInitParameter(CONTEXT_PARAM_INTERVAL));
    Duration randomInterval = Duration.parse(context.getInitParameter(CONTEXT_PARAM_RND));
    initQuartz(delay, interval, randomInterval);
  }

  @Override
  public void contextDestroyed(ServletContextEvent contextEvent) {
    l.debug("contextDestroyed::");
    shutdownQuartz();
  }

  private void initQuartz(Duration delay, Duration randomInterval, Duration interval) {
    try {
      l.info("initQuartz:: Initing Quartz Scheduler with delay {}, random maximum interval {}, interval {}", delay,
          randomInterval, interval);
      Random rnd = new Random();
      ZonedDateTime startBaseline = ZonedDateTime.now().plusSeconds(delay.toSeconds());
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.start();

      String jobId = "TBD";
      ZonedDateTime startAt = startBaseline.plusSeconds(rnd.nextInt((int) randomInterval.toSeconds()));
      l.info("initQuartz:: scheduling job [{}] to start since [{}] every [{}]", jobId,
          DATE_TIME_FORMATTER.format(startAt), interval);
      JobDetail job = newJob(GetUrlResourceJob.class).withIdentity(jobId + "~job", "download").build();
      Trigger trigger = newTrigger().withIdentity(jobId + "~trigger", "download")
          .startAt(Date.from(startAt.toInstant()))
          .withSchedule(simpleSchedule().withIntervalInSeconds((int) interval.toSeconds()).repeatForever())
          .build();

      scheduler.scheduleJob(job, trigger);
    } catch (SchedulerException se) {
      l.error("initQuartz:: unable to initialize quartz scheduler", se);
    }
  }

  private void shutdownQuartz() {
    l.info("shutdownQuartz::");
    try {
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.shutdown();
    } catch (SchedulerException se) {
      l.error("shutdownQuartz:: unable to shutdown quartz scheduler", se);
    }
  }

}
