package cn.liusiqian.rxjavatraining;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liusiqian on 2017/12/7.
 */

public class FourthActivity extends BaseActivity {

    @Override
    protected void goNext() {
    }

    /*
     * zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件
     * 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据
     *
     * 当收到一个数据源的Complete之后，无论另一个数据源的事件有没有发送完成，都不在使其继续发送了
     * zip常用于请求多个接口，全部返回之后再显示结果的情况
     */

    @Override
    protected void triggerRx() {
        Observable observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.i(TAG, "Source 1 subscribe start");
                Log.i(TAG, "Source 1 subscribe Thread:" + Thread.currentThread().getName());
                e.onNext("Event AAA");
                Log.i(TAG, "subscribe A");
                e.onNext("Event BBB");
                Log.i(TAG, "subscribe B");
                e.onNext("Event CCC");
                Log.i(TAG, "subscribe C");
                e.onNext("Event DDD");
                Log.i(TAG, "subscribe D");
                e.onComplete();
                Log.i(TAG, "Source 1 Complete");
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "Source 1 doOnNext---accept：" + s);
                Log.i(TAG, "Source 1 doOnNext---accept Thread:" + Thread.currentThread().getName());
                Thread.sleep(1200);
            }
        }).subscribeOn(Schedulers.newThread());

        Observable observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "Source 2 subscribe start");
                Log.i(TAG, "Source 2 subscribe Thread:" + Thread.currentThread().getName());
                emitter.onNext(1111);
                Log.i(TAG, "Source 2 subscribe 1");
                emitter.onNext(2222);
                Log.i(TAG, "Source 2 subscribe 2");
                emitter.onNext(3333);
                Log.i(TAG, "Source 2 subscribe 3");
                emitter.onNext(4444);
                Log.i(TAG, "Source 2 subscribe 4");
                emitter.onNext(5555);
                Log.i(TAG, "Source 2 subscribe 5");
                emitter.onNext(6666);
                Log.i(TAG, "Source 2 subscribe 6");
                emitter.onComplete();
                Log.i(TAG, "Source 2 Complete");
            }
        }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer i) throws Exception {
                Log.i(TAG, "Source 2 doOnNext---accept：" + i);
                Log.i(TAG, "Source 2 doOnNext---accept Thread:" + Thread.currentThread().getName());
                Thread.sleep(1000);
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String string, Integer integer) throws Exception {
                Log.i(TAG, "ZIP BiFunction---apply：" + string +","+integer);
                return string + " && " + integer;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
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
        return "To Fifth";
    }

    @Override
    protected CharSequence setTriggetText() {
        return "Trigger Rx-zip";
    }
}
