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
	 * Structure principale contenant les musiques chargées
	 */
	private static Hashtable<String, Music> musics = new Hashtable<String, Music>();
	private static final String prefix = "/sounds/";
	private static int volume = 100;

	/**
	 * Fonction privée permettant de charger une musique, renvoie une exception si problème
	 * @param path Chemin vers le fichier contenant la musique
	 * @return La musique chargée
	 */
	private static Music loadMusic(String path){
		Music music = new Music(); // Instanciation d'une nouvelle musique		        
		try {
			InputStream inputStream = music.getClass().getResourceAsStream(prefix+path);
			music.openFromStream(inputStream);
		} catch (IOException e) {
			Debugger.log("Erreur : le fichier de musique n'a pas pu être chargé");
			e.printStackTrace();
		}
		// On active la boucle d'écoute
		music.setLoop(true);
		return music; // Retour de la musique				
	}
	
	/**
	 * Méthode de chargement d'une musique
	 * @param key Chemin de la musique
	 * @return Music correspondant à la musique
	 */
	public static Music load(String key){
		// Si la musique est déjà stockée, on la renvoie
		Music value = musics.get(key);
		
		// Si la musique n'est pas stockée, on la charge et stocke
		if (value == null){
			value = loadMusic(key);
			value.setVolume(volume);
			musics.put(key, value);
		}
		
		// On la retourne
		return value;
	}
	
	/**
	 * Méthode permettant de changer le volume de toutes les musiques
	 * @param volume Volume auquel mettre les musiques
	 */
	public static void setVolume(int volume){
		// On met à jour le volume du manager
		MusicManager.volume = volume;
		
		// On crée un itérateur pour pouvoir naviguer dans le tableau des musiques
		Iterator<Map.Entry<String, Music>> it = musics.entrySet().iterator();
		
		/* On itère le hashtable, on met le volume voulu */
		while (it.hasNext()){
			it.next().getValue().setVolume(volume);
		}
	}
	
	/**
	 * Met toutes les musiques à un volume de zéro
	 */
	public static void muteAll(){
		MusicManager.setVolume(0);
	}

	/**
	 * Méthode appelée à la fin du programme pour vider la mémoire
	 */
	public static void clear(){
		musics.clear();
	}
}
