package com.cugley.MyBreakout;

import org.flixel.*;
import org.flixel.event.*;


public class PlayState extends FlxState
{
	private Ball _ball;
	private Bat _bat;
	private FlxGroup _block;
	private FlxText _scoreDisplay;
	private AFlxCollision batBallCollision;
	
	
	@Override
	public void create()
	{
		// Ball
		add(new FlxText(0, 0, 200, "Hello World"));
		_ball = new Ball(FlxG.width / 2, FlxG.height /2, 100, -100);
		add(_ball);
		
		// Bat
		_bat = new Bat((int)FlxG.mouse.x, FlxG.height - 24, 0, 0);
		add(_bat);
		
		// Set up the collision callback
		batBallCollision = new AFlxCollision()
		{
			@Override
			public void callback(FlxObject colBall, FlxObject colBat)
			{
				if (colBall.isTouching(FlxObject.WALL))
				{
					colBall.velocity.x = -colBall.velocity.x;
				}
				if (colBall.isTouching(FlxObject.UP) || colBall.isTouching(FlxObject.DOWN))
				{
					colBall.velocity.y = -colBall.velocity.y;
				}
				
			}
			
		};
		
	}
	

	
	@Override
	public void update()
	{
		super.update();
		// Let's see if the ball has hit the bat.
		FlxG.collide(_ball, _bat, batBallCollision);
	}
	

}