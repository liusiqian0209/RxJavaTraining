package cn.liusiqian.rxjavatraining;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
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

public class FifthActivity extends BaseActivity {

    @Override
    protected void goNext() {
    }

    /*
     * 事件上游和下游处于不同线程时，如果上游事件得不到及时处理，则会造成堆积，当事件过多时造成占用内存过大，从而发生OOM
     * 背压（backpressure）是下游用来控制上游流速的一种手段
     *
     * 在rxjava1.x的时代，上游会给下游set一个producer，下游通过producer向上游请求n个数据，这样上游就有记录下游请求了多少个数据，然后下游请求多少个上游就给多少个，这个就是背压。
     * 在rxjava2.x的时代，背压逻辑全部挪到Flowable里了，所以说Flowable支持背压。
     * 而2.x时代的Observable是没有背压的概念的，Observable如果来不及消费会一直缓存直到OOM
     * rxjava2.x的官方文档里面有讲，大数据流用Flowable，小数据流用Observable
     *
     * filter用于过滤事件，返回true表示事件会继续发送到下游
     * sample方法，每隔一段时间，从事件上游取出一个事件发给下游
     */

    /*
     * Flowable上游使用Flowable，下游使用Subscriber
     */

    @Override
    protected void triggerRx() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
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
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onSubscribe(Subscription subscription) {
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
                        Log.i(TAG, "onError Thread:" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
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
        return "Trigger Rx-backpressure";
    }
}
