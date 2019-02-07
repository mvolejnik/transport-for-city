package app.ptd.server.scheduler;

import java.net.MalformedURLException;
import java.net.URL;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@WebListener
public class QuartzInit implements ServletContextListener {
  private static final String CONTEXT_PARAM_DELAY = "schedulerdelay";
  private static final String CONTEXT_PARAM_INTERVAL = "schedulerjobinterval";
  private static final String CONTEXT_PARAM_RND = "schedulerjobintervalrandom";
  private static final String CONTEXT_PARAM_OPERATOR = "operators";
  private static final Map<String, URL> OPERATORS = new HashMap<>();
  private static final String CONTEXT_PARAM_VALUES_SEPARATOR = ";";
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
  private static final Logger l = LogManager.getLogger(QuartzInit.class);

  @Override
  public void contextInitialized(ServletContextEvent contextEvent) {
    l.debug("contextInitialized::");
    ServletContext context = contextEvent.getServletContext();
    Duration delay = Duration.parse(context.getInitParameter(CONTEXT_PARAM_DELAY));
    Duration interval = Duration.parse(context.getInitParameter(CONTEXT_PARAM_INTERVAL));
    Duration randomInterval = Duration.parse(context.getInitParameter(CONTEXT_PARAM_RND));
    OPERATORS.putAll(initOperatorsResources(context));
    OPERATORS.entrySet().stream()
      .filter(e -> e.getValue() != null)
      .forEach(e -> initQuartzResourceJob(delay, interval, randomInterval, e.getKey(), e.getValue()));
  }

  private Map<String, URL> initOperatorsResources(ServletContext context) {
        return Pattern.compile(CONTEXT_PARAM_VALUES_SEPARATOR)
                .splitAsStream(context.getInitParameter(CONTEXT_PARAM_OPERATOR))
                .map(s -> s.trim().split("="))
                .filter(e -> e[1] != null)
                .collect(Collectors.toMap(c -> c[0], c -> {
                    try {
                        return new URL(c[1]); 
                    } catch (MalformedURLException e) {
                        l.error("Unable to parse URL.", e);
                        return null;
                    }
                }));
     }

  @Override
  public void contextDestroyed(ServletContextEvent contextEvent) {
    l.debug("contextDestroyed::");
    shutdownQuartz();
  }

  private void initQuartzResourceJob(Duration delay, Duration interval, Duration randomInterval, String jobId, URL url) {
      Objects.requireNonNull(url, "url cannot be null");
      if (jobId == null) {
        jobId = url.getHost() + "~" + new Random().nextInt();
      }
      try {
      l.info("initQuartz:: Initing Quartz Scheduler with Delay [{}], interval [{}], random interval [{}], jobId [{}], jobUrl [{}]", delay,
                    interval, randomInterval, jobId, url.toExternalForm());
      Random rnd = new Random();
      ZonedDateTime startBaseline = ZonedDateTime.now().plusSeconds(delay.toSeconds());
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.start();

      ZonedDateTime startAt = startBaseline.plusSeconds(rnd.nextInt((int) randomInterval.toSeconds()));
      l.info("initQuartz:: scheduling job [{}] to start since [{}] every [{}]", jobId,
          DATE_TIME_FORMATTER.format(startAt), interval);
      JobDetail job = newJob(GetUrlResourceJob.class)
              .withIdentity(jobId + "~job", "download")
              .usingJobData(GetUrlResourceJob.DATA_URL, url.toExternalForm())
              .build();
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
