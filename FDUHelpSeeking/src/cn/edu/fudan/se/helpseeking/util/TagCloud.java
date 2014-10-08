package cn.edu.fudan.se.helpseeking.util;

/*
     Copyright 2014 zhao xue jiao
 */

import java.util.*;

import processing.core.*;
import wordcram.*;  //����package��wordcram���е�������
import wordcram.text.*;

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

		size(500, 400); // (int)random(300, 800)); //1200, 675); //1600, 900);
		smooth();
		colorMode(HSB);
		initWordCram();//
		wordcram.drawAll();
	
		  // Save the image of the wordcram
		  cachedImage = get();
		  
		  // Set up styles for when we draw stuff to the screen (later)
		  textFont(createFont("sans", 70));
		  textAlign(CENTER, CENTER);
		//frameRate(1);
	}
//	�˷�����������û��ʹ�ù�(����һ������ֵΪPFont��ķ���)
	private PFont randomFont() {
		String[] fonts = PFont.list();
		String noGoodFontNames = "Dingbats|Standard Symbols L";
		String blockFontNames = "OpenSymbol|Mallige Bold|Mallige Normal|Lohit Bengali|Lohit Punjabi|Webdings";
		Set<String> noGoodFonts = new HashSet<String>(Arrays.asList((noGoodFontNames+"|"+blockFontNames).split("|")));
		String fontName;
		do {
			fontName = fonts[(int)random(fonts.length)];
		} while (fontName == null || noGoodFonts.contains(fontName));
		System.out.println(fontName);
		return createFont(fontName, 1);
		//return createFont("Molengo", 1);
	}
//����WordCramʵ��������ͳ�ƹ���	
	private void initWordCram() {
		background(30);//gray - specifies a value between white and black

		wordcram = new WordCram(this)    //����һ��WordCram��Ķ���
					//.fromTextFile(textFilePath())//������textFilePath������text�ļ�·�����ݸ�wordcram��fromTextFile����
		              .fromWebPage("http://docs.oracle.com/javase/tutorial/networking/sockets/")
		            //.upperCase()
					//.excludeNumbers()
					.withFont("Molengo") //randomFont())
					.withColorer(Colorers.twoHuesRandomSats(this))
					.angledAt(radians(-20))
					//.withPlacer(Placers.horizLine())
					.withPlacer(
							       new WordPlacer()
								 {
									private java.util.Random r = new java.util.Random();
			
									public PVector place(Word word, int wordIndex,
											int wordsCount, int wordImageWidth,
											int wordImageHeight, int fieldWidth, int fieldHeight) {
			
										float x = (float) (r.nextGaussian() * (1 - word.weight)) / 2;
										float y = (float) (r.nextGaussian() * (1 - word.weight)) / 2;
										//x *= Math.abs(x);
										y *= Math.abs(y);
										x = PApplet.map(x, -2, 2, 0, fieldWidth - wordImageWidth);//x������ֵ  -2 2��x��ȡֵ��Χ  0���ı䷶Χ���ȡֵ��Χ
										y = PApplet.map(y, -2, 2, 0, fieldHeight - wordImageHeight);
										
										y = PApplet.map((float)wordIndex, wordsCount, 0, 0, fieldHeight - wordImageHeight);
										y = PApplet.map((float)word.weight, 0, 1, 0, fieldHeight - wordImageHeight);
										
										x = PApplet.map((float)word.weight, 1, 0, 0, fieldWidth - wordImageWidth);
										//y = (float)(r.nextGaussian() * (1-word.weight)) / 2;
										//y *= Math.abs(y);
										//y = PApplet.map(y, -2, 2, 0, fieldHeight - wordImageHeight);
										
										int firstLetter = word.word.toCharArray()[0];
										x = PApplet.map(firstLetter, 97, 122, 0, fieldWidth - wordImageWidth);
										
										return new PVector(x, y);
									}
								 }
					          )
					//.withPlacer(Placers.wave())
					.withPlacer(Placers.centerClump())	
					.withSizer(Sizers.byWeight(0, 70))
					
					//.withNudger(new PlottingWordNudger(this, new SpiralWordNudger()))
					//.withNudger(new RandomWordNudger())
					
					//.printSkippedWords()
					;
	}
//drawʵ�ֽ�ͳ�ƺõ������˽��л滭	
/*	public void draw() {     // Called directly after setup()
		//fill(55);
		//rect(0, 0, width, height);
		
		boolean allAtOnce = true;
		if (allAtOnce) {
			wordcram.drawAll();
			println("Done");
			save("wordcram.png");
			noLoop();
		}
		else {
			int wordsPerFrame = 1;
			while (wordcram.hasMore() && wordsPerFrame-- > 0) {  //hasmMore()��������һ����������������������Ƿ���
				wordcram.drawNext();        //ÿ�λ�һ��word��drawNext��������һ��word
			}
			if (!wordcram.hasMore()) {
				println("Done");
				save("wordcram.png");
				noLoop();
			}
		}
	}*/
	
	/*
	public void mouseMoved() {
		Word word = wordcram.getWordAt(mouseX, mouseY);
		if (word != null) {
			System.out.println(round(mouseX) + "," + round(mouseY) + " -> " + word.word);
		}
	}
	*/
	
/*	public void mouseClicked() {
		initWordCram();
		loop();
	}*/
	
	public void keyPressed() {
		if (keyCode == ' ') {
			saveFrame("wordcram-##.png");
		}
	}
//����һ������ֵΪWord[]����	
//	private Word[] loadWords() {
//		String[] text = loadStrings(textFilePath());// must be located in the sketch's "data" directory/folder.
//		return new TextSplitter().split(text);
//	}
//���һ������Դ�ĵ�ַ	
/*	private String textFilePath() {
		boolean linux = true;
		String projDir = linux ? "/home/dan/projects/" : "c:/dan/";
		String path = projDir + "eclipse/wordcram/trunk/ideExample/tao-te-ching.txt";
		return path;		
	}*/
	
	private Word[] alphabet() {
		Word[] w = new Word[26];
		for (int i = 0; i < w.length; i++) {
			w[i] = new Word(new String(new char[]{(char)(i+65)}), 26-i);
		}
		return w;
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
	
	
	
//	void setup() {
//		  size(700, 400);
//		  background(255);
//		  
//		  // Make the wordcram
//		  wc = new WordCram(this).fromWebPage("http://wikipedia.org");
//		  wc.drawAll();
//		  
//		  // Save the image of the wordcram
//		  cachedImage = get();
//		  
//		  // Set up styles for when we draw stuff to the screen (later)
//		  textFont(createFont("sans", 150));
//		  textAlign(CENTER, CENTER);
//		}

	
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "TagCloud" });
	  }
	
	
	
	
}