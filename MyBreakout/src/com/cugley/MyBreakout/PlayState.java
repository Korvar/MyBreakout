package com.cugley.MyBreakout;

import org.flixel.*;
import org.flixel.event.*;
import org.flixel.system.*;

public class PlayState extends FlxState
{
	private FlxGroup _balls;
	private Ball _ball;
	private Bat _bat;
	private FlxGroup _blocks;
	private FlxText _scoreDisplay;
	private FlxSprite[] _hearts;
	
	private AFlxCollision batBallCollision;
	private AFlxCollision ballBlockCollision;
	
	private String[] _level;
	private int _currentLevel;
	
	private static String Level00Map = FlxG.loadString("level00.txt"); 
	private static String Level01Map = FlxG.loadString("level01.txt");
	private static String Level02Map = FlxG.loadString("level02.txt");
	private static String Level03Map = FlxG.loadString("level03.txt");
	private static String SimpleLevel = FlxG.loadString("SimpleLevel.txt");
	
	// private FlxDebugger _flxDebugger;
	
	
	
	@Override
	public void create()
	{
		FlxG.debug = false;
		FlxG.visualDebug = false;

		FlxG.setDebuggerLayout(FlxG.DEBUGGER_BIG);
		FlxG.mouse.hide();
		
		// Bat
		_bat = new Bat((int)FlxG.mouse.x, FlxG.height - 24, 0, 0);
		add(_bat);
		FlxG.watch(_bat, "x");
		
		// Ball
		_balls = new FlxGroup();
		_balls.add(new Ball(0, 0, 0, 0, false, _bat));
		add(_balls);
		
		// Score Display
		_scoreDisplay = new FlxText(0, 0, 200, "");
		add(_scoreDisplay);
		
		// Levels
		_level = new String[] 
				{
					SimpleLevel,
					Level00Map,
					Level01Map,
					Level02Map,
					Level03Map
				};
		_currentLevel = 0;
		FlxG.level = _currentLevel;
		
		// Blocks
		_blocks = createBlocks(_level[_currentLevel]);
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
				
				// And now check to see if the level is completed
				if (_blocks.countLiving() == 0)
				{
					_blocks.clear();
					FlxG.play("Yay.mp3");
					
					// Will replace this with a game end screen at some point
					_currentLevel = (_currentLevel + 1) % _level.length; 
					FlxG.level = _currentLevel;
					
					// Next level!
					_blocks = createBlocks(_level[_currentLevel]);
					add(_blocks);
					
					resetBalls();
					
					
				}
			}
			
		};
	}
	
	private void resetBalls()
	{
		// Get rid of any balls in the _balls list
		_balls.clear();
		_balls = new FlxGroup();
		_balls.add(new Ball(0, 0, 0, 0, false, _bat));
		add(_balls);
	}
	
	@Override
	public void update()
	{
		super.update();
		// Let's see if any of the balls have hit the bat.
		FlxG.overlap(_balls, _bat, batBallCollision);
		
		// Let us also see if any of the balls have hit any of the blocks
		FlxG.collide(_balls, _blocks, ballBlockCollision);
		
		// Check that there are still balls left
		if (_balls.countLiving() == 0)
		{
			_bat.hurt(1);
			FlxG.shake();
			resetBalls();
		}
	}
	
	private FlxGroup createBlocks(String levelMap)
	{
		FlxGroup blockGroup;
		int i;
		int j;
		
		int[] blockType = 
			{
				0x00000000,
				0xffff0000,
				0xff00ff00,
				0xff0000ff,
				0xffffff00,
				0xffff00ff,
				0xff00ffff,
				0xffffffff	
			};
		String[] cols;
		String[] rows = levelMap.split("\n");
		int heightInBlocks = rows.length;
		int blockHealth;
		
		blockGroup = new FlxGroup();
		
		for (i = 0; i < heightInBlocks; i++)
		{
			cols = rows[i].split(",");
			if(cols.length <= 1)
			{
				heightInBlocks --;  //not sure why I did this in the original Flash version?
				continue;  
			}
			for (j = 0; j < cols.length; j ++)
			{
				int thisBlock = Integer.parseInt(cols[j].trim());
				if (thisBlock > 0)
				{
					if (thisBlock ==7)
					{
						blockHealth = 2;
					}
					else
					{
						blockHealth = 1;
					}
					blockGroup.add(new Block(j*32, i * 8, 32, 8, blockType[thisBlock], blockHealth, 0, 0));
				}
			}
		}
		return(blockGroup);
		
	}
	

}