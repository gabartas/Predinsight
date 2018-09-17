package com.demo.predinsight.client;

import java.util.ArrayList;
import java.lang.System;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InstanceDesc extends Composite {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label classtitle = new Label();
	private Label insttitle = new Label();
	private FlexTable attrTable = new FlexTable();
	
	public InstanceDesc(String insTitle, String insClass, ArrayList<String> attributes, ArrayList<String> values) {
		
		attrTable.setText(0, 0, "Attribute");
		attrTable.setText(0, 1, "Value");
		
		for(int i = 0; i<attributes.size(); i++) {
			attrTable.setText(i+1, 0, attributes.get(i));
			attrTable.setText(i+1, 1, values.get(i));
		}
		
		insttitle.setText("Instance : " + insTitle);
		classtitle.setText("Class : " + insClass);
		mainPanel.add(insttitle);
		mainPanel.add(classtitle);
		mainPanel.add(attrTable);
		
		insttitle.setStyleName("instdesctitle");
		classtitle.setStyleName("instdesctitle");
		attrTable.setStyleName("instdesctable");
		attrTable.getRowFormatter().setStyleName(0, "instdescheader");
		attrTable.getCellFormatter().addStyleName(0, 0, "instdescnameColumn");
		attrTable.getCellFormatter().addStyleName(0, 1, "instdescvalueColumn");
		
		initWidget(mainPanel);
		
	}
	
	public InstanceDesc(String insTitle, String insClass, String[] attributes, String[] values) {
			
		attrTable.setText(0, 0, "Attribute");
		attrTable.setText(0, 1, "Value");
		
		for(int i = 0; i<attributes.length; i++) {
			attrTable.setText(i+1, 0, attributes[i]);
			attrTable.setText(i+1, 1, values[i]);
		}
		
		insttitle.setText("Instance : " + insTitle);
		classtitle.setText("Class : " + insClass);
		mainPanel.add(insttitle);
		mainPanel.add(classtitle);
		mainPanel.add(attrTable);
		
		insttitle.setStyleName("instdesctitle");
		classtitle.setStyleName("instdesctitle");
		attrTable.setStyleName("instdesctable");
		attrTable.getRowFormatter().setStyleName(0, "instdescheader");
		attrTable.getCellFormatter().addStyleName(0, 0, "instdescnameColumn");
		attrTable.getCellFormatter().addStyleName(0, 1, "instdescvalueColumn");
		
		initWidget(mainPanel);
		
	}
	
	public InstanceDesc(String insTitle, String insClass, String[] attributes, double[] values) {
		attrTable.setText(0, 0, "Attribute");
		attrTable.setText(0, 1, "Value");	
		
		for(int i = 0; i<attributes.length; i++) {
			attrTable.setText(i+1, 0, attributes[i]);
			attrTable.setText(i+1, 1, ""+values[i]);
		}
		
		insttitle.setText("Instance : " + insTitle);
		mainPanel.add(insttitle);
		mainPanel.add(classtitle);
		mainPanel.add(attrTable);
		
		insttitle.setStyleName("instdesctitle");
		classtitle.setStyleName("instdesctitle");
		attrTable.setStyleName("instdesctable");
		attrTable.getRowFormatter().setStyleName(0, "instdescheader");
		attrTable.getCellFormatter().addStyleName(0, 0, "instdescnameColumn");
		attrTable.getCellFormatter().addStyleName(0, 1, "instdescvalueColumn");
		
		initWidget(mainPanel);
	}

	public void refresh(String insTitle, String insClass, String[] attributes, String[] values) {
		
		attrTable.removeAllRows();
		
		attrTable.setText(0, 0, "Attribute");
		attrTable.setText(0, 1, "Value");
		
		for(int i = 0; i<attributes.length; i++) {
			attrTable.setText(i+1, 0, attributes[i]);
			attrTable.setText(i+1, 1, values[i]);
		}
		
		insttitle.setText("Instance : " + insTitle);
		classtitle.setText("Class : " + insClass);
		mainPanel.add(insttitle);
		mainPanel.add(classtitle);
		mainPanel.add(attrTable);
		
		insttitle.setStyleName("instdesctitle");
		classtitle.setStyleName("instdesctitle");
		attrTable.setStyleName("instdesctable");
		attrTable.getRowFormatter().setStyleName(0, "instdescheader");
		attrTable.getCellFormatter().addStyleName(0, 0, "instdescnameColumn");
		attrTable.getCellFormatter().addStyleName(0, 1, "instdescvalueColumn");
	}
	
}
