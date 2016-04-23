package com.rdf.data.ws.comparators;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Tag;

@Component
public class ViewComparator implements Comparator<Tag> {

	@Override
	public int compare(Tag o1, Tag o2) {
		if (o1.getTotals().getViewCount() > o2.getTotals().getViewCount()) {
			return 1;
		} else if (o1.getTotals().getViewCount() < o2.getTotals().getViewCount()) {
			return -1;
		} else {
			return 0;
		}
	}
}
