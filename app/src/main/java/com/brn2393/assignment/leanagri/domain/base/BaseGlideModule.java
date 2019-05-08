package com.brn2393.assignment.leanagri.domain.base;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.brn2393.assignment.leanagri.R;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public final class BaseGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setLogLevel(Log.ERROR)
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background));
        super.applyOptions(context, builder);
    }
}