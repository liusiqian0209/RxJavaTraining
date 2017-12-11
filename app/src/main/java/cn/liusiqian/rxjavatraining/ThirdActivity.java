package cn.liusiqian.rxjavatraining;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liusiqian on 2017/12/11.
 */

public class ThirdActivity extends BaseActivity {
    @Override
    protected void goNext() {

    }

    @Override
    protected void triggerRx() {
        /*
         * map的作用就是对上游发送的每一个事件应用一个函数, 使得每一个事件都按照指定的函数去变化
         */
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.i(TAG, "subscribe start");
                Log.i(TAG, "subscribe Thread:" + Thread.currentThread().getName());
                e.onNext("String 1");
                Log.i(TAG, "subscribe 1");
                e.onNext("String 2");
                Log.i(TAG, "subscribe 2");
                e.onNext("String 3");
                Log.i(TAG, "subscribe 3");
                e.onNext("String 4");
                Log.i(TAG, "subscribe 4");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "doOnNext---accept  s = " + s);
                        Log.i(TAG, "doOnNext---accept Thread:" + Thread.currentThread().getName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return "This is event "+ s;
                    }
                })
                .subscribe(new Observer<String>() {
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

    @Override
    protected CharSequence setNextText() {
        return "To Fourth";
    }

    @Override
    protected CharSequence setTriggetText() {
        return "Trigger Rx-operator";
    }

}
