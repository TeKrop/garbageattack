package com.tekrop.core;

import java.util.Stack;
import org.jsfml.graphics.RenderWindow;

/**
 * Classe représentant un écran donné
 * @author Valentin PORCHET
 *
 */
public abstract class Screen {
	
	/* Variables statiques */
	private static Stack<Screen> screens = new Stack<Screen>();
	//private static int config = 0;
		
	/**
	 * Méthode de rajout d'un nouveau écran sur la pile
	 * @param arg0 Screen à rajouter
	 */
	public static void push(Screen arg0){
		screens.push(arg0);
	}
	
	/**
	 * Méthode de retrait du dernier élément de la pile d'écrans
	 */
	public static void pop(){
		screens.pop();
	}
	
	/**
	 * Méthode permettant d'obtenir l'élément au top de la pile
	 * @return L'élément au top de la pile
	 */
	public static Screen last(){
		return screens.peek();
	}
	
	/**
	 * Méthode de gestion des évènements
	 * @param window Fenêtre de rendu
	 */
	public abstract void events(RenderWindow window);
	
	/**
	 * Méthode de mise à jour du rendu
	 * @param window Fenêtre de rendu
	 */
	public abstract void update(RenderWindow window);
	
	/**
	 * Méthode d'affichage du rendu
	 * @param window Fenêtre de rendu
	 */
	public abstract void draw(RenderWindow window);

}
