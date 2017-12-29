package cn.liusiqian.rxjavatraining;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Créé par liusiqian 17/12/24.
 */

public class SixthActivity extends BaseActivity
{
    /*
     * interval操作符发送Long型的事件, 从0开始, 每隔指定的时间就把数字加1并发送出来
     *
     * 下面三个方法用于指定背压策略
     * onBackpressureBuffer()
     * onBackpressureDrop()
     * onBackpressureLatest()
     *
     */

    @Override
    protected void goNext()
    {

    }

    @Override
    protected void triggerRx()
    {
        Flowable.interval(1, TimeUnit.SECONDS).
                onBackpressureBuffer().
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Long>() {

                @Override
                public void onSubscribe(Subscription s)
                {
                    Log.d(TAG, "onSubscribe");
                    s.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(Long aLong)
                {
                    Log.d(TAG, "onNext: " + aLong);

                }

                @Override
                public void onError(Throwable t)
                {
                    Log.w(TAG, "onError: ", t);
                }

                @Override
                public void onComplete()
                {
                    Log.d(TAG, "onComplete");
                }
        });
    }

    @Override
    protected CharSequence setNextText()
    {
        return "To Seventh";
    }

    @Override
    protected CharSequence setTriggetText()
    {
        return "Trigger Rx-interval";
    }

}
