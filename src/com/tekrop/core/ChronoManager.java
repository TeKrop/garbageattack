package com.tekrop.core;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Classe de gestion des Chrono
 * @author Valentin PORCHET
 *
 */
public class ChronoManager {
	/**
	 * Structure principale contenant les chronos chargés
	 */
	public static Hashtable<String, Chrono> chronos = new Hashtable<String, Chrono>();
		
	/**
	 * Méthode de chargement d'un chrono
	 * @param key Chemin de l'image
	 * @return Chrono correspondant à la clé
	 */
	public static Chrono load(String key){
		// Si le chrono est déjà stocké, on le renvoie
		Chrono value = chronos.get(key);
		
		// Si le chrono n'est pas stocké, on le charge et stocke
		if (value == null){
			value = new Chrono();
			chronos.put(key, value);
		}
		
		// On la retourne
		return value;
	}
	
	/**
	 * Méthode permettant de mettre en pause tous les chronos
	 */
	public static void pauseAll(){
		// On crée un itérateur pour pouvoir naviguer dans le tableau des chronos
		Iterator<Map.Entry<String, Chrono>> it = chronos.entrySet().iterator();
		
		/* On itère le hashtable, et on resume les chronos */
		while (it.hasNext()){
			it.next().getValue().pause();
		}
	}
	
	/**
	 * Méthode permettant de remettre en route les chronos
	 */
	public static void resumeAll(){
		// On crée un itérateur pour pouvoir naviguer dans le tableau des chronos
		Iterator<Map.Entry<String, Chrono>> it = chronos.entrySet().iterator();
		
		/* On itère le hashtable, et on resume les chronos */
		while (it.hasNext()){
			it.next().getValue().resume();
		}
	}
	
	/**
	 * Méthode de retrait d'un chrono du hashtable
	 * @param key chaîne pour identifier le chrono
	 */
	public static void remove(String key){
		chronos.remove(key);
	}
	
	/**
	 * Méthode appelée à la fin du programme pour vider la mémoire
	 */
	public static void clear(){
		chronos.clear();
	}	
}
