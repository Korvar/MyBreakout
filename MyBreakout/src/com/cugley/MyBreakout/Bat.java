package com.cugley.MyBreakout;

import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxG;

public class Bat extends FlxSprite 
{
	
	public int _max_health;
	
	public Bat(int X, int Y, int XVel, int YVel)
	{
		_max_health = 5;
		health = _max_health;
		makeGraphic(32,  8,  0xFFFFFFFF);
		origin.x = width / 2;
		origin.y = height / 2;
		elasticity = (float) 0.5;
		immovable = true;
		setSolid(true);
	}

	@Override public void update()
	{
		x = FlxG.mouse.x - width / 2;
		if (x < 0) x = 0;
		if (x> FlxG.width - width) x = FlxG.width - width;
		y = FlxG.height - 24;
			
		super.update();
	}
}
