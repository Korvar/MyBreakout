/**
 * 
 */
package com.cugley.MyBreakout;

import org.flixel.*;
import org.flixel.event.AFlxCamera;


/**
 * @author Mike
 *
 */
public class GameOverState extends FlxState
{
	private FlxText _gameOverText;
	private FlxText _scoreText;
	private FlxText _pressToContinueText;
	private float _maxtime;
	private float _time;
	private boolean _finished;
	
	private AFlxCamera onFade = new AFlxCamera()
	{
		@Override
		public void callback()
		{
	
		FlxG.switchState(new MenuState());
		}
	};
	
	
	int i = 0;
	
	public GameOverState()
	{
		super();
	}
	
	@Override
	public void create()
	{
		_maxtime = (float) 5.0;
		_time = _maxtime;
		_finished = false;
		
		_gameOverText = new FlxText(0, FlxG.height/3, FlxG.width, "Game Over!" );
		_gameOverText.setFormat(null, 16, 0xFFFFFF, "center");
		add(_gameOverText);
		
		_scoreText = new FlxText(0, FlxG.height/2, FlxG.width, "You scored " + FlxG.score + " points!");
		_scoreText.setFormat(null, 12, 0xFFFFFF, "center");
		add(_scoreText);

		_pressToContinueText = new FlxText(0, FlxG.height -24, FlxG.width, "Click to Continue");
		_pressToContinueText.setFormat(null,8, 0xFFFFFF, "center");
		add(_pressToContinueText);
	}
	
	@Override
	public void update()
	{
		_time -= FlxG.elapsed;
		if (_time <= 0)
		{
			_finished = true;
		}
		if (_finished)
		{
			FlxG.flash(0xFFFFFF, (float) 0.75);
			FlxG.fade(0xFF000000, 1, onFade);
			
		}
		
		i++;
		_gameOverText.setAlpha(1 - (_time / _maxtime));
		// _scoreText.setText(String.valueOf(_time) + " " + String.valueOf(FlxG.elapsed));
		
		if(FlxG.mouse.justPressed())
		{
			FlxG.flash(0xFFFFFF, (float) 0.75);
			FlxG.fade(0xFF000000, 1, onFade);
		}
		
		super.update();
	}
}
