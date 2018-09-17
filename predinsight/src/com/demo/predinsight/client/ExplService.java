package com.demo.predinsight.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("explanation")

public interface ExplService extends RemoteService {

	String[] getDatasetsNames();

	int[] getDatasetsIds();
	
	String[] getFlowsNames();
	
	String[] getFlowsDesc();
	
	Informations generateDesc(int[] flowsindex, int datasetindex);
	
	Informations generateDesc_suppressatts(int[] flowsindex, int datasetindex, ArrayList<String> suppressedatts);
	
	Boolean trainAndSave(String flowname) throws Exception;
	
	AttributesInformations getinfs();
	
	Influence predictOne(String[] attributes, double[] values, String param);
	
}
