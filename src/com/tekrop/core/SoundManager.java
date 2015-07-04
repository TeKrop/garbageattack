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
	 * Structure principale contenant les sons charg�s
	 */
	private static Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();
	private static Hashtable<String, SoundBuffer> soundBuffers = new Hashtable<String, SoundBuffer>();
	private static final String prefix = "/sounds/";
	private static int volume = 100;

	/**
	 * Fonction priv�e permettant de charger un son, renvoie une exception si probl�me
	 * @param path Chemin vers le fichier contenant le son
	 * @return Le son charg�
	 */
	private static Sound loadSound(String path){
		SoundBuffer soundBuffer = new SoundBuffer();
		InputStream inputStream = soundBuffer.getClass().getResourceAsStream(prefix+path);
		try {			
			soundBuffer.loadFromStream(inputStream);
		} catch (IOException e) {
			Debugger.log("Erreur : le fichier de musique n'a pas pu �tre charg�");
			e.printStackTrace();
		}
		soundBuffers.put(path, soundBuffer); // On rajoute le buffer
		
		Sound sound = new Sound(); // Instanciation d'un nouveau son		
		sound.setBuffer(soundBuffers.get(path)); // On lui associe le buffer
		
		soundBuffers.put(path, soundBuffer); // On rajoute le buffer
		return sound; // Retour du son
	}
	
	/**
	 * M�thode de chargement d'un son
	 * @param key Chemin du son
	 * @return Sound correspondant au son
	 */
	public static Sound load(String key){
		// Si l'image est d�j� stock�e, on la renvoie
		Sound value = sounds.get(key);
		
		// Si l'image n'est pas stock�e, on la charge et stocke
		if (value == null){
			value = loadSound(key);
			value.setVolume(volume);
			sounds.put(key, value);
		}
		
		// On la retourne
		return value;
	}
	
	/**
	 * M�thode permettant de changer le volume de tous les sons
	 * @param volume Volume auquel mettre les sons
	 */
	public static void setVolume(int volume){
		// On met � jour le volume du manager
		SoundManager.volume = volume;
		
		// On cr�e un it�rateur pour pouvoir naviguer dans le tableau des sons
		Iterator<Map.Entry<String, Sound>> it = sounds.entrySet().iterator();
		
		/* On it�re le hashtable, on met le volume voulu */
		while (it.hasNext()){
			it.next().getValue().setVolume(volume);
		}
	}
	
	/**
	 * Met toutes les musiques � un volume de z�ro
	 */
	public static void muteAll(){
		SoundManager.setVolume(0);
	}

	/**
	 * M�thode appel�e � la fin du programme pour vider la m�moire
	 */
	public static void clear(){
		sounds.clear();
		soundBuffers.clear();
	}
}
