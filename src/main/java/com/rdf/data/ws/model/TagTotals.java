package com.rdf.data.ws.model;

public class TagTotals {
	private Integer questionCount;
	private Integer answeredQuestionCount;
	private Integer viewCount;
	private Integer voteCount;
	private Long totalAnswerTime;

	public TagTotals() {
		super();
		this.questionCount = 0;
		this.answeredQuestionCount = 0;
		this.viewCount = 0;
		this.voteCount = 0;
		this.totalAnswerTime = 0L;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public Integer getAnsweredQuestionCount() {
		return answeredQuestionCount;
	}

	public void setAnsweredQuestionCount(Integer answeredQuestionCount) {
		this.answeredQuestionCount = answeredQuestionCount;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

	public Long getTotalAnswerTime() {
		return totalAnswerTime;
	}

	public void setTotalAnswerTime(Long totalAnswerTime) {
		this.totalAnswerTime = totalAnswerTime;
	}

	@Override
	public String toString() {
		return "TagTotals [questionCount=" + questionCount + ", answeredQuestionCount=" + answeredQuestionCount
				+ ", viewCount=" + viewCount + ", voteCount=" + voteCount + ", totalAnswerTime=" + totalAnswerTime
				+ "]";
	}

}
