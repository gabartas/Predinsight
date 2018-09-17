package com.demo.predinsight.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.demo.predinsight.client.Influence;

public class Explanation_comparison {
	
	private static int[] flowsId = {8508,8509,8510}; //Decision tree, perceptron, bayes
	private static int irisTaskId = 59;
	private static String[] flowsnames = {"decision_tree","perceptron","bayes"};
	
	public static void write_to_csv(double[][] data,  String filename) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		String line = "";
		for(int i=0; i<data.length; i++) {
			line = String.valueOf(data[i][0]);
			for(int j=0; j<data[0].length; j++) {
				line += "," + data[i][j];
			}
			writer.println(line);
		}
		writer.close();
	}
	
	public static void write_to_csv(explanation expl, String filename) throws FileNotFoundException, UnsupportedEncodingException {
		String line = "";
		PrintWriter writer;
		Influence[][] infs = expl.infs;
		double[] current;		
		
		for(int f=0; f<3; f++) {
			writer = new PrintWriter(filename + "_" + flowsnames[f] + ".csv", "UTF-8");
			
			for(int i=0; i<infs[f].length; i++) {
				
				current = infs[f][i].getInf();
				line = String.valueOf(current[0]);
				
				for(int j=1; j<current.length; j++) {
					line += "," + current[j];
				}
				
				line += "," + infs[f][i].getPred();
				writer.println(line);
			}
			writer.close();
		}
	}

	public static void write_to_csv_att(explanation expl, String filename) throws FileNotFoundException, UnsupportedEncodingException {
		String line = "";
		PrintWriter writer;
		Influence[][] infs = expl.infs;
		double[] current;
		String[] attnames= infs[0][0].getAtt();
		String attString = attnames[0];
		
		
		for(int i=1; i<attnames.length; i++) {
			attString += ";" + attnames[i];
		}
		
		for(int f=0; f<3; f++) {
			writer = new PrintWriter(filename + "_" + flowsnames[f] + ".csv", "UTF-8");
			writer.println(attString + "; ; ;" + attString);
			for(int i=0; i<infs[f].length; i++) {
				
				current = infs[f][i].getInf();
				line = String.valueOf(current[0]);
				
				for(int j=1; j<current.length; j++) {
					line += "," + current[j];
				}
				
				line += "," + infs[f][i].getPred() + ", ";
				
				current = infs[f][i].getInstance();
				for(int j=0; j<current.length; j++) {
					line += "," + current[j];
				}
				
				writer.println(line);
			}
			writer.close();
		}
	}
	
	public static void write_to_csv_att_abs(explanation expl, String filename) throws FileNotFoundException, UnsupportedEncodingException {
		String line = "";
		PrintWriter writer;
		Influence[][] infs = expl.infs;
		double[] current;
		String[] attnames= infs[0][0].getAtt();
		String attString = attnames[0];
		
		
		for(int i=1; i<attnames.length; i++) {
			attString += ";" + attnames[i];
		}
		attString += ";class";
		
		for(int f=0; f<3; f++) {
			writer = new PrintWriter(filename + "_" + flowsnames[f] + ".csv", "UTF-8");
			writer.println(attString + "; ;" + attString + "; ;" + attString);
			for(int i=0; i<infs[f].length; i++) {
				
				current = infs[f][i].getInf();
				line = String.valueOf(current[0]);
				
				for(int j=1; j<current.length; j++) {
					line += ";" + current[j];
				}
				
				line += ";" + infs[f][i].getPred() + "; ";

				current = infs[f][i].getRawinf();
				for(int j=0; j<current.length; j++) {
					line += ";" + current[j];
				}
				
				line += ";" + infs[f][i].getPred() + "; ";
				
				current = infs[f][i].getInstance();
				for(int j=0; j<current.length; j++) {
					line += ";" + current[j];
				}
				
				writer.println(line);
			}
			writer.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		explanation expl_stat;
		explanation expl_train;
		explanation[] expl;
		
		File workDir = new File("F:\\Work\\whatev");
		Session sess = new Session("test",flowsId,irisTaskId,workDir);
		
		expl = explanation.generate_explanations_train_stat_quick(sess);
		expl_stat = expl[0];
		expl_train = expl[1];
		
		
		write_to_csv_att_abs(expl_stat,"F:\\Work\\results\\expl_stat");
		write_to_csv_att_abs(expl_train,"F:\\Work\\results\\expl_train");
	}

}
