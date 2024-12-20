package com.mygdx.game.UI.Windows;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Foititopoli;
import com.mygdx.game.Logic.Player;
import com.mygdx.game.Logic.Squares.CourseSquare;
import com.mygdx.game.UI.Screens.GameScreen;

public class CourseInfoWindow extends Window {

    private final CourseSquare course;
    private final Player currentPlayer;

    public CourseInfoWindow(final CourseSquare course, final Player aPlayer, GameScreen.UI ui) {
        super(course.getName(), Foititopoli.gameSkin);
        this.course = course;
        this.currentPlayer = aPlayer;

        setSize(400,200);
        setScale(1.5f);
        setPosition((1280-getWidth()*getScaleX())/2,(720-getHeight()*getScaleY())/2 );

        Label priceLabel = new Label("Τιμή: " + course.getPrice(), Foititopoli.gameSkin);
        add(priceLabel).row();

        Label directionLabel = new Label(course.getDirection(), Foititopoli.gameSkin);
        add(directionLabel).row();

        final Label moneyLabel = new Label(aPlayer.getStudyHours()+"", Foititopoli.gameSkin);
        add(moneyLabel).row();

        final TextButton buyButton = new TextButton("Buy", Foititopoli.gameSkin);
        add(buyButton).row();

        refreshBuyButton(buyButton);
        buyButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (buyButton.getText().toString().toLowerCase().contains("buy")) {
                    currentPlayer.buySquare(course);
                } else if (buyButton.getText().toString().toLowerCase().contains("upgrade")) {
                    course.upgrade(currentPlayer);
                }
                moneyLabel.setText(aPlayer.getStudyHours()+"");
                refreshBuyButton(buyButton);
                ui.updatePlayer(aPlayer);
            }
        });

        TextButton closeButton = new TextButton("Close", Foititopoli.gameSkin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                CourseInfoWindow.this.remove();
            }
        });

        add(closeButton).expand().fillX().bottom().fillX();
        setModal(true);
    }



    private void refreshBuyButton(TextButton button) {
        if (checkCanBuy()) {
            button.setTouchable(Touchable.enabled);
            button.setDisabled(false);
            button.setText("Buy Course");
        } else if (checkCanUpgrade()) {
            button.setTouchable(Touchable.enabled);
            button.setDisabled(false);
            button.setText("Upgrade Course");
        } else if (course.getGrade()==10){
            button.setText("Max Upgrades");
            button.setTouchable(Touchable.disabled);
            button.setDisabled(true);
        } else {
            button.remove();
        }
    }

    private Boolean checkCanBuy() {
        boolean isOnSameSquare = currentPlayer.getPawn().getCurrentSquare().equals(course);
        boolean isAlreadyBought = course.getGrade() >= 5;
        boolean hasEnoughMoney = currentPlayer.getStudyHours() >= course.getPrice();
        return !isAlreadyBought && hasEnoughMoney && isOnSameSquare;
    }

    private Boolean checkCanUpgrade() {
        if (course.getGrade()==0) return false;
        boolean isAlreadyBoughtByPlayer = currentPlayer.getCourseList().contains(course);
        boolean hasEnoughMoney = currentPlayer.getStudyHours() >= course.getUpgradeCost();
        boolean hasMaximumGrade = course.getGrade() == 10;
        return isAlreadyBoughtByPlayer && hasEnoughMoney && !hasMaximumGrade;
    }
}
