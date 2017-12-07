package cn.liusiqian.rxjavatraining;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by liusiqian on 2017/12/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected static final String TAG = "RxJavaTag";
    protected TextView trigger, next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trigger = (TextView) findViewById(R.id.tv_trigger);
        next = (TextView) findViewById(R.id.tv_next);

        trigger.setText(setTriggetText());
        next.setText(setNextText());

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerRx();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected abstract void goNext();

    protected abstract void triggerRx();

    protected CharSequence setNextText(){
        return "Go Next";
    }


    protected CharSequence setTriggetText(){
        return "Trigger Rx";
    }
}
