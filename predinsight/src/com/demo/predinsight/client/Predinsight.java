package com.demo.predinsight.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;


public class Predinsight implements EntryPoint {
	
	private Label truc = new Label("BonJour");
	
	public void onModuleLoad() {
		ContentContainer.getInstance().setContent(new Selection());
	}
}

