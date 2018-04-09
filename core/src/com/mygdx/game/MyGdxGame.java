package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	ShapeRenderer shapeRenderer ;

	Texture gameover ;

	Texture[] bird ;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0 ;
	Circle birdCircle ;

	int score = 0 ;
	int scoringTube = 0 ;
	BitmapFont font ;

	int gameSate = 0;
	float gravity = 2;

	Texture topTube;
	Texture bottomTube;
	float gap = 1000 ;
	float maxTubeOffset;
	Random randomGenerator ;
	float tubeVelocity = 12 ;
	int numberOfTubes =4;
	int[] tubeX= new int[numberOfTubes] ;
	float[] TubeOffset = new float[numberOfTubes] ;
	float distanceBetweenTube ;
	Rectangle[] topTubeRectangles ;
	Rectangle[] bottomTubeRectangles ;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");

		gameover = new Texture("gameover.png");

		bird = new Texture[2];
		bird[0] = new Texture("bird.png");
		bird[1] = new Texture("bird2.png");

		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		birdY = Gdx.graphics.getHeight() / 2 - bird[0].getHeight() / 2 ;

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() /2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTube = Gdx.graphics.getWidth()  ;

		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

      for (int i =0 ; i< numberOfTubes ; i++){

			TubeOffset[i] = (randomGenerator.nextFloat() - .5f)*(Gdx.graphics.getHeight() - gap -100);

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth()+ i * Gdx.graphics.getWidth() ;

			topTubeRectangles[i]= new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();


		}

	}

	public void startGame(){

		birdY = Gdx.graphics.getHeight() / 2 - bird[0].getHeight() / 2 ;

		for (int i =0 ; i< numberOfTubes ; i++){

			TubeOffset[i] = (randomGenerator.nextFloat() - .5f)*(Gdx.graphics.getHeight() - gap -100);

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth() /2 + Gdx.graphics.getWidth()+ i * Gdx.graphics.getWidth() ;

			topTubeRectangles[i]= new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();


		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameSate == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {

				score++;

				if (scoringTube < numberOfTubes - 1) {

					scoringTube++;
				} else {

					scoringTube = 0;
				}


			}


			if (Gdx.input.justTouched()) {

				//	Gdx.app.log("Touched","Yep");

				velocity = -30;


			}
			for (int i = 0; i < numberOfTubes; i++) {

				if (tubeX[i] < -topTube.getWidth()) {

					tubeX[i] += numberOfTubes * distanceBetweenTube;
					TubeOffset[i] = (randomGenerator.nextFloat() - .5f) * (Gdx.graphics.getHeight() - gap - 50);


				}

				tubeX[i] = (int) (tubeX[i] - tubeVelocity );

				batch.draw(topTube, tubeX[i], (Gdx.graphics.getWidth() / 2) + gap / 2 + TubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + TubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], (Gdx.graphics.getWidth() / 2) + gap / 2 + TubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + TubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());


			}


			if (birdY > 0) {

				velocity = velocity + gravity;
				birdY -= velocity;

			} else {
				gameSate = 2;
			}


		} else if (gameSate == 0) {
			if (Gdx.input.justTouched()) {

				gameSate = 1;

			}

		} else if (gameSate == 2) {


			batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2 , Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
		//	batch.draw(gameover ,Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth() / 2, birdY);

			if (Gdx.input.justTouched()) {

				gameSate = 1;
				startGame();
				score=0;
				scoringTube=0;
				velocity = 0 ;

			}

		}


			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}


			batch.draw(bird[flapState], Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth() / 2, birdY);
			font.draw(batch, String.valueOf(score), 100, 200);

			batch.end();

			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.RED);

			birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + bird[flapState].getHeight() / 2, bird[flapState].getWidth() / 2);
			//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

			for (int i = 0; i < numberOfTubes; i++) {

				//shapeRenderer.rect(tubeX[i], (Gdx.graphics.getWidth() / 2) + gap / 2 +  TubeOffset[i],topTube.getWidth(),topTube.getHeight());
				//	shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + TubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

				if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

					//	Gdx.app.log("Touched","Yep");
					gameSate = 2;

				}


			}

			//shapeRenderer.end();


		}
	/*

		for (int i =0 ; i< numberOfTubes ; i++) {

			shapeRenderer.rect(tubeX[i], (Gdx.graphics.getWidth() / 2) + (gap / 2) +  TubeOffset[i],topTube.getWidth(),topTube.getHeight());
			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + TubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());


		}
	 */

	}

