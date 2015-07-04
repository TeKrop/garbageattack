package com.tekrop.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.jsfml.audio.Music;

/**
 * Classe de gestion des musiques
 * @author Valentin PORCHET
 *
 */
public final class MusicManager {
	
	/**
	 * Structure principale contenant les musiques charg�es
	 */
	private static Hashtable<String, Music> musics = new Hashtable<String, Music>();
	private static final String prefix = "/sounds/";
	private static int volume = 100;

	/**
	 * Fonction priv�e permettant de charger une musique, renvoie une exception si probl�me
	 * @param path Chemin vers le fichier contenant la musique
	 * @return La musique charg�e
	 */
	private static Music loadMusic(String path){
		Music music = new Music(); // Instanciation d'une nouvelle musique		        
		try {
			InputStream inputStream = music.getClass().getResourceAsStream(prefix+path);
			music.openFromStream(inputStream);
		} catch (IOException e) {
			Debugger.log("Erreur : le fichier de musique n'a pas pu �tre charg�");
			e.printStackTrace();
		}
		// On active la boucle d'�coute
		music.setLoop(true);
		return music; // Retour de la musique				
	}
	
	/**
	 * M�thode de chargement d'une musique
	 * @param key Chemin de la musique
	 * @return Music correspondant � la musique
	 */
	public static Music load(String key){
		// Si la musique est d�j� stock�e, on la renvoie
		Music value = musics.get(key);
		
		// Si la musique n'est pas stock�e, on la charge et stocke
		if (value == null){
			value = loadMusic(key);
			value.setVolume(volume);
			musics.put(key, value);
		}
		
		// On la retourne
		return value;
	}
	
	/**
	 * M�thode permettant de changer le volume de toutes les musiques
	 * @param volume Volume auquel mettre les musiques
	 */
	public static void setVolume(int volume){
		// On met � jour le volume du manager
		MusicManager.volume = volume;
		
		// On cr�e un it�rateur pour pouvoir naviguer dans le tableau des musiques
		Iterator<Map.Entry<String, Music>> it = musics.entrySet().iterator();
		
		/* On it�re le hashtable, on met le volume voulu */
		while (it.hasNext()){
			it.next().getValue().setVolume(volume);
		}
	}
	
	/**
	 * Met toutes les musiques � un volume de z�ro
	 */
	public static void muteAll(){
		MusicManager.setVolume(0);
	}

	/**
	 * M�thode appel�e � la fin du programme pour vider la m�moire
	 */
	public static void clear(){
		musics.clear();
	}
}
