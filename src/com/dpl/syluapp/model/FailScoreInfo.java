package com.dpl.syluapp.model;

import java.io.Serializable;

public class FailScoreInfo implements Serializable{
	
	private String name;
	private String score;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScore() {
		return score;
	}
	@Override
	public String toString() {
		return "FailScoreInfo [name=" + name + ", score=" + score + "]";
	}
	public void setScore(String score) {
		this.score = score;
	}

}
