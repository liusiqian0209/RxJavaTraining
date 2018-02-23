package cn.liusiqian.rxjavatraining;

import android.content.Intent;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liusiqian on 2017/12/7.
 */

public class SecondActivity extends BaseActivity {

    @Override
    protected void goNext() {
        Intent intent = new Intent(this,ThirdActivity.class);
        startActivity(intent);
    }

    /*
     *多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
     *多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
     *
     * 仅有subscribeOn时，后续的onNext也会被切换到指定的线程上
     * subscribeOn影响前面，observeOn影响后面
     */

    /*
        Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
        Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
        Schedulers.newThread() 代表一个常规的新线程
        Schedulers.trampoline() 当其它排队的任务完成后，在当前线程排队开始执行
        AndroidSchedulers.mainThread() 代表Android的主线程
     */

    @Override
    protected void triggerRx() {
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
//                e.onError(new RuntimeException("custom exception"));
//                throw new NullPointerException("custom exception duplicated");
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "doOnNext---accept  s = " + s);
                        Log.i(TAG, "doOnNext---accept Thread:" + Thread.currentThread().getName());
                        Thread.sleep(1000);
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
        return "To Third";
    }

    @Override
    protected CharSequence setTriggetText() {
        return "Trigger Rx-async";
    }
}
