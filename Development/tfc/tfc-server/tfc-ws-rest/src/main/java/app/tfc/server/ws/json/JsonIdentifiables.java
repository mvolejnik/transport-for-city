package app.tfc.server.ws.json;

import java.io.OutputStream;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import app.tfc.server.model.AbstractIdentification;
import app.tfc.server.model.Cities;
import app.tfc.server.model.Identifiable;
import app.tfc.server.model.Identifiables;

public class JsonIdentifiables {

	private static final Logger l = LogManager.getLogger(JsonIdentifiables.class);
	
	private static final String CITY_CODE = "code" ;
	private static final String CITY_NAME = "name" ;
	
	public void identifiables(OutputStream citiesOs, Identifiables<? extends Identifiable> objects){
		l.debug("identifiables()::");
		JsonGenerator gen = Json.createGenerator(citiesOs);	
		gen.writeStartObject(); // {
		gen.writeStartArray(objects.getName()); // {
		for (Identifiable i : objects){
			l.trace("identifiables():: Writing %s [%s] to JSON stream.", objects.getName(), i);
			gen.writeStartObject();
			gen.write(CITY_CODE, i.getCode());
			gen.write(CITY_NAME, i.getName());
			gen.writeEnd();
		}
		gen.writeEnd();
		gen.writeEnd();
		gen.close();
	}

}
