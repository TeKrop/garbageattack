package com.tekrop.core;

import org.jsfml.system.Clock;

/**
 * Classe Chrono enveloppant la classe Clock de JSFML
 * Possibilité de mettre en pause et reprendre
 * @author Valentin PORCHET
 *
 */
public class Chrono {
	
	private Clock clock = new Clock();
	private boolean inPauseState = false;
	private float elapsedTime = 0.f;
	
	/**
	 * Constructeur par défaut
	 */
	public Chrono(){
		this.clock.restart();
	}
	
	/**
	 * Méthode permettant de redémarrer le chrono
	 * @return Temps écoulé en secondes
	 */
	public float restart(){
		
		float returnTime = this.getElapsedTime();
		
		this.elapsedTime = 0.f;
		this.clock.restart();
		this.inPauseState = false;
		
		return returnTime;
	}
	
	/**
	 * Met en pause le chronomètre
	 */
	public void pause(){
		this.elapsedTime += this.clock.getElapsedTime().asSeconds();
		this.inPauseState = true;
	}
	
	/**
	 * Reprend le chronomètre
	 */
	public void resume(){
		this.clock.restart();
		this.inPauseState = false;
	}
	
	/**
	 * Permet d'obtenir le temps écoulé en secondes
	 * @return Temps écoulé en secondes
	 */
	public float getElapsedTime(){
		if (this.inPauseState)
			return this.elapsedTime;
		else
			return this.elapsedTime + this.clock.getElapsedTime().asSeconds();
	}

	@Override
	public String toString() {
		return "Chrono [clock=" + clock + ", inPauseState=" + inPauseState
				+ ", elapsedTime=" + elapsedTime + "]";
	}	
}
