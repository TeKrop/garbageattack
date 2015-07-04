package com.tekrop.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.jsfml.graphics.Font;

/**
 * Classe de gestion des polices
 * @author Valentin PORCHET
 *
 */
public final class FontManager {
	
	/**
	 * Structure principale contenant les polices chargées
	 */
	private static Hashtable<String, Font> fonts = new Hashtable<String, Font>();
	private static final String prefix = "/fonts/";
	
	/**
	 * Fonction privée permettant de charger une police, renvoie une exception si problème
	 * @param path Chemin vers le fichier contenant la police
	 * @return La police chargée
	 */
	private static Font loadFont(String path){
		Font font = new Font();
		try {
			// On stocke dans un input stream la police que l'on veut charger
			// (la méthode utilisée permet de charger depuis le .jar final)
			InputStream inputStream = font.getClass().getResourceAsStream(prefix+path);
		    font.loadFromStream(inputStream);
		} catch(IOException ex) {
		    ex.printStackTrace();
		}
		return font;
	}
	
	/**
	 * Méthode de chargement d'une police
	 * @param key Chemin de la police
	 * @return Texture correspondant à la police
	 */
	public static Font load(String key){
		// Si la police est déjà stockée, on la renvoie
		Font value = fonts.get(key);
		
		// Si la police n'est pas stockée, on la charge et stocke
		if (value == null){
			value = loadFont(key);
			fonts.put(key, value);
		}
		
		// On la retourne
		return value;
	}
	
	/**
	 * Méthode appelée à la fin du programme pour vider la mémoire
	 */
	public static void clear(){
		fonts.clear();
	}
}
