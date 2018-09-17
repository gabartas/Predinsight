package com.demo.predinsight.server;

import java.io.File;

import com.demo.predinsight.client.Influence;

public class Test {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		explanation expl;
		Influence inf;
		double[] truc;
		
		int taskid = 3832;
		int[] flows = new int[1];
		
		flows[0] = 7791;
		
		System.out.println(taskid);
		System.out.println(flows[0]);
		
		File workDir = new File("D:\\Work\\whatev");
		Session sess = new Session("test",flows,taskid,workDir);
		
		//expl = explanation.generate_explanations_train(sess);
		expl = explanation.generate_explanations_stat_quick(sess);
		
		int[] pres = explanation.select_presentation(expl.infs);
		
		for(int i=0; i<pres.length; i++) {
			System.out.println(pres[i]);
		}
	}

}
