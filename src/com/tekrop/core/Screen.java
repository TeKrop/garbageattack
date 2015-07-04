package com.tekrop.core;

import java.util.Stack;
import org.jsfml.graphics.RenderWindow;

/**
 * Classe repr�sentant un �cran donn�
 * @author Valentin PORCHET
 *
 */
public abstract class Screen {
	
	/* Variables statiques */
	private static Stack<Screen> screens = new Stack<Screen>();
	//private static int config = 0;
		
	/**
	 * M�thode de rajout d'un nouveau �cran sur la pile
	 * @param arg0 Screen � rajouter
	 */
	public static void push(Screen arg0){
		screens.push(arg0);
	}
	
	/**
	 * M�thode de retrait du dernier �l�ment de la pile d'�crans
	 */
	public static void pop(){
		screens.pop();
	}
	
	/**
	 * M�thode permettant d'obtenir l'�l�ment au top de la pile
	 * @return L'�l�ment au top de la pile
	 */
	public static Screen last(){
		return screens.peek();
	}
	
	/**
	 * M�thode de gestion des �v�nements
	 * @param window Fen�tre de rendu
	 */
	public abstract void events(RenderWindow window);
	
	/**
	 * M�thode de mise � jour du rendu
	 * @param window Fen�tre de rendu
	 */
	public abstract void update(RenderWindow window);
	
	/**
	 * M�thode d'affichage du rendu
	 * @param window Fen�tre de rendu
	 */
	public abstract void draw(RenderWindow window);

}
