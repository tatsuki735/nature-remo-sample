package com.mizo0203.natureremoapisample;

import android.support.annotation.NonNull;

import com.mizo0203.natureremoapisample.data.source.NatureRemoLocalApiClient;
import com.mizo0203.natureremoapisample.data.source.NatureRemoRepository;
import com.mizo0203.natureremoapisample.util.AppExecutors;

import static java.util.Objects.requireNonNull;

public class Injection {

    public static NatureRemoRepository provideNatureRemoRepository(@NonNull String remoIpAddress) {
        requireNonNull(remoIpAddress);
        return NatureRemoRepository.getInstance(new AppExecutors(), new NatureRemoLocalApiClient(remoIpAddress));
    }
}
