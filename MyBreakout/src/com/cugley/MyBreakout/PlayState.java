package com.cugley.MyBreakout;

import org.flixel.*;
import org.flixel.event.*;


public class PlayState extends FlxState
{
	private FlxGroup _balls;
	private Ball _ball;
	private Bat _bat;
	private FlxGroup _blocks;
	private FlxText _scoreDisplay;
	private FlxText _debugDisplay;
	private AFlxCollision batBallCollision;
	private AFlxCollision ballBlockCollision;
	
	private FlxSprite _batAngleLine;
	private FlxSprite _incomingAngleLine;
	private FlxSprite _outgoingAngleLine;
	private FlxSprite _incomingAngleLineRotated;
	private FlxSprite _outgoingAngleLineRotated;

	
	@Override
	public void create()
	{
	
		// Bat
		_bat = new Bat((int)FlxG.mouse.x, FlxG.height - 24, 0, 0);
		add(_bat);
		
		// Ball
		_balls = new FlxGroup();
		_ball = new Ball(0, 0, 0, 0, false, _bat);
		_balls.add(_ball);
		_balls.add(new Ball(FlxG.width / 2, FlxG.height/2, 100, -100));
		add(_balls);
		
		// Score Display
		_scoreDisplay = new FlxText(0, 0, 200, "");
		add(_scoreDisplay);
		
// Blocks
		_blocks = new FlxGroup();
		_blocks.add(new Block(20, 30, 32, 8, 0xFFFFFFFF, 3));
		add(_blocks);
		
		// Set up the collision callbacks
		batBallCollision = new AFlxCollision()
		{
			@Override
			public void callback(FlxObject colBall, FlxObject colBat)
			{
				FlxPoint LeftPoint = new FlxPoint();
				FlxPoint RightPoint = new FlxPoint();
				FlxPoint TopPoint = new FlxPoint();
				FlxPoint BottomPoint = new FlxPoint();
				
				TopPoint.x = colBall.x + colBall.width / 2;
				TopPoint.y = colBall.y;
				LeftPoint.x = colBall.x;
				LeftPoint.y = colBall.y + colBall.height / 2;
				RightPoint.x = colBall.x + colBall.width;
				RightPoint.y = colBall.y + colBall.height / 2;
				BottomPoint.x = colBall.x + colBall.width / 2;
				BottomPoint.y = colBall.y + colBall.height;

				if (colBall.justTouched(FlxObject.WALL))
				{
					colBall.velocity.x = -colBall.velocity.x;
					FlxG.play("Pong.mp3");
				}
				if (colBall.justTouched(FlxObject.UP)) 
				{
					// I have no idea how the ball would be travelling
					// up, but let's chuck this here just in case
					colBall.velocity.y = -colBall.velocity.y;
					FlxG.play("Pong.mp3");
				}
				
				
				//if (colBall.isTouching(FlxObject.DOWN))
				//{
				if (colBat.overlapsPoint(BottomPoint))
				{
					
					colBall.y = colBat.y - colBall.height;
										
					FlxPoint originPoint = new FlxPoint(0,0);
					float batRatio; // How far along the bat the ball hit
					float batAngle;  // The effective angle of the bat at a given point along its length
					
					FlxPoint tmpVelocity = new FlxPoint();
					
					tmpVelocity.x = colBall.velocity.x;
					tmpVelocity.y = colBall.velocity.y;
					
					batRatio = (colBall.x - colBat.x)/(float) colBat.width;
					batAngle = (30 - (60 * (float)batRatio)); // ranges from -30 to 30
					
					// Possibly stupid way of doing this: rotate the the incoming velocity vector
					// by negative the ball angle, so the frame of reference
					// is a flat horizontal bat.  Then do standard reflect,
					// and rotate back.

					FlxPoint incomingVelocity = new FlxPoint();
					FlxPoint outgoingVelocity = new FlxPoint();
					
					incomingVelocity = FlxU.rotatePoint(tmpVelocity.x,  tmpVelocity.y,  0,  0,  -batAngle);
					outgoingVelocity.x = incomingVelocity.x;
					outgoingVelocity.y = -incomingVelocity.y;
					tmpVelocity = FlxU.rotatePoint(outgoingVelocity.x,  outgoingVelocity.y,  0,  0,  -batAngle);
					
					
					colBall.velocity.x = tmpVelocity.x;
					colBall.velocity.y = tmpVelocity.y;

					FlxG.play("Pong.mp3");	
				}
				
			}
			
		};
		
		ballBlockCollision = new AFlxCollision()
		{
			@Override
			public void callback(FlxObject colBall, FlxObject colBlock)
			{
				colBlock.hurt(1);
				FlxG.play("Pang.mp3");
			}
			
		};
	}
	

	
	@Override
	public void update()
	{
		super.update();
		// Let's see if any of the balls have hit the bat.
		FlxG.overlap(_balls, _bat, batBallCollision);
		
		// Let us also see if any of the balls have hit any of the blocks
		FlxG.collide(_balls, _blocks, ballBlockCollision);
	}
	
	
	

}