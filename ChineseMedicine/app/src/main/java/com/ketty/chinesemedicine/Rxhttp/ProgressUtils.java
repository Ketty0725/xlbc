package com.ketty.chinesemedicine.Rxhttp;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ProgressUtils {
    public static <T> ObservableTransformer<T, T> applyProgressBar(
            @NonNull final Activity activity, String msg) {
        final WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        final DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.showProgress(activityWeakReference.get());
        return upstream -> upstream.doOnSubscribe(disposable -> {

        }).doOnTerminate(() -> {
            Activity context;
            if ((context = activityWeakReference.get()) != null
                    && !context.isFinishing()) {
                dialogUtils.dismissProgress();
            }
        }).doOnSubscribe((Consumer<Disposable>) disposable -> {
            /*Activity context;
            if ((context = activityWeakReference.get()) != null
                    && !context.isFinishing()) {
                dialogUtils.dismissProgress();
            }*/
        });
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(
            @NonNull final Activity activity) {
        return applyProgressBar(activity, "");
    }
}
