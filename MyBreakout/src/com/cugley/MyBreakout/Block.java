package com.cugley.MyBreakout;

import org.flixel.FlxSprite;
import org.flixel.FlxG;


public class Block extends FlxSprite 
{
	private int _maxHealth;
	private Boolean _invulnerable = false;
	
	public Block(int X, int Y, int Width, int Height)
	{
		this(X, Y, Width, Height, 0xFFFFFFFF, 1, 0, 0);
	}
	
	public Block(int X, int Y, int Width, int Height, int Colour)
	{
		this(X, Y, Width, Height, Colour, 1, 0, 0);
	}
	
	public Block(int X, int Y, int Width, int Height, int Colour, int Health)
	{
		this(X, Y, Width, Height, Colour, Health, 0, 0);
	}
	
	public Block(int X, int Y, int Width, int Height, int Colour, int Health, int XVel, int YVel)
	{
		x = X;
		y = Y;
		velocity.x = XVel;
		velocity.y = YVel;
		width = Width;
		height = Height;
		makeGraphic(Width, Height, Colour);
		health = Health;
		_maxHealth = (int) health;
		immovable = true;
	}
	
	@Override
	public  void hurt(float damage)
	{
		if (! _invulnerable)
		{
			super.hurt(damage);
			setAlpha(health / _maxHealth);
		}
	}
	
	@Override
	public void kill()
	{
		FlxG.score += 1;
		super.kill();
	}


}

