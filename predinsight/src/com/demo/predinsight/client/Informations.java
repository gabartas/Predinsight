package com.demo.predinsight.client;

import java.io.Serializable;

public class Informations implements Serializable {

	private static final long serialVersionUID = -9199010844126088387L;
	
	public Influence[][] desc;
	public double[] trueclasses;
	public int[] selectedIds;
	
	public Informations() {}
	
	public Informations(Influence[][] newdesc, double[] newtrueclasses, int[] selectedinstances) {
		desc = newdesc;
		trueclasses = newtrueclasses;
		selectedIds = selectedinstances;
	}
}
