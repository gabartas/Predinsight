package com.demo.predinsight.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExplServiceAsync {

	void getDatasetsNames(AsyncCallback<String[]> callback);
	
	void getDatasetsIds(AsyncCallback<int[]> callback);
	
	void getFlowsNames(AsyncCallback<String[]> callback);
	
	void getFlowsDesc(AsyncCallback<String[]> callback);
	
	void generateDesc(int[] flowsindex, int datasetindex, AsyncCallback<Informations> callback);
	
	void generateDesc_suppressatts(int[] flowsindex, int datasetindex, ArrayList<String> suppressedatts, AsyncCallback<Informations> callback);
	
	void trainAndSave(String flowname, AsyncCallback<Boolean> callback) throws Exception;
	
	void getinfs(AsyncCallback<AttributesInformations> callback);
	
	void predictOne(String[] attributes, double[] values, String param, AsyncCallback<Influence> callback);
	
}
