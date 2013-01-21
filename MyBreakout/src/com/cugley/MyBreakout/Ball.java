package com.cugley.MyBreakout;

import org.flixel.FlxObject;
import org.flixel.FlxPoint;
import org.flixel.FlxSprite;
import org.flixel.FlxG;
import org.flixel.FlxU;

public class Ball extends FlxSprite {
	
	private Boolean _served;
	private FlxSprite _bat;
	

	// sound (work out how to do that later)
	
	public Ball()
	{
	}
	
	public Ball(int X, int Y, int XVel, int YVel)
	{
		super();
		create(X, Y, XVel, YVel, false, null);
	}
	public Ball(int X, int Y, int XVel, int YVel, Boolean Served)
	{
		super();
		create(X, Y, XVel, YVel, Served, null);
	}
		
	public Ball(int X, int Y, int XVel, int YVel, Boolean Served, FlxSprite ThisBat)
	{
		super();
		create(X, Y, XVel, YVel, Served, ThisBat);
	}
	
	public void create(int X, int Y, int XVel, int YVel, Boolean Served, FlxSprite ThisBat)
	{
		makeGraphic(4, 4, 0xFFFFFFFF);
		_served = Served;
		_bat = ThisBat;
		x=X;
		y=Y;
		velocity.x = XVel;
		velocity.y = YVel;
		exists = true;
		elasticity = 1;
		// immovable = true;
		setSolid(true);
	}
	@Override
	public void update()
	{
		if (!_served && (_bat != null))
		{
			velocity.x = 0;
			velocity.y = 0;
			x = _bat.x + _bat.width / 4;
			y = _bat.y - height;
		}
		if (FlxG.mouse.justPressed() && !_served)
		{
			_served = true;
			velocity.x = 100;
			velocity.y = -100;
		}
		if((x > (FlxG.width - width)))
		{
			x = FlxG.width - width;
			velocity.x = -velocity.x;
			FlxG.play("Ping.mp3");
			
		}
		if (x <= 0)
		{
			x = 0;
			velocity.x = -velocity.x;
			FlxG.play("Ping.mp3");			
		}
		if (y <= 0)
		{
			y = 0;
			velocity.y = - velocity.y;
			FlxG.play("Ping.mp3");
		}
		if (y >= FlxG.height)
		{
			FlxG.play("Ow.mp3");
			// kill();
			y = FlxG.height;
			velocity.y = -velocity.y;
			
		}
		
		super.update();
		
		
	}
	


}
