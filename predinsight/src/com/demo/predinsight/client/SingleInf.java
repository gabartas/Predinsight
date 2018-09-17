package com.demo.predinsight.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.Composite;

public class SingleInf extends Composite{
	
	static final int canvasHeight = 25;
    static final int canvasWidth = 250;
	
	public static Canvas createInf (String name, double value) {
		Canvas canva = Canvas.createIfSupported();
		if(canva == null) {
            return null;
		}
		
		canva.setWidth(canvasWidth+"px");
		canva.setHeight(canvasHeight+"px");
		
		canva.setCoordinateSpaceWidth(canvasWidth);
		canva.setCoordinateSpaceHeight(canvasHeight);
		
		
		
		Context2d context = canva.getContext2d();
		
		context.setTextAlign("left");
		
		if(value<-0.001) {
			context.setFillStyle(CssColor.make("rgba(250,100,100,1"));
		}
		else if(value>0.001){
			context.setFillStyle(CssColor.make("rgba(100,200,100,1"));
		}
		else {
			context.setFillStyle(CssColor.make("rgba(220,220,220,1"));
		}
		
		if(Math.abs(value) > 0.001) {
			context.fillRect(0,0,250*Math.abs(value),50);
			context.setFillStyle(CssColor.make("rgba(220,220,220,1"));
			context.fillRect(250*Math.abs(value),0,250*(1-Math.abs(value)),50);
		}
		else {
			context.fillRect(0,0,250,50);
		}
		
		
		context.setFillStyle(CssColor.make("rgba(0,0,0,1"));
		context.setFont("15px Arial");
		context.fillText(name, 0, 16);
		context.fillText((Math.round(value*100)) + "%", 200, 16);
		
		return canva;
	}
	
}
