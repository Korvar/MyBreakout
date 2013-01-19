package com.cugley.MyBreakout;

import org.flixel.*;


public class PlayState extends FlxState
{
	private Ball _ball;
	
	
	@Override
	public void create()
	{
		add(new FlxText(0, 0, 200, "Hello World"));
		_ball = new Ball(FlxG.width / 2, FlxG.height /2, 100, -100);
		add(_ball);
	}
}