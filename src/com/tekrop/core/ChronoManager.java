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
	 * Structure principale contenant les chronos charg�s
	 */
	public static Hashtable<String, Chrono> chronos = new Hashtable<String, Chrono>();
		
	/**
	 * M�thode de chargement d'un chrono
	 * @param key Chemin de l'image
	 * @return Chrono correspondant � la cl�
	 */
	public static Chrono load(String key){
		// Si le chrono est d�j� stock�, on le renvoie
		Chrono value = chronos.get(key);
		
		// Si le chrono n'est pas stock�, on le charge et stocke
		if (value == null){
			value = new Chrono();
			chronos.put(key, value);
		}
		
		// On la retourne
		return value;
	}
	
	/**
	 * M�thode permettant de mettre en pause tous les chronos
	 */
	public static void pauseAll(){
		// On cr�e un it�rateur pour pouvoir naviguer dans le tableau des chronos
		Iterator<Map.Entry<String, Chrono>> it = chronos.entrySet().iterator();
		
		/* On it�re le hashtable, et on resume les chronos */
		while (it.hasNext()){
			it.next().getValue().pause();
		}
	}
	
	/**
	 * M�thode permettant de remettre en route les chronos
	 */
	public static void resumeAll(){
		// On cr�e un it�rateur pour pouvoir naviguer dans le tableau des chronos
		Iterator<Map.Entry<String, Chrono>> it = chronos.entrySet().iterator();
		
		/* On it�re le hashtable, et on resume les chronos */
		while (it.hasNext()){
			it.next().getValue().resume();
		}
	}
	
	/**
	 * M�thode de retrait d'un chrono du hashtable
	 * @param key cha�ne pour identifier le chrono
	 */
	public static void remove(String key){
		chronos.remove(key);
	}
	
	/**
	 * M�thode appel�e � la fin du programme pour vider la m�moire
	 */
	public static void clear(){
		chronos.clear();
	}	
}
