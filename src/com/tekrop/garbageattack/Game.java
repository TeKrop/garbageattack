package com.tekrop.garbageattack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;

import com.tekrop.core.Chrono;
import com.tekrop.core.ChronoManager;
import com.tekrop.core.MusicManager;
import com.tekrop.core.Screen;
import com.tekrop.core.SoundManager;
import com.tekrop.core.TextureManager;

public class Game extends Screen {
	
	private final int GARBAGE_MAX = 10;
	
	private int score = 0;
	private int garbageScore = 0;
	private ArrayList<Sprite> garbages = new ArrayList<Sprite>();
	private Overlay overlay = new Overlay();
	private Sprite background = new Sprite();
	
	private Sprite mathieu = new Sprite();
	private int currentFrame = 0;
	private boolean progressPos = true; // Progression de l'image en positif
	
	private Chrono gameClock;
	private Chrono mathieuClock;
	private Chrono garbageClock;
	private float garbageTimer = 1.f;
	private float garbageSpeed = 350.f;
	private Random rand = new Random();
	
	
	private Music music;
	private Sound trashFall;

	public Game(){
		// Initialisation du background
		this.background.setTexture(TextureManager.load("background.png"));
		this.background.setPosition(new Vector2f(0,0));
		
		// Initialisation de mathieu
		this.mathieu.setTexture(TextureManager.load("mathieu.png"));
		this.mathieu.setPosition(new Vector2f(263,86));
		this.mathieu.setTextureRect(new IntRect(0,0,292,243));
		
		// Initialisation du chrono
		this.gameClock = ChronoManager.load("gameClock");
		this.gameClock.restart();
		
		this.mathieuClock = ChronoManager.load("mathieuClock");
		this.mathieuClock.restart();
		
		this.garbageClock = ChronoManager.load("garbageClock");
		this.garbageClock.restart();
		
		this.music = MusicManager.load("music.ogg");		
		this.music.play();
		
		this.trashFall = SoundManager.load("trashfall.ogg");
	}
	
	@Override
	public void events(RenderWindow window) {
		for(Event event : window.pollEvents()) {	        
            // Close window: exit
            if (event.type == Event.Type.CLOSED)
                window.close();
            if (event.type == Event.Type.MOUSE_BUTTON_PRESSED){
            	// On va chercher si la souris est sur une poubelle
            	// On va dans le désordre pour traiter les poubelles à l'avant
            	
            	for (int i=garbages.size()-1; i >= 0; i--){
            		// Si la souris est sur une poubelle
            		if (mouseOn(garbages.get(i), window)){
            			this.score++;
            			garbages.remove(i);
            			break;
            		}
            	}
            }
        }
	}

	private boolean mouseOn(Sprite g, RenderWindow window) {
		Vector2i mousePos = Mouse.getPosition(window);		
		
		return ((mousePos.x >= g.getPosition().x)&&
				(mousePos.x <= g.getPosition().x + g.getTexture().getSize().x)&&
				(mousePos.y >= g.getPosition().y)&&
				(mousePos.y <= g.getPosition().y + g.getTexture().getSize().y));
	}

	@Override
	public void update(RenderWindow window) {
		float elapsed = this.gameClock.restart();
		
		// TODO Auto-generated method stub
		this.overlay.update(score, garbageScore);
		
		// Mise à jour de mathieu
		if (this.mathieuClock.getElapsedTime() > 0.045){

			// En fonction de la frame, on va dans un sens ou un autre
			if (this.currentFrame == 9){ this.progressPos = false; } 
			else if (this.currentFrame == 0){ this.progressPos = true; }
			
			// On va mettre le nouveau numéro de frame
			if (this.progressPos){ this.currentFrame++; }
			else { this.currentFrame--; }
			
			// On update la texture
			this.mathieu.setTextureRect(new IntRect(this.currentFrame * 292,0,292,243));
			this.mathieuClock.restart();
		}
		
		// Ajout de nouvelle poubelle si timer
		if (this.garbageClock.getElapsedTime() > this.garbageTimer){
			// Poubelle aléatoire
			int typeGarbage = rand.nextInt(4) + 1;
			int xGarbage = rand.nextInt(550);
			
			// Création et insertion de la poubelle
			Sprite newGarbage = new Sprite();
			newGarbage.setTexture(TextureManager.load("bag_"+String.valueOf(typeGarbage)+".png"));
			newGarbage.setPosition(new Vector2f(xGarbage, -200));
			this.garbages.add(newGarbage);
			
			// Limite max de temps entre chaque poubelles
			if (this.garbageTimer > 0.2){
				this.garbageTimer *= 0.98;
			}
			
			if (this.garbageSpeed < 600){
				this.garbageSpeed *= 1.02;
			}
			
			this.garbageClock.restart();
		}
		
		// On met à jour les positions des poubelles
		for (Sprite g : garbages){
			g.move(0, garbageSpeed*elapsed);
		}
		
		// On va supprimer les poubelles en dehors, et augmenter
		// le score poubelle si y en a une qui tombe
		Iterator<Sprite> it = garbages.iterator();
		while (it.hasNext()){
			Sprite s = it.next();
			if (s.getPosition().y > 450){
				this.garbageScore++;
				this.trashFall.play();
				it.remove();
				if (this.garbageScore == GARBAGE_MAX){
					Screen.pop();
					this.music.stop();
					Screen.push(new GameOver(this.score));
				}
			}
		}
	}

	@Override
	public void draw(RenderWindow window) {
		// TODO Auto-generated method stub
		window.draw(this.background);
		window.draw(this.mathieu);
		window.draw(this.overlay);
		
		for (Sprite s : garbages){
			window.draw(s);
		}
	}

}
