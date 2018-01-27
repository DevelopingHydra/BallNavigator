package at.passini.ballnavigator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Benedikt on 27.01.2018.
 */

public class GameActivity  extends AppCompatActivity{
    private GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = new GameView(this);
        setContentView(gv);
    }
}
