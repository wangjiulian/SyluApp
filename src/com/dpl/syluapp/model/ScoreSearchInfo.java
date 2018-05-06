package com.dpl.syluapp.model;

import java.io.Serializable;

public class ScoreSearchInfo implements Serializable{
	private String RebuildsocreFlag;
	public String getRebuildsocreFlag() {
		return RebuildsocreFlag;
	}

	public void setRebuildsocreFlag(String rebuildsocreFlag) {
		RebuildsocreFlag = rebuildsocreFlag;
	}

	private String makeupinfo;
	private String papermakeupinfo;
	private String rebuildsocre;
	public String getMakeupinfo() {
		return makeupinfo;
	}

	public void setMakeupinfo(String makeupinfo) {
		this.makeupinfo = makeupinfo;
	}

	public String getPapermakeupinfo() {
		return papermakeupinfo;
	}

	public void setPapermakeupinfo(String papermakeupinfo) {
		this.papermakeupinfo = papermakeupinfo;
	}

	public String getRebuildsocre() {
		return rebuildsocre;
	}

	public void setRebuildsocre(String rebuildsocre) {
		this.rebuildsocre = rebuildsocre;
	}

	private String name;
	private String nature;
	private String credit;
	private String GPA;
	private String ususal;
	private String terminal;
	private String midterm;
	private String experiment;
	private String result;

	@Override
	public String toString() {
		return "ScoreSearchInfo [name=" + name + ", nature=" + nature
				+ ", credit=" + credit + ", GPA=" + GPA + ", ususal=" + ususal
				+ ", terminal=" + terminal + ", midterm=" + midterm
				+ ", experiment=" + experiment + ", result=" + result + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getGPA() {
		return GPA;
	}

	public void setGPA(String gPA) {
		GPA = gPA;
	}

	public String getUsusal() {
		return ususal;
	}

	public void setUsusal(String ususal) {
		this.ususal = ususal;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getMidterm() {
		return midterm;
	}

	public void setMidterm(String midterm) {
		this.midterm = midterm;
	}

	public String getExperiment() {
		return experiment;
	}

	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
