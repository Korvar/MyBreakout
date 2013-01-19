package com.cugley.MyBreakout;

import org.flixel.*;


public class PlayState extends FlxState
{
	private Ball _ball;
	private Bat _bat;
	private FlxGroup _block;
	private FlxText _scoreDisplay;
	
	
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
		
	}
}