package com.demo.predinsight.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentContainer {

	private static ContentContainer INSTANCE = new ContentContainer();
	
	private ContentContainer() {
	}
	
	public static ContentContainer getInstance()
    {   return INSTANCE;
    }
	
	public void setContent(Content newcontent) {
		//RootPanel.get("content").clear();
		//RootPanel.get("content").add(newcontent);
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(newcontent);
	}
	
}