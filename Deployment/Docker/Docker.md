## Docker

Docker container for TFC is based on jetty<openjdk<alpine

### Build
#### Prereq
WAR file have to bi in the root of context
```
docker build -t tfc .
Sending build context to Docker daemon 16.95 MB
Step 1/5 : FROM jetty:9.4.5-alpine
 ---> b0b260abf55d
Step 2/5 : ADD tfc-ws-rest.war /var/lib/jetty/webapps/
 ---> 1a54a96ff70d
Removing intermediate container dd912db8573e
Step 3/5 : ADD tfc-web-scheduler.war /var/lib/jetty/webapps/
 ---> f8eee421feba
Removing intermediate container e1f589086257
Step 4/5 : EXPOSE 8080
 ---> Running in 04c8c2b9f451
 ---> 2710ba610be7
Removing intermediate container 04c8c2b9f451
Step 5/5 : ENTRYPOINT /docker-entrypoint.sh
 ---> Running in 6e08875f6d06
 ---> d95bed780ab7
Removing intermediate container 6e08875f6d06
**Successfully built d95bed780ab7**
```

### Run
```
docker run --rm -p 8080:8080 tfc .
2017-06-17 15:26:20.152:INFO::main: Logging initialized @328ms to org.eclipse.jetty.util.log.StdErrLog
2017-06-17 15:26:20.333:INFO:oejs.SetUIDListener:main: Setting umask=02
2017-06-17 15:26:20.341:INFO:oejs.SetUIDListener:main: Opened ServerConnector@18be83e4{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
2017-06-17 15:26:20.341:INFO:oejs.SetUIDListener:main: Setting GID=101
2017-06-17 15:26:20.342:INFO:oejs.SetUIDListener:main: Setting UID=100
2017-06-17 15:26:20.344:INFO:oejs.Server:main: jetty-9.4.5.v20170502
2017-06-17 15:26:20.363:INFO:oejdp.ScanningAppProvider:main: Deployment monitor [file:///var/lib/jetty/webapps/] at interval 1
2017-06-17 15:26:20.790:INFO:oeja.AnnotationConfiguration:main: Scanning elapsed time=245ms
2017-06-17 15:26:21.163:INFO:oejs.session:main: DefaultSessionIdManager workerName=node0
2017-06-17 15:26:21.163:INFO:oejs.session:main: No SessionScavenger set, using defaults
2017-06-17 15:26:21.164:INFO:oejs.session:main: Scavenging every 660000ms
2017-06-17 15:26:21,370 DEBUG [main] a.t.s.s.QuartzInit - contextInitialized::
2017-06-17 15:26:21,377 INFO  [main] a.t.s.s.QuartzInit - initQuartz:: Initing Quartz Scheduler with Delay 60, random interval60, interval 60
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
2017-06-17 15:26:21,421 INFO  [main] a.t.s.s.QuartzInit - initQuartz:: scheduling job [TBD] to start since [17.06.2017 15:27:39.378+0000] every [60] seconds
2017-06-17 15:26:21.444:INFO:oejsh.ContextHandler:main: Started o.e.j.w.WebAppContext@527740a2{/tfc-web-scheduler,file:///tmp/jetty/jetty-0.0.0.0-8080-tfc-web-scheduler.war-_tfc-web-scheduler-any-9201289482805195523.dir/webapp/,AVAILABLE}{/tfc-web-scheduler.war}
2017-06-17 15:26:22.212:INFO:oeja.AnnotationConfiguration:main: Scanning elapsed time=606ms
2017-06-17 15:26:22.786:INFO:oejsh.ContextHandler:main: Started o.e.j.w.WebAppContext@14a50707{/tfc-ws-rest,file:///tmp/jetty/jetty-0.0.0.0-8080-tfc-ws-rest.war-_tfc-ws-rest-any-4164143925779233696.dir/webapp/,AVAILABLE}{/tfc-ws-rest.war}
2017-06-17 15:26:22.789:INFO:oejs.AbstractConnector:main: Started ServerConnector@18be83e4{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
**2017-06-17 15:26:22.789:INFO:oejs.Server:main: Started @2965ms**
```

### Push to repository
Tag image locally and push it to the repository
```
docker tag tfc:latest __user__/tfc:0.0.1
docker push __user__/tfc:0.0.1
```
