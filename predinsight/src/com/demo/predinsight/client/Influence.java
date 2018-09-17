package com.demo.predinsight.client;

import java.io.Serializable;

public class Influence implements Serializable {
	
	private static final long serialVersionUID = 9001287466417888677L;
	
	public double[] inf;
	public double[] rawinf;
	public double pred;
	public String[] att;
	public double[] instance;
	
	public Influence() {}
	
	public Influence(double[] newinf, double[] newrawinf, double prediction, String[] attr, double[] inst) {
		inf = newinf;
		rawinf = newrawinf;
		pred = prediction;
		att = attr;
		instance = inst;
	}
	
	public Influence(int size) {
		inf = new double[size];
		setRawinf(new double[size]);
		pred = 0;
		att = new String[size];
		setInstance(new double[size]);
	}

	public Influence(Influence oldinf) {
		inf = new double[oldinf.inf.length];
		System.arraycopy(oldinf.inf, 0, inf, 0, oldinf.inf.length);
		rawinf = new double[oldinf.rawinf.length];
		System.arraycopy(oldinf.rawinf, 0, rawinf, 0, oldinf.rawinf.length);
		att = new String[oldinf.att.length];
		System.arraycopy(oldinf.att, 0, att, 0, oldinf.att.length);
		instance = new double[oldinf.instance.length];
		System.arraycopy(oldinf.instance, 0, instance, 0, oldinf.instance.length);
		this.pred = oldinf.pred;
		/*this.inf = oldinf.inf.clone();
		this.rawinf = oldinf.rawinf.clone();
		this.pred = oldinf.pred;
		this.att = oldinf.att.clone();
		this.instance = oldinf.instance.clone();*/
	}
	
	public String[] getAttNames() {
		return att;
	}

	public double[] getInf() {
		return inf;
	}

	public void setInf(double[] inf) {
		this.inf = inf;
	}
	
	public double getPred() {
		return pred;
	}

	
	public void setPred(double prediction) {
		this.pred = prediction;
	}
	
	public void add(Influence newinf) {
		double[] truc = newinf.getInf();
		for(int i=0; i<inf.length; i++) {
			this.inf[i] += truc[i];
		}
	}
	
	public void add(double[] newinf) {
		for(int i=0; i<inf.length; i++) {
			this.inf[i] += newinf[i];
		}
	}
	
	public void mult(double m) {
		for(int i=0; i<inf.length; i++) {
			this.inf[i] = m*this.inf[i];
		}
	}
	
	public int nbatt() {
		return this.inf.length;
	}
	
	public double[] abs() {
		double[] absol = new double[this.inf.length];
		for(int i=0; i<inf.length; i++) {
			absol[i] = Math.abs(inf[i]);
		}
		return absol;
	}

	public double[] sqrt() {
		double[] absol = new double[this.inf.length];
		for(int i=0; i<inf.length; i++) {
			absol[i] = Math.sqrt(inf[i]);
		}
		return absol;
	}

	public double[] getRawinf() {
		return rawinf;
	}

	public void setRawinf(double[] rawinf) {
		this.rawinf = rawinf;
	}

	public String[] getAtt() {
		return att;
	}

	public void setAtt(String[] att) {
		this.att = att;
	}

	public double[] getInstance() {
		return instance;
	}

	public void setInstance(double[] instance) {
		this.instance = instance;
	}
}
