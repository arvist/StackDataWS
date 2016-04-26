package com.rdf.data.ws.comparators.sorter;


import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rdf.data.ws.comparators.question.AnswerCountComparator;
import com.rdf.data.ws.comparators.question.CreatedDateComparator;
import com.rdf.data.ws.comparators.question.ScoreComparator;
import com.rdf.data.ws.comparators.question.TitleComparatorQ;
import com.rdf.data.ws.comparators.question.ViewComparatorQ;
import com.rdf.data.ws.model.Question;
import com.rdf.data.ws.model.enums.QuestionMeasurement;

@Component
public class QuestionSorter {
	
	@Autowired
	private ViewComparatorQ vc;
	@Autowired
	private AnswerCountComparator ac;
	@Autowired
	private ScoreComparator sc;
	@Autowired
	private CreatedDateComparator cd;
	@Autowired
	private TitleComparatorQ tt;
	
	public Comparator<Question> getComparator(QuestionMeasurement measurement) {

		switch (measurement) {
		case vc:
			return vc;
		case ac:
			return ac;
		case sc:
			return sc;
		case cd:
			return cd;
		case tt:
			return tt;
		default:
			return tt;
		}
	}

}
