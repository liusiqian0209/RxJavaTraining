package cn.liusiqian.rxjavatraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "RxJavaTag";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable.create(new ObservableOnSubscribe<String>()
        {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception
            {
                Log.i(TAG, "subscribe start");
                e.onNext("String 1");
                Log.i(TAG, "subscribe 1");
                e.onNext("String 2");
                Log.i(TAG, "subscribe 2");
                e.onNext("String 3");
                Log.i(TAG, "subscribe 3");
                e.onNext("String 4");
                Log.i(TAG, "subscribe 4");
                e.onComplete();
//                e.onError(new RuntimeException("custom exception"));
//                throw new NullPointerException("custom exception duplicated");
            }
        }).subscribe(new Observer<String>()
        {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d)
            {
                mDisposable = d;
                Log.i(TAG, "onSubscribe");
                Log.i(TAG, "onSubscribe Thread:"+Thread.currentThread().getName());
            }

            @Override
            public void onNext(@NonNull String s)
            {
                Log.i(TAG, "onNext --- " + s);
                Log.i(TAG, "onNext Thread:"+Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e)
            {
                Log.i(TAG, "onError --- " + e.getMessage() + "  isDisposed:" + mDisposable.isDisposed());
                Log.i(TAG, "onError Thread:"+Thread.currentThread().getName());
            }

            @Override
            public void onComplete()
            {
                Log.i(TAG, "onComplete  isDisposed:" + mDisposable.isDisposed());
                Log.i(TAG, "onComplete Thread:"+Thread.currentThread().getName());
            }
        });
    }
}
