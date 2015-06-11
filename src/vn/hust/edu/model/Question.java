package vn.hust.edu.model;

import java.util.List;

public class Question {
	private int id;
	private String subject;
	private String linkImage;
	private String linkAudio;
	private String content;
	private List<QuesContruct> question;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getLinkImage() {
		return linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}

	public String getLinkAudio() {
		return linkAudio;
	}

	public void setLinkAudio(String linkAudio) {
		this.linkAudio = linkAudio;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<QuesContruct> getQuestion() {
		return question;
	}

	public void setQuestion(List<QuesContruct> question) {
		this.question = question;
	}

}
