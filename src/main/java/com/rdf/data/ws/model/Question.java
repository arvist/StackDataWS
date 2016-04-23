package com.rdf.data.ws.model;

import java.util.Date;
import java.util.List;

public class Question {

	private Integer questionId;
	private List<String> tags;
	private Integer viewCount;
	private Integer answerCount;
	private Integer acceptedAnswerId;
	private Integer score;
	private Date createdDate;
	private String selfLink;
	private String title;
	private String ownerUserId;

	public String getOwnerUserId() {
		return this.ownerUserId;
	}

	public void setOwnerUserId(String id) {
		this.ownerUserId = id;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	public Integer getAcceptedAnswerId() {
		return acceptedAnswerId;
	}

	public void setAcceptedAnswerId(Integer acceptedAnswerId) {
		this.acceptedAnswerId = acceptedAnswerId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title.replaceAll(" ", "-").toLowerCase();
	}

	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", tags=" + tags + "viewCount=" + viewCount + ", answerCount="
				+ answerCount + ", acceptedAnswerId=" + acceptedAnswerId + ", score=" + score + ", createdDate="
				+ createdDate + ", selfLink=" + selfLink + ", title=" + title + "]";
	}

}
