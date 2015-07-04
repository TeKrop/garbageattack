package com.tekrop.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Classe enveloppant println et permettant l'affichage de logs
 * dans la console mais également dans un fichier
 * @author Valentin PORCHET
 *
 */
public final class Debugger {
	
	// Variables privées
	private static boolean debuggerEnabled = true;
	private static boolean writeEnabled = true;
	private static File myFile;
	private static BufferedWriter myBufferedWriter;
	private static Date actualTime;
	
	/**
	 * Méthode d'initialisation du debugger
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
	 * @param o Objet à afficher
	 */
	public static void log(Object o){
		// On crée une chaîne type pour l'heure de l'affichage
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
	 * Fonction permettant de changer l'état du debugger
	 * @param state Booléen du nouvel état du debugger
	 */
	public static void setDebuggerEnabled(boolean state){
		debuggerEnabled = state;
	}
	
	/**
	 * Fonction permettant de définir si on écrit dans un fichier ou pas
	 * @param state Booléen de l'état de l'écriture dans un fichier
	 * @param path Chemin vers
	 */
	public static void setWriteEnabled(boolean state, String path){
		writeEnabled = state;
		
		/* Si le nom du fichier de log utilisé
		 * est différent de celui actuel, on change */
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
	 * Fonction permettant de définir si on écrit dans un fichier ou pas
	 * @param state Booléen de l'état de l'écriture dans un fichier
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
