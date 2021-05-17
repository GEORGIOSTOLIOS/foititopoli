package com.mygdx.game.UI.Windows;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Foititopoli;
import com.mygdx.game.Logic.GameInstance;
import com.mygdx.game.UI.Screens.MainMenuScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class PauseWindow extends Window{
    GameInstance currentGame;

    public PauseWindow(String title, Skin skin, final Foititopoli game) {
        super(title, skin);

        this.setModal(true);

        TextButton resumeButton = new TextButton("Resume", Foititopoli.gameSkin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PauseWindow.this.remove();
            }
        });
        final TextButton saveButton = new TextButton("Save", Foititopoli.gameSkin);
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {

                    File f = new File("save.ser");

                    FileOutputStream fouts = new FileOutputStream(f);
                    ObjectOutputStream douts = new ObjectOutputStream(fouts);
                    douts.writeObject(game.getGameInstance());
                    douts.close();
                    fouts.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                saveButton.setText("Game Saved");

            }
        });
        TextButton exitButton = new TextButton("Exit to Main Menu", Foititopoli.gameSkin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        add(resumeButton).expand().fill().row();
        add(saveButton).expand().fill().row();
        add(exitButton).expand().fill();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x-getWidth()/2, y-getHeight()/2);
    }
}
