package com.demo.predinsight.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InfDesc extends Composite implements HasClickHandlers {

	private String name;
	private Label title = new Label();
	private Label classLabel = new Label();
	private FocusPanel focus = new FocusPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	
	public InfDesc(String newtitle, String classtext, String[] attributes, double[] values) {
		setName(newtitle);
		title.setText(newtitle);
		classLabel.setText("class : "+ classtext);
		title.setStyleName("subparttitle");
		classLabel.setStyleName("subparttitle");
		mainPanel.add(title);
		mainPanel.add(classLabel);
		focus.add(mainPanel);
		mainPanel.setSize("100%", "100%");
		for(int i=0; i<attributes.length;i++) {
			Canvas canva = SingleInf.createInf(attributes[i], values[i]);
			if(canva == null) {
				Window.alert("Sorry, your browser doesn't support the HTML5 Canvas element");
				break;
			}
			mainPanel.add(canva);
		}
		focus.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				mainPanel.setStyleName("infdescfocused");			
			}
		});
		
		focus.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				mainPanel.setStyleName("infdesc");	
			}
		});
		
		mainPanel.setStyleName("infdesc");	
		
		initWidget(focus);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler){
	        return addDomHandler(handler, ClickEvent.getType());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
