package com.cugley.MyBreakout;

import org.flixel.*;
import org.flixel.event.*;


public class PlayState extends FlxState
{
	private FlxGroup _balls;
	private Ball _ball;
	private Bat _bat;
	private FlxGroup _block;
	private FlxText _scoreDisplay;
	private FlxText _debugDisplay;
	private AFlxCollision batBallCollision;
	
	private FlxSprite _batAngleLine;
	private FlxSprite _incomingAngleLine;
	private FlxSprite _outgoingAngleLine;
	private FlxSprite _incomingAngleLineRotated;
	private FlxSprite _outgoingAngleLineRotated;

	
	@Override
	public void create()
	{
//		// Debug display
//		_debugDisplay = new FlxText(0, 0, FlxG.width, "");
//		add(_debugDisplay);
//		// Debug Line
//		_batAngleLine = new FlxSprite(FlxG.width/2 - 50, FlxG.height/2);
//		_batAngleLine.makeGraphic(100, 1,0xFFFF0000);
//		add(_batAngleLine);
//		// Debug Line
//		_incomingAngleLine = new FlxSprite(FlxG.width/2, FlxG.height/2);
//		_incomingAngleLine.makeGraphic(50, 1,0xFF88FF88);
//		_incomingAngleLine.origin=new FlxPoint(0,0);
//		add(_incomingAngleLine);
//		_outgoingAngleLine = new FlxSprite(FlxG.width/2, FlxG.height/2);
//		_outgoingAngleLine.makeGraphic(50, 1,0xFFCCFFCC);
//		_outgoingAngleLine.origin=new FlxPoint(0,0);
//		add(_outgoingAngleLine);
//		_incomingAngleLineRotated = new FlxSprite(FlxG.width/2, FlxG.height/2);
//		_incomingAngleLineRotated.makeGraphic(50, 1,0xFFFF8888);
//		_incomingAngleLineRotated.origin=new FlxPoint(0,0);
//		add(_incomingAngleLineRotated);
//		_outgoingAngleLineRotated = new FlxSprite(FlxG.width/2, FlxG.height/2);
//		_outgoingAngleLineRotated.makeGraphic(50, 1,0xFFFFCCCC);
//		_outgoingAngleLineRotated.origin=new FlxPoint(0,0);
//		add(_outgoingAngleLineRotated);
		
		// Ball
		_balls = new FlxGroup();
		_ball = new Ball(FlxG.width / 2, FlxG.height /2, 100, 100);
		_balls.add(_ball);
		_balls.add(new Ball(FlxG.width / 2, FlxG.height/2, 100, -100));
		add(_balls);
		
		// Score Display
		_scoreDisplay = new FlxText(0, 0, 200, "");
		add(_scoreDisplay);
		
		// Bat
		_bat = new Bat((int)FlxG.mouse.x, FlxG.height - 24, 0, 0);
		add(_bat);
		
		// Set up the collision callback
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
					String tempStr;
					
//					tempStr = String.valueOf(tmpVelocity.x) + " " 
//							+ String.valueOf(tmpVelocity.y) + " | ";
					
					incomingVelocity = FlxU.rotatePoint(tmpVelocity.x,  tmpVelocity.y,  0,  0,  -batAngle);
					outgoingVelocity.x = incomingVelocity.x;
					outgoingVelocity.y = -incomingVelocity.y;
					tmpVelocity = FlxU.rotatePoint(outgoingVelocity.x,  outgoingVelocity.y,  0,  0,  -batAngle);
					
					
					colBall.velocity.x = tmpVelocity.x;
					colBall.velocity.y = tmpVelocity.y;

					FlxG.play("Pong.mp3");	
//					tempStr =  tempStr + " " 
//							+ String.valueOf(incomingVelocity.x) + " " 
//							+ String.valueOf(incomingVelocity.y) + " | "
//							+ String.valueOf(outgoingVelocity.x) + " " 
//							+ String.valueOf(outgoingVelocity.y) + " | " 
//							+ String.valueOf(FlxU.getAngle(originPoint, outgoingVelocity)) + " | "
//							+ String.valueOf(tmpVelocity.x) + " " 
//							+ String.valueOf(tmpVelocity.y) + " | "
//							+ String.valueOf(FlxU.getAngle(originPoint, tmpVelocity)) + " | "
//							+ String.valueOf(batAngle);
//					_debugDisplay.setText(tempStr);
//					_batAngleLine.angle = batAngle;
//					
//					
//					_incomingAngleLine.angle = FlxU.getAngle(originPoint, incomingVelocity);
//					_outgoingAngleLine.angle = FlxU.getAngle(originPoint, outgoingVelocity);
//					_incomingAngleLineRotated.angle = FlxU.getAngle(originPoint, incomingVelocity) + batAngle;
//					_outgoingAngleLineRotated.angle = FlxU.getAngle(originPoint, outgoingVelocity) + batAngle;
					
					//colBall.immovable = false;

				}
				
			}
			
		};
		
	}
	

	
	@Override
	public void update()
	{
		super.update();
		// Let's see if any of the balls have hit the bat.
		FlxG.overlap(_balls, _bat, batBallCollision);
	}
	

}