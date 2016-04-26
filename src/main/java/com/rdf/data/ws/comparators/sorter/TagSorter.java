package com.rdf.data.ws.comparators.sorter;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rdf.data.ws.comparators.tag.AnsweredPercentageComparator;
import com.rdf.data.ws.comparators.tag.AnsweredQuestionComparator;
import com.rdf.data.ws.comparators.tag.AverageAnswerTimeComparator;
import com.rdf.data.ws.comparators.tag.QuestionCountComparator;
import com.rdf.data.ws.comparators.tag.ViewComparatorT;
import com.rdf.data.ws.comparators.tag.TitleComparatorT;
import com.rdf.data.ws.comparators.tag.VoteComparator;
import com.rdf.data.ws.model.Tag;
import com.rdf.data.ws.model.enums.TagMeasurement;

@Component
public class TagSorter {

	@Autowired
	private AnsweredPercentageComparator apq;
	@Autowired
	private AnsweredQuestionComparator aqc;
	@Autowired
	private AverageAnswerTimeComparator aat;
	@Autowired
	private QuestionCountComparator qc;
	@Autowired
	private TitleComparatorT tc;
	@Autowired
	private ViewComparatorT vic;
	@Autowired
	private VoteComparator voc;

	public Comparator<Tag> getComparator(TagMeasurement measurement) {

		switch (measurement) {
		case qc:
			return qc;
		case aqc:
			return aqc;
		case vic:
			return vic;
		case voc:
			return voc;
		case aqp:
			return apq;
		case aat:
			return aat;
		default:
			return tc;
		}
	}
}