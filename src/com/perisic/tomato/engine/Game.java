package com.perisic.tomato.engine;


import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game {

	BufferedImage image = null; 	
	int solution = -1;
	

	public Game(BufferedImage image, int solution) {
		super();
		this.image = image;
		this.solution = solution;
	}
	

	public BufferedImage getImage() {
		return image;
	}


	public int getSolution() {
		return solution;
	}
	
	
	
	

}
