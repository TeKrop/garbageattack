package com.tekrop.garbageattack;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import com.tekrop.core.FontManager;
import com.tekrop.core.TextureManager;

public class Overlay implements Drawable {
	
	private RectangleShape garbageBar = new RectangleShape(); // Barre des poubelles
	private RectangleShape backgroundScore = new RectangleShape();
	private Sprite garbageScoreSprite = new Sprite(); // Sprite pour la barre des poubelles	
	private Text scoreText = new Text(); // Score de la personne
	
	public Overlay(){
		// Initialisation de la barre des poubelles
		garbageBar.setSize(new Vector2f(50,350));
		garbageBar.setPosition(new Vector2f(725,50));
		garbageBar.setOutlineColor(Color.RED);
		garbageBar.setOutlineThickness(3);
		garbageBar.setFillColor(new Color(0,0,0,0));
		
		// Initialisation du background du score
		backgroundScore.setFillColor(Color.BLACK);
		backgroundScore.setSize(new Vector2f(150, 50));
		backgroundScore.setPosition(new Vector2f(325, 390));
		
		// Initialisation du sprite pour la texture des poubelles
		garbageScoreSprite.setTexture(TextureManager.load("garbageBar.png"));
		garbageScoreSprite.setPosition(new Vector2f(725,400));
		garbageScoreSprite.setTextureRect(new IntRect(0, 0, 100, 0));
		
		// Initialisation du texte du score
		scoreText.setFont(FontManager.load("scribish.ttf"));
		scoreText.setPosition(0, 390);
		scoreText.setCharacterSize(40);
		scoreText.setColor(new Color(0,188,19));				
		scoreText.setString("0");
		this.centerScore();
	}
	
	/**
	 * Méthode de centrage de texte
	 * @param text Texte à centrer
	 */
	private void centerScore() {
		// Variables à utiliser
		float width = 800.f;
		float textWidth = scoreText.getGlobalBounds().width;
		Vector2f textPos = scoreText.getPosition();
		// On retourne la nouvelle position
		scoreText.setPosition(new Vector2f(width/2.f - textWidth/2.f, textPos.y));
	}
	
	/**
	 * Méthode de mise à jour de l'overlay
	 * @param score valeur du score
	 * @param garbageScore valeur du score poubelle
	 */
	public void update(int score, int garbageScore){
		// Mise à jour du score
		scoreText.setString(String.valueOf(score));
		this.centerScore();
		
		// Mise à jour du score poubele
		garbageScoreSprite.setPosition(new Vector2f(725, 400-(35*garbageScore)));
		garbageScoreSprite.setTextureRect(new IntRect(0, 0, 50, 35*garbageScore));
	}
	
	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		arg0.draw(garbageBar);
		arg0.draw(garbageScoreSprite);
		
		arg0.draw(backgroundScore);
		arg0.draw(scoreText);
	}

}
