package com.tekrop.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.jsfml.graphics.Texture;

/**
 * Classe de gestion des textures
 * @author Valentin PORCHET
 *
 */
public final class TextureManager {
	
	/**
	 * Structure principale contenant les textures chargées
	 */
	private static Hashtable<String, Texture> textures = new Hashtable<String, Texture>();
	private static final String prefix = "/img/";
	
	/**
	 * Fonction privée permettant de charger une texture, renvoie une exception si problème
	 * @param path Chemin vers le fichier contenant la texture
	 * @return La texture chargée
	 */
	private static Texture loadTexture(String path){
		Texture texture = new Texture(); // Instanciation d'une nouvelle texture
        
		try {
			// On stocke dans un input stream l'image que l'on veut charger
			// (la méthode utilisée permet de charger depuis le .jar final)
			InputStream inputStream = texture.getClass().getResourceAsStream(prefix+path);
			texture.loadFromStream(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture; // Retour de la texture			
	}
	
	/**
	 * Méthode de chargement d'une texture
	 * @param key Chemin de l'image
	 * @return Texture correspondant à l'image
	 */
	public static Texture load(String key){
		// Si l'image est déjà stockée, on la renvoie
		Texture value = textures.get(key);
		
		// Si l'image n'est pas stockée, on la charge et stocke
		if (value == null){
			value = loadTexture(key);
			textures.put(key, value);
		}
		
		// On la retourne
		return value;
	}

	/**
	 * Méthode appelée à la fin du programme pour vider la mémoire
	 */
	public static void clear(){
		textures.clear();
	}
}
