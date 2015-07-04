package com.tekrop.core;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/**
 * Classe abstraite représentant une entité
 * @author Valentin PORCHET
 *
 */
public abstract class Entity implements Drawable {
	// Variable globale 
	public static int nbEntity = 0;
	
	protected int id;
	//protected IntRect hitBox; // Rectangle pour les collisions de coups
	protected RectangleShape moveBox = new RectangleShape(); // Rectangle pour les collisions de mouvement
	protected Vector2i dimensions; // Dimensions d'une image du sprite
	
	// Variables visuelles
	protected Sprite sprite = new Sprite();
	protected CircleShape shadow = new CircleShape(); // Ombre de l'entité
	protected Chrono chrono;
	protected float speed = 300.f;
	protected float framerate = 10; // Nombre d'images de l'entité à afficher par seconde
	
	// Etat visuel de l'entité
	protected State state = State.IDLE; // On initialise à l'état "pause"
	protected Direction direction = Direction.RIGHT; // On initialise le personnage à droite
	protected int framePerState = 3; // Nombre d'images pour chaque état
	protected int currentFrame = 0; // Première image pour l'état donné
	
	// Etat dans le jeu
	protected int life = 100;
		
	/**
	 * Enumération des états d'une entité
	 * @author Valentin PORCHET
	 *
	 */
	public enum State {
		/**
	     * Etat "en attente"
	     */
		IDLE,
		
		/**
	     * Etat de mouvement
	     */
		MOVING,
		
		/**
	     * Etat d'attaque
	     */
		ATTACKING,
		
		/**
		 * Etat de saut
		 */
		JUMPING,
		
		/**
	     * Etat de prise de coup
	     */
		HIT
	}
	
	/**
	 * Enumération des directions d'une entité
	 * @author Valentin PORCHET
	 *
	 */
	public enum Direction {
		/**
	     * Direction droite
	     */
		RIGHT,
		
		/**
	     * Direction bas
	     */
		DOWN,
		
		/**
	     * Direction gauche
	     */
		LEFT,
		
		/**
	     * Direction haut
	     */
		UP
	}
	
	/**
	 * Constructeur par défaut d'une entité
	 * @param position Position de l'entité
	 * @param dimensions Dimensions d'une image du sprite
	 */
	public Entity (Vector2i position, Vector2i dimensions) {
		Entity.nbEntity++;
		this.id = Entity.nbEntity;		
		this.chrono = ChronoManager.load("Entity" + this.id);
		
		// On considère que la hitbox d'un personnage est à ses pieds (pour le mouvement)
		this.moveBox.setPosition(position.x + (1/4.f)*dimensions.x, position.y + (3/4.f)*dimensions.y);
		this.moveBox.setSize(new Vector2f((1/2.f)*dimensions.x, (1/4.f)*dimensions.y));
		this.moveBox.setFillColor(new Color(0,255,0,128));
		
		// Initialisation du sprite
		this.dimensions = dimensions;
		this.sprite.setPosition(new Vector2f(position.x, position.y));
		
		// On initialise également son ombre
		this.shadow.setFillColor(new Color(0,0,0,128));
		this.shadow.setPosition(position.x + (1/4.f)*dimensions.x , position.y + (3/4.f)*dimensions.y);
		this.shadow.setRadius(dimensions.x/4);
		this.shadow.setScale(new Vector2f(1, (1/2.f)));
				
		
	}
	
	/**
	 * Constructeur d'une entité sans préciser les dimensions
	 * @param position Position de l'entité
	 */
	public Entity (Vector2i position){
		this(position, new Vector2i(100,100));
	}
	
	/**
	 * Méthode de mise à jour du sprite à l'état donné
	 */
	public void updateSprite(){
		this.sprite.setTextureRect(new IntRect(
				(this.state.ordinal()*this.framePerState + this.currentFrame) * dimensions.x,
				this.direction.ordinal() * dimensions.y, 
				dimensions.x, dimensions.y));
//		Debugger.log("Etat : " + this.state + " | Frame : " + this.currentFrame);
	}
	
	/**
	 * Méthode de mise à jour de l'entité, appelée à chaque boucle après le reste
	 */
	public void updateEntity(){
		// Mise à jour de l'état si le temps écoulé est bon
		if (this.chrono.getElapsedTime() > 1/this.framerate){
			this.currentFrame = (this.currentFrame + 1) % this.framePerState;
			
			// Si on était en état d'attaque, et qu'on a fini l'animation, on revient en IDLE
			if ((this.currentFrame == 0)&&(this.state == State.ATTACKING))
				this.state = State.IDLE;
			
			this.updateSprite();
			this.chrono.restart();
		}
	}
	
	/**
	 * Fonction de mouvement du personnage
	 * @param direction Direction du mouvement
	 * @param timeElapsed Temps écoulé
	 */
	public void move(Direction direction, float timeElapsed){
		
		/* Mise à jour de la direction (si on appuie en haut ou en bas,
		 on va garder la précédente direction visuellement dans notre cas particulier */
		if ((this.direction != direction)&&
				(direction != Direction.UP)&&
				(direction != Direction.DOWN)){
			this.direction = direction;
			this.state = State.MOVING;
			this.currentFrame = 0;
			this.chrono.restart();
		}
		
		// Mise à jour de l'état
		if ((this.state != State.MOVING)){
			this.state = State.MOVING;
			this.currentFrame = 0;
			this.chrono.restart();
		}
		
		// Mise à jour de la position
		float distance = this.speed * timeElapsed;
		float x = 0, y = 0;
		
		switch(direction) {
			case UP:
				y = -distance;
				break;
			
			case RIGHT:
				x = distance;
				break;
			
			case DOWN:
				y = distance;
				break;
			
			case LEFT:
				x = -distance;
		}
		
		/* On va d'abord tester si jamais le mouvement ne fait 
		   pas sortir le personnage du cadre */
		Vector2f newPos = new Vector2f(this.sprite.getPosition().x + x, 
									   this.sprite.getPosition().y + y);
		
		/* Si c'est bon, on bouge */
		if (newPos.x + (1/4.f)*this.dimensions.x > 0 && 
			newPos.x + (3/4.f)*this.dimensions.x < 800 && 
			newPos.y > 250 && newPos.y + this.dimensions.y < 600){
			this.sprite.move(x, y);
			this.shadow.move(x, y);
			this.moveBox.move(x, y);
		}
	}
	
	/**
	 * Fonction permettant au personnage d'attaquer
	 */
	public void attack(){
		this.state = State.ATTACKING;
		this.currentFrame = 0;
		this.chrono.restart();
		// On lance le son
		SoundManager.load("hit.ogg").play();
	}
	
	/**
	 * Méthode permettant de mettre l'entité en état "Idle"
	 */
	public void setIdle(){
		this.state = State.IDLE;
	}

	/**
	 * Méthode permettant de savoir si l'entité est aux limites du niveau
	 * @param levelLimits Limites du niveau
	 * @return Booléen avec le résultat
	 */
	public boolean intersects(FloatRect levelLimits) {
		return this.moveBox.getGlobalBounds().intersection(levelLimits) != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		target.draw(this.shadow);
		target.draw(this.sprite);
		
		// DEBUG
		//target.draw(this.moveBox);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		// On met à jour les entités et les chronos
		Entity.nbEntity--;
		ChronoManager.remove("Entity" + this.id);
	}
}
