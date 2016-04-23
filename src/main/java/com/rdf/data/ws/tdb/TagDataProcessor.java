package com.rdf.data.ws.tdb;

import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.rdf.data.ws.model.Tag;
import com.rdf.data.ws.model.TagTotals;
import com.rdf.data.ws.repositories.TagsRepository;

@Component
public class TagDataProcessor {
	private static final String ENCODING_UTF_8 = "UTF-8";
	private Model model;
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
	// Query Strings
	private String selectQuestionsByTagDates;
	private String selectQuestionsByTagDatesCountry;
	private String selectQuestionsByTagDatesContinent;
	// Properties
	private Property votesProperty;
	private Property viewProperty;
	private Property isAnsweredProperty;
	private Property dcTitleProperty;
	private Property wikiUrlProperty;
	private Property trendsProperty;
	private Property createdDateProperty;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

	public TagDataProcessor() throws FileNotFoundException, IOException {
		model = StackTdbFactory.getInstance().getDefaultModel();

		// Initialize queries
		selectQuestionsByTagDates = IOUtils
				.toString(new ClassPathResource(SELECT_QUESTIONS_BY_TAG_DATES).getInputStream(), ENCODING_UTF_8);
		selectQuestionsByTagDatesCountry = IOUtils.toString(
				new ClassPathResource(SELECT_QUESTIONS_BY_TAG_DATES_COUNTRY).getInputStream(), ENCODING_UTF_8);
		selectQuestionsByTagDatesContinent = IOUtils.toString(
				new ClassPathResource(SELECT_QUESTIONS_BY_TAG_DATES_CONITNENT).getInputStream(), ENCODING_UTF_8);

		// Initialize properties
		votesProperty = model.createProperty("http://www.stackdata.com/owl#votes");
		viewProperty = model.createProperty("http://www.stackdata.com/owl#views");
		isAnsweredProperty = model.createProperty("http://www.stackdata.com/owl#isAnswered");
		dcTitleProperty = model.createProperty("http://purl.org/dc/elements/1.1/title");
		wikiUrlProperty = model.createProperty("http://xmlns.com/foaf/0.1/primaryTopic");
		trendsProperty = model.createProperty("http://www.stackdata.com/owl#timeTrend");
		createdDateProperty = model.createProperty("http://www.stackdata.com/owl#createdDate");
	}

	public List<Tag> getAllTagTotalsAndAveragesByDatesContinent(String continent, Date from, Date to) {
		List<Tag> tagList = new ArrayList<Tag>();
		for (String tagStr : TagsRepository.tags) {
			Tag tag = getTagTotalsAndAveragesByTagDatesContinent(tagStr, continent, from, to);
			if (tag != null) {
				if (tag.getTotals().getQuestionCount() > Integer.valueOf(0))
					tagList.add(tag);
			}
		}
		return tagList;
	}

	public List<Tag> getAllTagTotalsAndAveragesByDatesCountry(String country, Date from, Date to) {
		List<Tag> tagList = new ArrayList<Tag>();
		for (String tagStr : TagsRepository.tags) {
			Tag tag = getTagTotalsAndAveragesByTagDatesCountry(tagStr, country, from, to);
			if (tag != null) {
				if (tag.getTotals().getQuestionCount() > Integer.valueOf(0))
					tagList.add(tag);
			}
		}
		return tagList;
	}

	public List<Tag> getAllTagTotalsAndAveragesByDates(Date from, Date to) {
		List<Tag> tagList = new ArrayList<Tag>();
		for (String tagStr : TagsRepository.tags) {
			Tag tag = getTagTotalsAndAveragesByTagAndDates(tagStr, from, to);
			if (tag != null) {
				if (tag.getTotals().getQuestionCount() > Integer.valueOf(0))
					tagList.add(tag);
			}
		}
		return tagList;
	}

	public Tag getTagTotalsAndAveragesByTagDatesContinent(String tagStr, String continent, Date from, Date to) {
		Tag tag = getTag(tagStr);
		if (tag != null) {
			Set<String> taggedQuestions = getQuestionResourcesByTagDatesContinent(tagStr, continent, from, to);
			for (String questionStr : taggedQuestions) {
				updateTagTotalsFromQuestionResource(tag.getTotals(), questionStr);
			}
			tag.initAverages();
		}
		return tag;
	}

	public Tag getTagTotalsAndAveragesByTagDatesCountry(String tagStr, String country, Date from, Date to) {
		Tag tag = getTag(tagStr);
		if (tag != null) {
			Set<String> taggedQuestions = getQuestionResourcesByTagDatesCountry(tagStr, country, from, to);
			for (String questionStr : taggedQuestions) {
				updateTagTotalsFromQuestionResource(tag.getTotals(), questionStr);
			}
			tag.initAverages();
		}
		return tag;
	}

	public Tag getTagTotalsAndAveragesByTagAndDates(String tagStr, Date from, Date to) {

		Tag tag = getTag(tagStr);
		if (tag != null) {

			Set<String> taggedQuestions = getQuestionResourcesByTagAndDates(tagStr, from, to);
			for (String questionStr : taggedQuestions) {
				updateTagTotalsFromQuestionResource(tag.getTotals(), questionStr);
			}
			tag.initAverages();
		}
		return tag;
	}

	private void updateTagTotalsFromQuestionResource(TagTotals tagTotals, String questionResource) {
		try {
			Resource qResource = model.getResource(questionResource);

			// Increment votes if has votes
			Statement votes = qResource.getProperty(votesProperty);
			if (qResource.getProperty(votesProperty) != null) {
				tagTotals.setVoteCount(new Integer(tagTotals.getVoteCount().intValue() + votes.getInt()));
			}
			// Increment view count if has views
			Statement views = qResource.getProperty(viewProperty);
			if (views != null) {
				tagTotals.setViewCount(new Integer(tagTotals.getViewCount().intValue() + views.getInt()));
			}
			// Increment answered question count if is answered
			Statement answer = qResource.getProperty(isAnsweredProperty);
			if (answer != null) {
				tagTotals.setAnsweredQuestionCount(new Integer(tagTotals.getAnsweredQuestionCount().intValue() + 1));
				Statement qCreated = qResource.getProperty(createdDateProperty);

				Resource aResource = model.getResource(answer.getObject().toString());
				Statement aCreated = aResource.getProperty(createdDateProperty);

				try {
					Date createdDate = formatter
							.parse(qCreated.getLiteral().toString().substring(0, 19).replaceFirst("T", "-"));
					Date answeredDate = formatter
							.parse(aCreated.getLiteral().toString().substring(0, 19).replaceFirst("T", "-"));

					tagTotals.setTotalAnswerTime(new Long(tagTotals.getTotalAnswerTime()
							+ ((answeredDate.getTime() - createdDate.getTime()) / (1000 * 60))));
				} catch (Exception e) {
					System.out.println("Failed to parse dates from dataset");
				}
			}
			// Increment question count
			tagTotals.setQuestionCount(new Integer(tagTotals.getQuestionCount().intValue() + 1));
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	private Tag getTag(String tagStr) {
		Tag tag = new Tag();
		tag.setTotals(new TagTotals());
		Resource resource = model.getResource(TAG_RESOURCE.concat(tagStr));
		try {
			tag.setTitle(resource.getProperty(dcTitleProperty).getString());
			tag.setWikiUrl(resource.getProperty(wikiUrlProperty).getString());
			tag.setGoogleTrendUri(resource.getProperty(trendsProperty).getString());
		} catch (Exception e) {
			return null;
		}
		return tag;
	}

	private Set<String> getQuestionResourcesByTagDatesContinent(String tag, String continent, Date from, Date to) {
		String queryStr = (this.bindFromToDateVariables(selectQuestionsByTagDatesContinent, from, to));
		Query query = QueryFactory.create(queryStr);
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.add(TAG_BIND_VAR, model.createResource(TAG_RESOURCE.concat(tag)));
		initialBinding.add(COUNTINENT_BIND_VAR, model.createLiteral(continent));
		return getResources(query, initialBinding, model);
	}

	private Set<String> getQuestionResourcesByTagDatesCountry(String tag, String country, Date from, Date to) {
		String queryStr = (this.bindFromToDateVariables(selectQuestionsByTagDatesCountry, from, to));
		Query query = QueryFactory.create(queryStr);
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.add(TAG_BIND_VAR, model.createResource(TAG_RESOURCE.concat(tag)));
		initialBinding.add(COUNTRY_BIND_VAR, model.createResource(COUNTRY_RESOURCE.concat(country)));
		return getResources(query, initialBinding, model);
	}

	private Set<String> getQuestionResourcesByTagAndDates(String tag, Date from, Date to) {
		String queryStr = (this.bindFromToDateVariables(selectQuestionsByTagDates, from, to));
		Query query = QueryFactory.create(queryStr);
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.add(TAG_BIND_VAR, model.createResource(TAG_RESOURCE.concat(tag)));
		return getResources(query, initialBinding, model);
	}

	private Set<String> getResources(Query query, QuerySolutionMap bindings, Model model) {
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

	private String bindFromToDateVariables(String query, Date fromDate, Date toDate) {

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
