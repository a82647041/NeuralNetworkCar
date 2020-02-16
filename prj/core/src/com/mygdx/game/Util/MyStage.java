package com.mygdx.game.Util;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MyStage extends Stage {
    InputProcessor inputProcessor;
    public MyStage(InputProcessor inputProcessor){
        this.inputProcessor = inputProcessor;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        inputProcessor.touchDown(screenX, screenY, pointer, button);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        inputProcessor.touchDragged(screenX, screenY, pointer);
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inputProcessor.touchUp(screenX, screenY, pointer, button);
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        inputProcessor.mouseMoved(screenX, screenY);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        inputProcessor.scrolled(amount);
        return super.scrolled(amount);
    }

    @Override
    public boolean keyDown(int keyCode) {
        inputProcessor.keyDown(keyCode);
        return super.keyDown(keyCode);
    }

    @Override
    public boolean keyUp(int keyCode) {
        inputProcessor.keyUp(keyCode);
        return super.keyUp(keyCode);
    }

    @Override
    public boolean keyTyped(char character) {
        inputProcessor.keyTyped(character);
        return super.keyTyped(character);
    }
}
