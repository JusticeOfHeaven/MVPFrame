package com.my.mvpframe.rxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import com.my.mvpframe.rxjava.RxSchedulers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by ZJ on 2018/1/31.
 * 封装的EventBus，其中processorMapper管理所有的Processor，可以取消指定的tag订阅
 * <p>
 * FlowableProcessor<Object> mBus = PublishProcessor.create().toSerialized();// 有背压
 * Subject<Object> mBus = PublishSubject.create().toSerialized();// 旧无背压
 * <p>
 * ======================!!!!!!!!!!! Attention !!!!!!!!!!!========================
 * 特别注意：页面注册的订阅，在页面结束的时候要取消订阅{@link #unRegister(Object tag)}
 * ===============================================================================
 */

public class RxBus {
    //    private HashMap<String, CompositeDisposable> mProcessorMap = new HashMap<>();
    private ConcurrentHashMap<Object, List<FlowableProcessor>> processorMapper = new ConcurrentHashMap();
    private static RxBus mRxBus;
//    private final FlowableProcessor<Object> mBus;

    //单列模式
    public static RxBus getIntanceBus() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    private RxBus() {
//        mBus = PublishProcessor.create().toSerialized();
        // 原没背压处理
//        Subject<Object> mBus1 = PublishSubject.create().toSerialized();
    }

    /**
     * 发送消息
     */
    @SuppressWarnings({"unchecked"})
    public void post(String tag, Object object) {
        List<FlowableProcessor> processorList = processorMapper.get(tag);
        if (!isEmpty(processorList)) {
            for (FlowableProcessor processor : processorList) {
                processor.onNext(object);
            }
        }
    }

    public static boolean isEmpty(Collection<FlowableProcessor> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 注册订阅消息
     * 没有用{@link CompositeDisposable}来统一管理，以后可考虑
     */
    public <T> void register(@NonNull Object tag, @NonNull Consumer<T> action) {
        List<FlowableProcessor> processorList = processorMapper.get(tag);
        if (null == processorList) {
            processorList = new ArrayList<FlowableProcessor>();
            processorMapper.put(tag, processorList);
        }
        PublishProcessor<T> objectPublishProcessor = PublishProcessor.create();
        FlowableProcessor<T> processor = objectPublishProcessor.toSerialized();
        processorList.add(processor);
        Disposable disposable = processor.compose(RxSchedulers.composeFlowable())
                .subscribe(action, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 取消订阅
     */
    public void unRegister(Object tag) {
        List<FlowableProcessor> processorList = processorMapper.get(tag);
        if (null != processorList) {
            processorMapper.remove(tag);
        }
    }

    /**
     * 取消所有订阅
     * 注：仅仅只在程序退出的时候调用
     */
    public void clearAll() {
        processorMapper.clear();
    }
}
