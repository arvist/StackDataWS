package com.rdf.data.ws;

import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.annotation.PostConstruct;

import org.apache.jena.rdf.model.Model;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rdf.data.ws.tdb.StackTdbFactory;

@SpringBootApplication
public class StackDataWsApplication {

	public static final String QUESTIONS = "data/questions.rdf";
	public static final String ANSWERS = "data/answers.rdf";
	public static final String USERS = "data/users.rdf";
	public static final String TAGS = "data/tags.rdf";
	public static final String LOCATIONS = "data/location.rdf";

	public static void main(String[] args) {
		SpringApplication.run(StackDataWsApplication.class, args);
	}

	@PostConstruct
	public void run() throws FileNotFoundException {
		Model model = StackTdbFactory.getInstance().getDefaultModel();
		model.enterCriticalSection(true);
		if (!model.isEmpty()) {
			model.removeAll();
			System.out.println("All data from TDB was removed " + model.isEmpty());
		}
		readFileIntoModel(QUESTIONS, model);
		readFileIntoModel(ANSWERS, model);
		readFileIntoModel(USERS, model);
		readFileIntoModel(TAGS, model);
		readFileIntoModel(LOCATIONS, model);
		
		model.leaveCriticalSection();
		
		//validate(model);
	}

	/*private void validate(Model data) throws FileNotFoundException {
		// Model data = FileManager.get().loadModel(fname);
		Model model2 = ModelFactory.createDefaultModel();
		model2.read(StackDataWsApplication.class.getClassLoader().getResourceAsStream("data/ontology.rdf"), null,
				"TURTLE");
		InfModel infmodel = ModelFactory.createInfModel(ReasonerRegistry.getOWLReasoner(),model2,data);
		
		ValidityReport validity = infmodel.validate();
		if (validity.isValid()) {
			System.out.println("OK");
		} else {
			System.out.println("Conflicts");
			for (Iterator i = validity.getReports(); i.hasNext();) {
				System.out.println(" - " + i.next());
			}
		}
	}*/

	private static void readFileIntoModel(String filename, Model model) throws FileNotFoundException {

		// Use the class loader to find the input file
		InputStream in = StackDataWsApplication.class.getClassLoader().getResourceAsStream(filename);

		if (in == null) {
			throw new FileNotFoundException("File not found on classpath: " + filename);
		}

		// Read the triples from the file into the model
		model.read(in, null, "TURTLE");

	}
}
