package com.tekrop.garbageattack;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;

import com.tekrop.core.ChronoManager;
import com.tekrop.core.Debugger;
import com.tekrop.core.FontManager;
import com.tekrop.core.MusicManager;
import com.tekrop.core.Screen;
import com.tekrop.core.SoundManager;
import com.tekrop.core.TextureManager;

/**
 * Classe contenant le code de lancement du jeu
 * @author Valentin PORCHET
 *
 */
public final class Main {

	/**
	 * Point d'entr�e du programme
	 * @param args arguments pass�s en param�tre au lancement du jeu
	 */
	public static void main(String[] args){
		// Gestion du debugger
		Debugger.init();
		Debugger.setDebuggerEnabled(true);
		Debugger.setWriteEnabled(true);
		
		// Initialisation des volumes
		SoundManager.setVolume(30);
		MusicManager.setVolume(30);
		
		// Cr�ation de la nouvelle fen�tre
		RenderWindow window = new RenderWindow();
		window.create(new VideoMode(800, 450), "Garbage Attack", 4);
		window.setVerticalSyncEnabled(true);
		
		// On rajoute un nouveau jeu
		Screen.push(new Intro());
	    
	    // Start the game loop
	    while (window.isOpen())
	    {
	    	// Gestion des �v�nements
	    	Screen.last().events(window);
	    	
	    	// Mise � jour des donn�es
	    	Screen.last().update(window);

	    	// Affichage
	    	window.clear();
	    	Screen.last().draw(window);
	    	window.display();
	    }
	    
	    // A la fin du programme, on clear les manager
	    TextureManager.clear();
	    MusicManager.clear();
	    SoundManager.clear();
	    FontManager.clear();
	    ChronoManager.clear();
	}
}
