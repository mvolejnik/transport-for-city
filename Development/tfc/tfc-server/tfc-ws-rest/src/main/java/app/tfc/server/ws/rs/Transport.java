package app.tfc.server.ws.rs;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import app.tfc.server.management.Metrics;
import app.tfc.server.model.Cities;
import app.tfc.server.model.Countries;
import app.tfc.server.model.Lines;
import app.tfc.server.ws.json.JsonIdentifiables;

import static app.tfc.server.management.Metrics.MetricsNames.*;

@Path("/transport")
public class Transport {
	
	private static final Logger l = LogManager.getLogger(Transport.class);
	private static final Timer T_COUNTRIES = Metrics.REGISTRY.timer(MetricRegistry.name(Transport.class, METRICS_TRANSPORT_COUTNRIES.getName()));
	private static final Timer T_CITIES = Metrics.REGISTRY.timer(MetricRegistry.name(Transport.class, METRICS_TRANSPORT_CITIES.getName()));
	private static final Timer T_OPERATORS = Metrics.REGISTRY.timer(MetricRegistry.name(Transport.class, METRICS_TRANSPORT_OPERATORS.getName()));
	private static final Timer T_LINES = Metrics.REGISTRY.timer(MetricRegistry.name(Transport.class, METRICS_TRANSPORT_LINES.getName()));

	JsonIdentifiables identifialbes = new JsonIdentifiables();

	@GET
	@Path("/countries")
	@Produces("application/json")
	public Response countries() {
		l.debug("countries()::");
		try {
			StreamingOutput stream = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					l.debug("write()::");
					final Timer.Context tc = T_COUNTRIES.time();
					try {
						identifialbes.identifiables(output, Countries.PROTOTYPE);//TODO
					} catch (Exception e) {
						l.error("write():: Unspecific Error occured!", e);
						throw new WebApplicationException(e);
					} finally {
						tc.stop();
					}
				}
			};
			return Response.ok(stream).build();
		} catch (Exception e){
			l.error("countries():: Unspecific Error occured!", e);
			return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/countries/{country}/cities")
	@Produces("application/json")
	//@Timed(name=METRICS_TRANSPORT_CITIES)
	public Response cities(@PathParam("country") String country) {
		l.debug("cities()::");
		try {
			StreamingOutput stream = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					l.debug("write()::");
					final Timer.Context tc = T_CITIES.time();
					try {
						identifialbes.identifiables(output, Cities.PROTOTYPE);//TODO
					} catch (Exception e) {
						l.error("write():: Unspecific Error occured!", e);
						throw new WebApplicationException(e);
					} finally {
						tc.stop();
					}
				}
			};
			return Response.ok(stream).build();
		} catch (Exception e){
			l.error("Unspecific Error occured!", e);
			return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/countries/{country}/cities/{city}/operators/{operator}")
	@Produces("application/json")
	public Response operator(@PathParam("country") String country, @PathParam("city") String city, @PathParam("operator") String operator) {
		l.debug("operators()::");
		try {
			StreamingOutput stream = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					l.debug("write()::");
					final Timer.Context tc = T_OPERATORS.time();
					try {
						identifialbes.identifiables(output, Cities.PROTOTYPE);//TODO
					} catch (Exception e) {
						l.error("write():: Unspecific Error occured!", e);
						throw new WebApplicationException(e);
					} finally {
						tc.stop();
					}
				}
			};
			return Response.ok(stream).build();
		} catch (Exception e){
			l.error("Unspecific Error occured!", e);
			return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/countries/{country}/cities/{city}/lines")
	@Produces("application/json")
	public Response lines(@PathParam("country") String country, @PathParam("city") String city) {
		l.debug("lines():: Country Code [%s], City Code [%s], Operator [%s]");
		try {
			StreamingOutput stream = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					l.debug("write()::");
					final Timer.Context tc = T_LINES.time();
					try {
						identifialbes.identifiables(output, Lines.PROTOTYPE);//TODO
					} catch (Exception e) {
						l.error("write():: Unspecific Error occured!", e);
						throw new WebApplicationException(e);
					} finally {
						tc.stop();
					}
				}
			};
			return Response.ok(stream).build();
		} catch (Exception e){
			l.error("lines():: Unspecific Error occured!", e);
			return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
