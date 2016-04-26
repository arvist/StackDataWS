package com.rdf.data.ws.tdb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.rdf.data.ws.model.Question;

@Component
public class QuestionDataProcessor {

	private static final String ENCODING_UTF_8 = "UTF-8";
	// Resource URIs
	private static final String TAG_RESOURCE = "http://www.stackdata.com/tags/";
	private static final String COUNTRY_RESOURCE = "http://www.stackdata.com/countries/";
	// Query resources
	private static final String SELECT_QUESTIONS_BY_TAG_DATES = "./queries/selectQuestionsByTagDates.rq";
	private static final String SELECT_QUESTIONS_BY_TAG_DATES_COUNTRY = "./queries/selectQuestionsByTagDatesCountry.rq";
	private static final String SELECT_QUESTIONS_BY_TAG_DATES_CONITNENT = "./queries/selectQuestionsByTagDatesContinent.rq";
	// Bind variables
	private static final String TAG_BIND_VAR = "tag";
	private static final String COUNTRY_BIND_VAR = "country";
	private static final String QUESTION_BIND_VAR = "question";
	private static final String COUNTINENT_BIND_VAR = "continent";

	// Properties
	private Property titleProperty;
	private Property answerCountProperty;
	private Property createdDateProperty;
	private Property hasOwnerProperty;
	private Property isAnsweredProperty;
	private Property isTaggedWithProperty;
	private Property viewsProperty;
	private Property votesProperty;
	private Property sameAsProperty;
	private SimpleDateFormat formatter;
	// Model
	private Model model;
	// Query Strings
	private String selectQuestionsByTagDates;
	private String selectQuestionsByTagDatesCountry;
	private String selectQuestionsByTagDatesContinent;

	public QuestionDataProcessor() throws FileNotFoundException, IOException {
		model = StackTdbFactory.getInstance().getDefaultModel();

		// Initialize queries

		// Initialize queries
		selectQuestionsByTagDates = IOUtils
				.toString(new ClassPathResource(SELECT_QUESTIONS_BY_TAG_DATES).getInputStream(), ENCODING_UTF_8);
		selectQuestionsByTagDatesCountry = IOUtils.toString(
				new ClassPathResource(SELECT_QUESTIONS_BY_TAG_DATES_COUNTRY).getInputStream(), ENCODING_UTF_8);
		selectQuestionsByTagDatesContinent = IOUtils.toString(
				new ClassPathResource(SELECT_QUESTIONS_BY_TAG_DATES_CONITNENT).getInputStream(), ENCODING_UTF_8);

		// Initialize properties
		titleProperty = model.createProperty("http://purl.org/dc/elements/1.1/title");
		answerCountProperty = model.createProperty("http://www.stackdata.com/owl#answerCount");
		createdDateProperty = model.createProperty("http://www.stackdata.com/owl#createdDate");
		hasOwnerProperty = model.createProperty("http://www.stackdata.com/owl#hasOwner");
		isAnsweredProperty = model.createProperty("http://www.stackdata.com/owl#isAnswered");
		isTaggedWithProperty = model.createProperty("http://www.stackdata.com/owl#isTaggedWith");
		viewsProperty = model.createProperty("http://www.stackdata.com/owl#views");
		votesProperty = model.createProperty("http://www.stackdata.com/owl#votes");
		sameAsProperty = model.createProperty("http://www.w3.org/2002/07/owl#sameAs");

		formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	}
	
	public List<Question> getQuestionsByTagDates(String tag, Date startTime, Date endTime) {

		List<Question> list = new ArrayList<Question>();
		Set<String> set = getQuestionResourcesByTagAndDates(tag, startTime, endTime);
		for (String str : set) {
			String resParts[] = str.split("/");
			try {
				list.add(getQuestionById(new Integer(resParts[resParts.length - 1])));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List<Question> getQuestionsByTagDatesContinent(String tag, String continent, Date startTime, Date endTime) {

		List<Question> list = new ArrayList<Question>();
		Set<String> set = getQuestionResourcesByTagDatesCountry(tag, continent, startTime, endTime);
		for (String str : set) {
			String resParts[] = str.split("/");
			try {
				list.add(getQuestionById(new Integer(resParts[resParts.length - 1])));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	
	public List<Question> getQuestionsByTagDatesCountry(String tag, String country, Date startTime, Date endTime) {

		List<Question> list = new ArrayList<Question>();
		Set<String> set = getQuestionResourcesByTagDatesCountry(tag, country, startTime, endTime);
		for (String str : set) {
			String resParts[] = str.split("/");
			try {
				list.add(getQuestionById(new Integer(resParts[resParts.length - 1])));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public Set<String> getQuestionResourcesByTagDatesContinent(String tag, String continent, Date from, Date to) {
		String queryStr = (this.bindFromToDateVariables(selectQuestionsByTagDatesContinent, from, to));
		Query query = QueryFactory.create(queryStr);
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.add(TAG_BIND_VAR, model.createResource(TAG_RESOURCE.concat(tag)));
		initialBinding.add(COUNTINENT_BIND_VAR, model.createLiteral(continent));
		return getResources(query, initialBinding, model);
	}

	public Set<String> getQuestionResourcesByTagDatesCountry(String tag, String country, Date from, Date to) {
		String queryStr = (this.bindFromToDateVariables(selectQuestionsByTagDatesCountry, from, to));
		Query query = QueryFactory.create(queryStr);
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.add(TAG_BIND_VAR, model.createResource(TAG_RESOURCE.concat(tag)));
		initialBinding.add(COUNTRY_BIND_VAR, model.createResource(COUNTRY_RESOURCE.concat(country)));
		return getResources(query, initialBinding, model);
	}

	public Set<String> getQuestionResourcesByTagAndDates(String tag, Date from, Date to) {
		String queryStr = (this.bindFromToDateVariables(selectQuestionsByTagDates, from, to));
		Query query = QueryFactory.create(queryStr);
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.add(TAG_BIND_VAR, model.createResource(TAG_RESOURCE.concat(tag)));
		return getResources(query, initialBinding, model);
	}
	
 	public Set<String> getResources(Query query, QuerySolutionMap bindings, Model model) {
		HashSet<String> taggedQuestions = new HashSet<String>();
		try (QueryExecution qexec = QueryExecutionFactory.create(query, model, bindings)) {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				taggedQuestions.add(soln.get(QUESTION_BIND_VAR).toString());
			}
		}
		return taggedQuestions;
	}

	
	public Question getQuestionById(int id) throws ParseException {
		Question question = new Question();
		question.setQuestionId(new Integer(id + ""));
		Resource questionR = model.getResource("http://www.stackdata.com/questions/".concat(String.valueOf(id)));
		question.setTitle(questionR.getProperty(titleProperty).getString());

		Statement answeredR = questionR.getProperty(isAnsweredProperty);
		if (answeredR != null) {
			String[] answer = answeredR.getObject().toString().split("/");
			question.setAcceptedAnswerId(new Integer(answer[answer.length - 1]));
		}
		
		question.setAnswerCount(new Integer(questionR.getProperty(answerCountProperty).getInt()));
		String dateStr = questionR.getProperty(createdDateProperty).getLiteral().toString().substring(0, 19)
				.replaceFirst("T", "-");
		question.setCreatedDate(formatter.parse(dateStr));
		String owner[] = questionR.getProperty(hasOwnerProperty).getObject().toString().split("/");
		question.setOwnerUserId(owner[owner.length - 1]);
		question.setScore(new Integer(questionR.getProperty(votesProperty).getObject().asLiteral().getInt()));
		question.setViewCount(new Integer(questionR.getProperty(viewsProperty).getString()));
		ArrayList<String> tags = new ArrayList<String>();
		// Tags
		String[] tagStr = questionR.getProperty(isTaggedWithProperty).getObject().toString().split("/");
		tags.add(tagStr[tagStr.length - 1]);
		question.setTags(tags);
		question.setSelfLink(questionR.getProperty(sameAsProperty).getObject().toString());
		return question;
	}

	public String bindFromToDateVariables(String query, Date fromDate, Date toDate) {

		if (query == null) {
			throw new IllegalArgumentException("Query null, can't bind date variables");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		if (fromDate == null) {
			fromDate = new Date(0L); // Set date to 1970-01-01
		}
		if (toDate == null) {
			toDate = new Date(2082758400000L); // Set date to 2036-01-01
		}

		query = query.replaceAll("\\?fromDate", "\"" + dateFormat.format(fromDate) + "T00:00:00\"^^xsd:dateTime");
		query = query.replaceAll("\\?toDate", "\"" + dateFormat.format(toDate) + "T00:00:00\"^^xsd:dateTime");
		return query;
	}
}
