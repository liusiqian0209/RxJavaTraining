package cn.liusiqian.rxjavatraining;

import android.content.Intent;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    @Override
    protected void goNext() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void triggerRx() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
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
        }).subscribe(new Observer<String>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
                Log.i(TAG, "onSubscribe");
                Log.i(TAG, "onSubscribe Thread:" + Thread.currentThread().getName());
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.i(TAG, "onNext --- " + s);
                Log.i(TAG, "onNext Thread:" + Thread.currentThread().getName());
                if (s.contains("String 2")) {
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError --- " + e.getMessage() + "  isDisposed:" + mDisposable.isDisposed());
                Log.i(TAG, "onError Thread:" + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete  isDisposed:" + mDisposable.isDisposed());
                Log.i(TAG, "onComplete Thread:" + Thread.currentThread().getName());
            }
        });
    }
}
