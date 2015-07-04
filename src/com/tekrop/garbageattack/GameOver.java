package com.tekrop.garbageattack;

import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;

import com.tekrop.core.FontManager;
import com.tekrop.core.MusicManager;
import com.tekrop.core.Screen;
import com.tekrop.core.TextureManager;

public class GameOver extends Screen {

	private Text score = new Text();
	private Sprite replayButton = new Sprite();
	private Sprite background = new Sprite();
	private Music music = new Music();
	
	public GameOver(int score){
		// Initialisation du background
		this.background.setTexture(TextureManager.load("background.png"));
		this.background.setPosition(new Vector2f(0,0));
		
		this.score.setFont(FontManager.load("scribish.ttf"));
		this.score.setCharacterSize(100);
		this.score.setColor(Color.RED);
		this.score.setString(String.valueOf(score));
		this.centerScore();
		
		this.replayButton.setTexture(TextureManager.load("replay.png"));
		this.replayButton.setPosition(150, 325);
		
		this.music = MusicManager.load("gameover.ogg");		
		this.music.play();
	}
	
	private void centerScore() {
		// Variables	
		Vector2f dim = new Vector2f(score.getGlobalBounds().width, score.getGlobalBounds().height);
		
		// On retourne la nouvelle position
		score.setPosition(new Vector2f(409 - dim.x/2.f, 188 - dim.y/2.f));
	}

	@Override
	public void events(RenderWindow window) {
		for(Event event : window.pollEvents()) {	        
            // Close window: exit
            if (event.type == Event.Type.CLOSED)
                window.close();
            if (event.type == Event.Type.MOUSE_BUTTON_PRESSED){
            	// Si la souris a hit le bouton rejouer
            	Vector2i mPos = Mouse.getPosition(window);
            	if (mPos.x >= 150 && mPos.x <= 650 && mPos.y >= 325 && mPos.y <= 425){
            		this.music.stop();
            		Screen.pop();
            		Screen.push(new Game());
            	}
            }
        }
	}

	@Override
	public void update(RenderWindow window) {
	}

	@Override
	public void draw(RenderWindow window) {
		// TODO Auto-generated method stub
		window.draw(this.background);
		window.draw(this.score);
		window.draw(this.replayButton);
	}

}
