package cn.liusiqian.rxjavatraining;

import io.reactivex.schedulers.Schedulers;

/**
 * Créé par liusiqian 18/2/23.
 */

public class SchedulerDemo
{
    //RxJava中可以使用Schedulers开启线程
    public void loadBitmap() {
        Schedulers.io().createWorker().schedule(new Runnable()
        {
            @Override
            public void run()
            {
                //do something here
            }
        });
    }
}
