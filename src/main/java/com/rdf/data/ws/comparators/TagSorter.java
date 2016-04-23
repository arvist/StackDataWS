package com.rdf.data.ws.comparators;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Tag;
import com.rdf.data.ws.model.enums.Measurement;

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
	private TitleComparator tc;
	@Autowired
	private ViewComparator vic;
	@Autowired
	private VoteComparator voc;

	public Comparator<Tag> getComparator(Measurement measurement) {

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