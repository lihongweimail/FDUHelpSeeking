package cn.edu.fudan.se.helpseeking.util;


/*
     Copyright 2014 zhao xue jiao
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import processing.core.*;
import wordcram.*;  //导入package“wordcram”中的所有类


public class TagCloud extends PApplet {
	
	WordCram wordcram;
	PImage cachedImage; // Cache the rendered wordcram, so drawing is fast
	Word lastClickedWord; // The word that was under the user's last click
	public void setup() {
		// destination.image.getGraphics():
		// P2D -> sun.awt.image.ToolkitImage, JAVA2D -> java.awt.image.BufferedImage.

		// parent.getGraphics():
		// P2D -> sun.java2d.SunGraphics2D, JAVA2D -> same thing.

		// P2D can't draw to destination.image.getGraphics(). Interesting.

		size(500, 350); // (int)random(300, 800)); //1200, 675); //1600, 900);
		smooth();
		colorMode(RGB);
		
		try {
			initWordCram();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		wordcram.drawAll();
		  // Save the image of the wordcram
		  cachedImage = get();
		  
		  // Set up styles for when we draw stuff to the screen (later)
		  textFont(createFont("sans", 70));
		  textAlign(CENTER, CENTER);
		//frameRate(1);
	}
//调用WordCram实现文字云统计工作	
	private void initWordCram() throws IOException {
		background(30);//gray - specifies a value between white and black
		
     	BufferedReader LdaOutcomeReader = null;
     	Word[] wordFromLda = new Word[20];
     	LdaOutcomeReader = new BufferedReader(new FileReader(new File(textFilePath())));
     	System.out.println(new File(textFilePath()).getAbsolutePath());
     	char[] tmp = new char[1024];
     	LdaOutcomeReader.read(tmp);
     	String LdaOutcome = String.valueOf(tmp);     	
     	String[] splitOutcome = LdaOutcome.split("\\s+");

     	for (int i=0;i<20;i++)
     	   { 
     		System.out.println(splitOutcome[2*i]);
     		System.out.println(splitOutcome[2*i+1]);
    		 wordFromLda[i] = new Word(splitOutcome[2*i], Float.valueOf(splitOutcome[2*i+1]));
    		 if(i<5)
    		     {
    		        wordFromLda[i].setColor(color(255,255,255));
    		        wordFromLda[i].setPlace(40, 40);
    		     }
    		 else if(i<10)
    		    {
    			 
    			    wordFromLda[i].setColor(color(0,255,0)); 
    			    wordFromLda[i].setPlace(200, 40);
    		    }
    		 else if(i<15)
		        {
		           wordFromLda[i].setColor(color(0,0,255));
		           wordFromLda[i].setPlace(0, 150);
		        }
		     else
		        {
			 
			       wordFromLda[i].setColor(color(255,255,0)); 
			       wordFromLda[i].setPlace(200, 150);
		        }	 
     	   }  	 		
		wordcram = new WordCram(this)    //创建一个WordCram类的对象	
		            .fromWords(wordFromLda) 
					//.fromTextFile(textFilePath())//将来自textFilePath方法的text文件路径传递给wordcram的fromTextFile方法
		           //   .fromWebPage("http://www.sina.com.cn/")
		            //.upperCase()
					//.excludeNumbers()
		            .maxNumberOfWordsToDraw(50)
					.withFont("Molengo") //randomFont())
					.withColorer(Colorers.twoHuesRandomSats(this))
					.angledAt(radians(0))
					.withPlacer(Placers.horizLine())
					//.withPlacer(Placers.centerClump())	
					.withSizer(Sizers.byWeight(0, 30))
					;
	}
//draw实现将统计好的文字运进行绘画	
	public void keyPressed() {
		if (keyCode == ' ') {
			saveFrame("wordcram-##.png");
		}
	}
//组成一个文字源的地址	
	private String textFilePath() {

	 	//String path = "C:\\Users\\zxj\\workspace\\TagCloud_ZXJ\\Data\\lda_100.txt";
	 	String path = "Y:\\Downloads\\TagCloud\\TagCloud_1.1\\Data\\lda_100.txt";
		return path;		
	}
		
	public void draw() {
		  // First, wipe out the last frame: re-draw the cached image
		  image(cachedImage, 0, 0);
		  
		  // If the user's last click was on a word, render it big and blue:
		  if (lastClickedWord != null) {
		    noStroke();
		    fill(255, 190);
		    rect(0, height/2 - textAscent()/2, width, textAscent() + textDescent());

		    fill(30, 144, 13, 150);
		    text(lastClickedWord.word, width/2, height/2);
		    println(lastClickedWord);
		  }
		}

	public void mouseClicked() {
		  lastClickedWord = wordcram.getWordAt(mouseX, mouseY); 
		  loop();
		  
		}

	
}