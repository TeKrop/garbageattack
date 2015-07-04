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
 * Classe abstraite repr�sentant une entit�
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
	protected CircleShape shadow = new CircleShape(); // Ombre de l'entit�
	protected Chrono chrono;
	protected float speed = 300.f;
	protected float framerate = 10; // Nombre d'images de l'entit� � afficher par seconde
	
	// Etat visuel de l'entit�
	protected State state = State.IDLE; // On initialise � l'�tat "pause"
	protected Direction direction = Direction.RIGHT; // On initialise le personnage � droite
	protected int framePerState = 3; // Nombre d'images pour chaque �tat
	protected int currentFrame = 0; // Premi�re image pour l'�tat donn�
	
	// Etat dans le jeu
	protected int life = 100;
		
	/**
	 * Enum�ration des �tats d'une entit�
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
	 * Enum�ration des directions d'une entit�
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
	 * Constructeur par d�faut d'une entit�
	 * @param position Position de l'entit�
	 * @param dimensions Dimensions d'une image du sprite
	 */
	public Entity (Vector2i position, Vector2i dimensions) {
		Entity.nbEntity++;
		this.id = Entity.nbEntity;		
		this.chrono = ChronoManager.load("Entity" + this.id);
		
		// On consid�re que la hitbox d'un personnage est � ses pieds (pour le mouvement)
		this.moveBox.setPosition(position.x + (1/4.f)*dimensions.x, position.y + (3/4.f)*dimensions.y);
		this.moveBox.setSize(new Vector2f((1/2.f)*dimensions.x, (1/4.f)*dimensions.y));
		this.moveBox.setFillColor(new Color(0,255,0,128));
		
		// Initialisation du sprite
		this.dimensions = dimensions;
		this.sprite.setPosition(new Vector2f(position.x, position.y));
		
		// On initialise �galement son ombre
		this.shadow.setFillColor(new Color(0,0,0,128));
		this.shadow.setPosition(position.x + (1/4.f)*dimensions.x , position.y + (3/4.f)*dimensions.y);
		this.shadow.setRadius(dimensions.x/4);
		this.shadow.setScale(new Vector2f(1, (1/2.f)));
				
		
	}
	
	/**
	 * Constructeur d'une entit� sans pr�ciser les dimensions
	 * @param position Position de l'entit�
	 */
	public Entity (Vector2i position){
		this(position, new Vector2i(100,100));
	}
	
	/**
	 * M�thode de mise � jour du sprite � l'�tat donn�
	 */
	public void updateSprite(){
		this.sprite.setTextureRect(new IntRect(
				(this.state.ordinal()*this.framePerState + this.currentFrame) * dimensions.x,
				this.direction.ordinal() * dimensions.y, 
				dimensions.x, dimensions.y));
//		Debugger.log("Etat : " + this.state + " | Frame : " + this.currentFrame);
	}
	
	/**
	 * M�thode de mise � jour de l'entit�, appel�e � chaque boucle apr�s le reste
	 */
	public void updateEntity(){
		// Mise � jour de l'�tat si le temps �coul� est bon
		if (this.chrono.getElapsedTime() > 1/this.framerate){
			this.currentFrame = (this.currentFrame + 1) % this.framePerState;
			
			// Si on �tait en �tat d'attaque, et qu'on a fini l'animation, on revient en IDLE
			if ((this.currentFrame == 0)&&(this.state == State.ATTACKING))
				this.state = State.IDLE;
			
			this.updateSprite();
			this.chrono.restart();
		}
	}
	
	/**
	 * Fonction de mouvement du personnage
	 * @param direction Direction du mouvement
	 * @param timeElapsed Temps �coul�
	 */
	public void move(Direction direction, float timeElapsed){
		
		/* Mise � jour de la direction (si on appuie en haut ou en bas,
		 on va garder la pr�c�dente direction visuellement dans notre cas particulier */
		if ((this.direction != direction)&&
				(direction != Direction.UP)&&
				(direction != Direction.DOWN)){
			this.direction = direction;
			this.state = State.MOVING;
			this.currentFrame = 0;
			this.chrono.restart();
		}
		
		// Mise � jour de l'�tat
		if ((this.state != State.MOVING)){
			this.state = State.MOVING;
			this.currentFrame = 0;
			this.chrono.restart();
		}
		
		// Mise � jour de la position
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
	 * M�thode permettant de mettre l'entit� en �tat "Idle"
	 */
	public void setIdle(){
		this.state = State.IDLE;
	}

	/**
	 * M�thode permettant de savoir si l'entit� est aux limites du niveau
	 * @param levelLimits Limites du niveau
	 * @return Bool�en avec le r�sultat
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
		// On met � jour les entit�s et les chronos
		Entity.nbEntity--;
		ChronoManager.remove("Entity" + this.id);
	}
}
