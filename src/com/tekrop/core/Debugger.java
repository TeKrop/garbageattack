package com.tekrop.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Classe enveloppant println et permettant l'affichage de logs
 * dans la console mais �galement dans un fichier
 * @author Valentin PORCHET
 *
 */
public final class Debugger {
	
	// Variables priv�es
	private static boolean debuggerEnabled = true;
	private static boolean writeEnabled = true;
	private static File myFile;
	private static BufferedWriter myBufferedWriter;
	private static Date actualTime;
	
	/**
	 * M�thode d'initialisation du debugger
	 */
	public static void init(){
		// Initialisation du buffer
		myFile = new File("debug.log");
		try {
			myBufferedWriter = new BufferedWriter(new FileWriter(myFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction permettant d'effectuer des logs
	 * @param o Objet � afficher
	 */
	public static void log(Object o){
		// On cr�e une cha�ne type pour l'heure de l'affichage
		actualTime = new Date();
		
		// Affichage en console
		if (debuggerEnabled){
			System.out.println(actualTime + " : " + o);
		}
		// Ecriture dans un fichier de log
		if (writeEnabled){
			try {
				myBufferedWriter.write(actualTime + " : " + o);
				myBufferedWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Fonction permettant de changer l'�tat du debugger
	 * @param state Bool�en du nouvel �tat du debugger
	 */
	public static void setDebuggerEnabled(boolean state){
		debuggerEnabled = state;
	}
	
	/**
	 * Fonction permettant de d�finir si on �crit dans un fichier ou pas
	 * @param state Bool�en de l'�tat de l'�criture dans un fichier
	 * @param path Chemin vers
	 */
	public static void setWriteEnabled(boolean state, String path){
		writeEnabled = state;
		
		/* Si le nom du fichier de log utilis�
		 * est diff�rent de celui actuel, on change */
		if (myFile.getName() != path){
			myFile = new File(path);
			try {
				myBufferedWriter = new BufferedWriter(new FileWriter(myFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Fonction permettant de d�finir si on �crit dans un fichier ou pas
	 * @param state Bool�en de l'�tat de l'�criture dans un fichier
	 */
	public static void setWriteEnabled(boolean state){
		setWriteEnabled(state, "debug.log");
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		myBufferedWriter.close();
	}
}
