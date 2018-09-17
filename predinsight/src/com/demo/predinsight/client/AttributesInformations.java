package com.demo.predinsight.client;

import java.io.Serializable;
import java.util.ArrayList;

public class AttributesInformations implements Serializable {

	private static final long serialVersionUID = 4824855354610960928L;
	public String[] attNames;
	public String[] attTypes;
	public double[] attmean;
	public double[] attmin;
	public double[] attmax;
	public ArrayList<String[]> attvalues;
	
	private AttributesInformations() {}
	
	public AttributesInformations(String[] newnames, String[] newtypes, double[] newmeans, double[] newmin, double[] newmax, ArrayList<String[]> newvals) {
		attNames = newnames;
		attTypes = newtypes;
		attmean = newmeans;
		attmin = newmin;
		attmax = newmax;
		attvalues = newvals;
	}

	public ArrayList<String[]> getAttvalues() {
		return attvalues;
	}

	public void setAttvalues(ArrayList<String[]> attvalues) {
		this.attvalues = attvalues;
	}

	public double[] getAttmin() {
		return attmin;
	}

	public void setAttmin(double[] attmin) {
		this.attmin = attmin;
	}

	public double[] getAttmax() {
		return attmax;
	}

	public void setAttmax(double[] attmax) {
		this.attmax = attmax;
	}

	public String[] getAttNames() {
		return attNames;
	}

	public void setAttNames(String[] attNames) {
		this.attNames = attNames;
	}

	public String[] getAttTypes() {
		return attTypes;
	}

	public void setAttTypes(String[] attTypes) {
		this.attTypes = attTypes;
	}

	public double[] getAttmean() {
		return attmean;
	}

	public void setAttmean(double[] attmean) {
		this.attmean = attmean;
	}
	
}
