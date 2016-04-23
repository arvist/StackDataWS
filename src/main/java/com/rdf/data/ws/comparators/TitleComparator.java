package com.rdf.data.ws.comparators;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Tag;

@Component
public class TitleComparator implements Comparator<Tag> {

	@Override
	public int compare(Tag o1, Tag o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
