package com.mizo0203.natureremoapisample;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mizo0203.natureremoapisample.data.source.NatureApiClient;
import com.mizo0203.natureremoapisample.data.source.NatureApiDataSource;
import com.mizo0203.natureremoapisample.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static NatureApiDataSource provideTasksRepository(@NonNull Context context, @NonNull String token) {
        checkNotNull(context);
        return NatureApiDataSource.getInstance(new AppExecutors(), new NatureApiClient(token));
    }
}
