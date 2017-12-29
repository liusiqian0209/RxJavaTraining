package cn.liusiqian.rxjavatraining;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by liusiqian on 2017/12/29.
 */

public class SeventhActivity extends BaseActivity {
    @Override
    protected void goNext() {

    }

    /**
     * requested()表示外部请求的数量
     *
     * 下游调用request(n) 告诉上游它的处理能力，上游每发送一个next事件之后，requested就减一
     * 注意仅next事件生效，complete和error事件不会消耗requested
     * 当减到0时，则代表下游没有处理能力了，如果这个时候继续发送事件，在此背压策略下则会抛出MissingBackpressureException
     *
     * 当上下游工作在不同的线程里时，每一个线程里都有一个requested
     * 下游调用request（1000）时，实际上改变的是下游线程中的requested
     * 而上游中的requested的值是由RxJava内部调用request(n)去设置的，这个会在下游每消费96个事件时自动触发
     */

    @Override
    protected void triggerRx() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "before emit, requested = " + emitter.requested());

                Log.i(TAG, "emit 1");
                emitter.onNext(1);
                Log.i(TAG, "after emit 1, requested = " + emitter.requested());

                Log.i(TAG, "emit 2");
                emitter.onNext(2);
                Log.i(TAG, "after emit 2, requested = " + emitter.requested());

                Log.i(TAG, "emit 3");
                emitter.onNext(3);
                Log.i(TAG, "after emit 3, requested = " + emitter.requested());

                Log.i(TAG, "emit complete");
                emitter.onComplete();

                Log.i(TAG, "after emit complete, requested = " + emitter.requested());
            }
        }, BackpressureStrategy.ERROR)
        .subscribe(new Subscriber<Integer>() {
            private Subscription mSubscription;

            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "onSubscribe");
                mSubscription = s;
                s.request(10);  //request 10
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    @Override
    protected CharSequence setNextText() {
        return "To Seventh";
    }

    @Override
    protected CharSequence setTriggetText() {
        return "Trigger Rx-Backpreesure";
    }
}
