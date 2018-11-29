package com.mizo0203.natureremoapisample;

import android.support.annotation.NonNull;

import com.mizo0203.natureremoapisample.data.source.NatureApiClient;
import com.mizo0203.natureremoapisample.data.source.NatureRemoLocalApiClient;
import com.mizo0203.natureremoapisample.data.source.NatureRemoRepository;
import com.mizo0203.natureremoapisample.util.AppExecutors;

import static java.util.Objects.requireNonNull;

public class Injection {

    public static NatureRemoRepository provideNatureRemoRepository(@NonNull String token, @NonNull String remoIpAddress) {
        requireNonNull(token);
        requireNonNull(remoIpAddress);
        return NatureRemoRepository.getInstance(new AppExecutors(), new NatureApiClient(token), new NatureRemoLocalApiClient(remoIpAddress));
    }
}
