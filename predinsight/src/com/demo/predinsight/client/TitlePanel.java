package com.demo.predinsight.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class TitlePanel extends Composite {

	private Label title = new Label("Predinsight");
	private HorizontalPanel mainPanel = new HorizontalPanel();


	public TitlePanel() {
		title.setStyleName("title");
		mainPanel.add(title);
		mainPanel.addStyleName("titlepanel");
		initWidget(mainPanel);
	}
}
