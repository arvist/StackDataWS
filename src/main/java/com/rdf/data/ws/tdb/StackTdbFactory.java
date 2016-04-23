package com.rdf.data.ws.tdb;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;

public class StackTdbFactory {

	private Dataset dataset = TDBFactory.createDataset("C:\\tbd");
	public static final String QUESTION_MODEL = "Question";
	public static final String TAG_MODEL = "Tag";
	public static final String USER_MODEL = "User";
	public static final String ANSWER_MODEL = "Answer";
	public static final String LOCATION_MODEL = "Location";

	private static StackTdbFactory instance = null;

	protected StackTdbFactory() {
	}

	public static StackTdbFactory getInstance() {
		if (instance == null) {
			instance = new StackTdbFactory();
		}
		return instance;
	}

	public Model getDefaultModel() {
		Model model = dataset.getDefaultModel();
		TDB.sync(model);
		return model;
	}
	
}
