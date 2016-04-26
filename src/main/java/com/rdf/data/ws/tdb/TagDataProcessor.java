package com.rdf.data.ws.tdb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Tag;
import com.rdf.data.ws.model.TagTotals;
import com.rdf.data.ws.repositories.TagsRepository;

@Component
public class TagDataProcessor {
	
	private static final String TAG_RESOURCE = "http://www.stackdata.com/tags/";
	// Properties
	private Property votesProperty;
	private Property viewProperty;
	private Property isAnsweredProperty;
	private Property dcTitleProperty;
	private Property wikiUrlProperty;
	private Property trendsProperty;
	private Property createdDateProperty;
	private SimpleDateFormat formatter;
	// Model
	private Model model;
	@Autowired
	private QuestionDataProcessor questionData;

	public TagDataProcessor() throws FileNotFoundException, IOException {
		model = StackTdbFactory.getInstance().getDefaultModel();

		// Initialize properties
		votesProperty = model.createProperty("http://www.stackdata.com/owl#votes");
		viewProperty = model.createProperty("http://www.stackdata.com/owl#views");
		isAnsweredProperty = model.createProperty("http://www.stackdata.com/owl#isAnswered");
		dcTitleProperty = model.createProperty("http://purl.org/dc/elements/1.1/title");
		wikiUrlProperty = model.createProperty("http://xmlns.com/foaf/0.1/primaryTopic");
		trendsProperty = model.createProperty("http://www.stackdata.com/owl#timeTrend");
		createdDateProperty = model.createProperty("http://www.stackdata.com/owl#createdDate");
		
		formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
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
			Set<String> taggedQuestions = questionData.getQuestionResourcesByTagDatesContinent(tagStr, continent, from, to);
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
			Set<String> taggedQuestions = questionData.getQuestionResourcesByTagDatesCountry(tagStr, country, from, to);
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

			Set<String> taggedQuestions = questionData.getQuestionResourcesByTagAndDates(tagStr, from, to);
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

}
