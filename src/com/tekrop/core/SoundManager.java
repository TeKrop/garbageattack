package com.tekrop.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;

/**
 * Classe de gestion des musiques
 * @author Valentin PORCHET
 *
 */
public final class SoundManager {
	
	/**
	 * Structure principale contenant les sons chargés
	 */
	private static Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();
	private static Hashtable<String, SoundBuffer> soundBuffers = new Hashtable<String, SoundBuffer>();
	private static final String prefix = "/sounds/";
	private static int volume = 100;

	/**
	 * Fonction privée permettant de charger un son, renvoie une exception si problème
	 * @param path Chemin vers le fichier contenant le son
	 * @return Le son chargé
	 */
	private static Sound loadSound(String path){
		SoundBuffer soundBuffer = new SoundBuffer();
		InputStream inputStream = soundBuffer.getClass().getResourceAsStream(prefix+path);
		try {			
			soundBuffer.loadFromStream(inputStream);
		} catch (IOException e) {
			Debugger.log("Erreur : le fichier de musique n'a pas pu être chargé");
			e.printStackTrace();
		}
		soundBuffers.put(path, soundBuffer); // On rajoute le buffer
		
		Sound sound = new Sound(); // Instanciation d'un nouveau son		
		sound.setBuffer(soundBuffers.get(path)); // On lui associe le buffer
		
		soundBuffers.put(path, soundBuffer); // On rajoute le buffer
		return sound; // Retour du son
	}
	
	/**
	 * Méthode de chargement d'un son
	 * @param key Chemin du son
	 * @return Sound correspondant au son
	 */
	public static Sound load(String key){
		// Si l'image est déjà stockée, on la renvoie
		Sound value = sounds.get(key);
		
		// Si l'image n'est pas stockée, on la charge et stocke
		if (value == null){
			value = loadSound(key);
			value.setVolume(volume);
			sounds.put(key, value);
		}
		
		// On la retourne
		return value;
	}
	
	/**
	 * Méthode permettant de changer le volume de tous les sons
	 * @param volume Volume auquel mettre les sons
	 */
	public static void setVolume(int volume){
		// On met à jour le volume du manager
		SoundManager.volume = volume;
		
		// On crée un itérateur pour pouvoir naviguer dans le tableau des sons
		Iterator<Map.Entry<String, Sound>> it = sounds.entrySet().iterator();
		
		/* On itère le hashtable, on met le volume voulu */
		while (it.hasNext()){
			it.next().getValue().setVolume(volume);
		}
	}
	
	/**
	 * Met toutes les musiques à un volume de zéro
	 */
	public static void muteAll(){
		SoundManager.setVolume(0);
	}

	/**
	 * Méthode appelée à la fin du programme pour vider la mémoire
	 */
	public static void clear(){
		sounds.clear();
		soundBuffers.clear();
	}
}
