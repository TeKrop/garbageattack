package com.tekrop.garbageattack;

import org.jsfml.audio.Music;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;

import com.tekrop.core.MusicManager;
import com.tekrop.core.Screen;
import com.tekrop.core.TextureManager;

public class Intro extends Screen {

	private Sprite background = new Sprite();
	private Music music = new Music();
	
	public Intro(){
		// Initialisation du background
		this.background.setTexture(TextureManager.load("intro.png"));
		this.background.setPosition(new Vector2f(0,0));
		
		this.music = MusicManager.load("intro.ogg");		
		this.music.play();
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
            	if (mPos.x >= 241 && mPos.x <= 556 && mPos.y >= 345 && mPos.y <= 444){
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
	}

}

