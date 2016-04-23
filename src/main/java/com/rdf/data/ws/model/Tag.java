package com.rdf.data.ws.model;

public class Tag {

	private String title;
	private String wikiUrl;
	private String googleTrendUri;
	private TagTotals totals;
	private TagAverages averages;

	public void initAverages() {
		TagAverages averages = new TagAverages();
		averages.setAnsweredQuestionPercentage(
				(1.0 * totals.getAnsweredQuestionCount()) / (1.0 * totals.getQuestionCount()));
		averages.setAverageViewCount((1.0 * totals.getViewCount()) / (1.0 * totals.getQuestionCount()));
		averages.setAverageVoteCount((1.0 * totals.getVoteCount()) / (1.0 * totals.getQuestionCount()));
		averages.setAverageAnswerTime((1.0 * totals.getTotalAnswerTime()) / (1.0 * totals.getQuestionCount()));
		this.averages = averages;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWikiUrl() {
		return wikiUrl;
	}

	public void setWikiUrl(String wikiUrl) {
		this.wikiUrl = wikiUrl;
	}

	public String getGoogleTrendUri() {
		return googleTrendUri;
	}

	public void setGoogleTrendUri(String googleTrendUri) {
		this.googleTrendUri = googleTrendUri;
	}

	public TagTotals getTotals() {
		return totals;
	}

	public void setTotals(TagTotals totals) {
		this.totals = totals;
	}

	public TagAverages getAverages() {
		return averages;
	}

	public void setAverages(TagAverages averages) {
		this.averages = averages;
	}

	@Override
	public String toString() {
		return "Tag [title=" + title + ", wikiUrl=" + wikiUrl + ", googleTrendUri=" + googleTrendUri + ", totals="
				+ totals + ", averages=" + averages + "]";
	}

}
