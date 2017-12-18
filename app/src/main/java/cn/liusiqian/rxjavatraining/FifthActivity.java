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
     *
     * Flowable在设计的时候采用了响应式拉取的思路来更好的解决上下游流速不均衡的问题，request可以当作下游处理事件的能力，
     * 下游能处理几个就告诉上游我要几个, 这样只要上游根据下游的处理能力来决定发送多少事件, 就不会造成一窝蜂的发出一堆事件来, 从而导致OOM
     * 只有当上游正确的实现了如何根据下游的处理能力来发送事件的时候, 才能达到这种效果
     */

    @Override
    protected void triggerRx() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                Log.i(TAG, "subscribe start");
                Log.i(TAG, "subscribe Thread:" + Thread.currentThread().getName());
                e.onNext("Event AAA");
                Log.i(TAG, "subscribe A");
                e.onNext("Event BBB");
                Log.i(TAG, "subscribe B");
                e.onNext("Event CCC");
                Log.i(TAG, "subscribe C");
                e.onNext("Event DDD");
                Log.i(TAG, "subscribe D");
                e.onComplete();
                Log.i(TAG, "Complete");
            }
        }, BackpressureStrategy.ERROR)      //上下游流速不均衡的时候直接抛出异常
                // 1、上下游处于同一线程时，如果出现事件流速不均匀时，则直接按照上面背压策略处理
                // 2、由于在Flowable里默认有一个大小为128的缓冲区, 当上游与下游工作在不同的线程中时, 上游会先把事件发送到这个缓冲区中
                // 因此, 下游虽然没有调用request, 但是上游在缓冲区中保存着这些事件, 只有当下游调用request时, 才会从缓冲区里取出事件发给下游。
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        //调用 subscription.cancel(); 可以直接切断上游与下游之间的联系，此时类似于 disposable.dispose();

                        Log.i(TAG, "onSubscribe");
                        Log.i(TAG, "onSubscribe Thread:" + Thread.currentThread().getName());
                        subscription.request(Long.MAX_VALUE);
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
