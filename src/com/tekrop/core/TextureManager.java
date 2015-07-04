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
	 * Structure principale contenant les textures charg�es
	 */
	private static Hashtable<String, Texture> textures = new Hashtable<String, Texture>();
	private static final String prefix = "/img/";
	
	/**
	 * Fonction priv�e permettant de charger une texture, renvoie une exception si probl�me
	 * @param path Chemin vers le fichier contenant la texture
	 * @return La texture charg�e
	 */
	private static Texture loadTexture(String path){
		Texture texture = new Texture(); // Instanciation d'une nouvelle texture
        
		try {
			// On stocke dans un input stream l'image que l'on veut charger
			// (la m�thode utilis�e permet de charger depuis le .jar final)
			InputStream inputStream = texture.getClass().getResourceAsStream(prefix+path);
			texture.loadFromStream(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture; // Retour de la texture			
	}
	
	/**
	 * M�thode de chargement d'une texture
	 * @param key Chemin de l'image
	 * @return Texture correspondant � l'image
	 */
	public static Texture load(String key){
		// Si l'image est d�j� stock�e, on la renvoie
		Texture value = textures.get(key);
		
		// Si l'image n'est pas stock�e, on la charge et stocke
		if (value == null){
			value = loadTexture(key);
			textures.put(key, value);
		}
		
		// On la retourne
		return value;
	}

	/**
	 * M�thode appel�e � la fin du programme pour vider la m�moire
	 */
	public static void clear(){
		textures.clear();
	}
}
