/**
Copyright 2012-2013 SMILE Consortium, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 **/
package org.smilec.smile.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;

	private int number;
	private String owner;
	private String ip;
	private String question;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private int answer;
	private String imageUrl;
	private List<Integer> answers = new ArrayList<Integer>();
	private List<Float> ratings = new ArrayList<Float>();

	private double perCorrect;

	public Question() {
		// Empty
	}

	public Question(int number, String owner, String ip, String question,
			String option1, String option2, String option3, String option4,
			int answer, String imageUrl) {
		super();
		this.number = number;
		this.owner = owner;
		this.ip = ip;
		this.question = question;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.answer = answer;
		this.imageUrl = imageUrl;
	}

	public int getNumber() {
		return number;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getQuestion() {
		return question;
	}

	public String getOption1() {
		return option1;
	}

	public String getOption2() {
		return option2;
	}

	public String getOption3() {
		return option3;
	}

	public String getOption4() {
		return option4;
	}

	public int getAnswer() {
		return answer;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean hasImage() {
		return imageUrl != null && imageUrl.trim().length() > 0;
	}

	public List<Integer> getAnswers() {
		return answers;
	}

	public void addAnswer(int answer) {
		answers.add(answer);
	}

	public double getHitAverage() {

		int corrects = 0;
		int n = 0;

		for (Integer i : answers) {
			n++;
			corrects += i == answer ? 1 : 0;
		}

		int value = 0;
		if (corrects != 0 && n != 0) {
			value = (corrects / n) * 100;
		}

		return value;

	}

	public List<Float> getRatings() {
		return ratings;
	}

	public void addRating(float rating) {
		ratings.add(rating);
	}

	public double getRating() {

		float total = 0;
		float n = 0;

		for (Float i : ratings) {
			if (i > 0) {
				total += i;
				n++;
			}
		}

		float value = 0;
		if (total != 0 && n != 0) {
			value = total / n;
		}

		return value;

	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setAnswers(List<Integer> answers) {
		this.answers = answers;
	}

	public void setRatings(List<Float> ratings) {
		this.ratings = ratings;
	}

	public double getPerCorrect() {
		return perCorrect;
	}

	public void setPerCorrect(double perCorrect) {
		this.perCorrect = perCorrect;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(question).append(option1)
				.append(option2).append(option3).append(option4)
				.append(imageUrl).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Question) {
			final Question other = (Question) o;
			return new EqualsBuilder().append(question, other.question)
					.append(option1, other.option1)
					.append(option2, other.option2)
					.append(option3, other.option3)
					.append(option4, other.option4)
					.append(answer, other.answer)
					.append(imageUrl, other.imageUrl).isEquals();
		} else {
			return false;			
		}
	}

}
