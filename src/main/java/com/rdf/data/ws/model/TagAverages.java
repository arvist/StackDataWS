package com.rdf.data.ws.model;

public class TagAverages {
	private Double answeredQuestionPercentage;
	private Double averageAnswerTime;
	private Double averageViewCount;
	private Double averageVoteCount;

	public TagAverages() {
		super();
		this.answeredQuestionPercentage = 0D;
		this.averageAnswerTime = 0D;
		this.averageViewCount = 0D;
		this.averageVoteCount = 0D;
	}

	public Double getAnsweredQuestionPercentage() {
		return answeredQuestionPercentage;
	}

	public void setAnsweredQuestionPercentage(Double answeredQuestionPercentage) {
		this.answeredQuestionPercentage = answeredQuestionPercentage;
	}

	public Double getAverageAnswerTime() {
		return averageAnswerTime;
	}

	public void setAverageAnswerTime(Double averageAnswerTime) {
		this.averageAnswerTime = averageAnswerTime;
	}

	public Double getAverageViewCount() {
		return averageViewCount;
	}

	public void setAverageViewCount(Double averageViewCount) {
		this.averageViewCount = averageViewCount;
	}

	public Double getAverageVoteCount() {
		return averageVoteCount;
	}

	public void setAverageVoteCount(Double averageVoteCount) {
		this.averageVoteCount = averageVoteCount;
	}

	@Override
	public String toString() {
		return "TagAverages [answeredQuestionPercentage=" + answeredQuestionPercentage + ", averageAnswerTime="
				+ averageAnswerTime + ", averageViewCount=" + averageViewCount + ", averageVoteCount="
				+ averageVoteCount + "]";
	}
}