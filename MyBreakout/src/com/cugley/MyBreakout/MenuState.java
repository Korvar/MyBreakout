package com.cugley.MyBreakout;

import org.flixel.FlxState;
import org.flixel.FlxG;
import org.flixel.FlxText;
import org.flixel.event.AFlxCamera;

public class MenuState extends FlxState {
	
	private FlxText text;
	
	@Override
	public void create()
	{
		text = new FlxText(0, (FlxG.width/2) -80, FlxG.width, "My Breakout");
		text.setFormat(null, 16, 0xFFFFFF, "center");
		add(text);
		
		text = new FlxText(0, FlxG.height -24, FlxG.width, "Click to Start");
		text.setFormat(null,8, 0xFFFFFF, "center");
		add(text);
	}
	
	private AFlxCamera onFade = new AFlxCamera()
	{
		@Override
		public void callback()
		{
	
		FlxG.switchState(new PlayState());
		}
	};
	
	@Override
	public void update()
	{
			super.update();
			
			if(FlxG.mouse.justPressed())
			{
				FlxG.flash(0xFFFFFF, (float) 0.75);
				FlxG.fade(0xFF000000, 1, onFade);
			}
		}

}
