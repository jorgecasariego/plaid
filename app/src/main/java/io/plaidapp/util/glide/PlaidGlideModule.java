/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.plaidapp.util.glide;

import android.app.ActivityManager;
import android.content.Context;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Glide module configurations
 */
@GlideModule
public class PlaidGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Prefer higher quality images unless we're on a low RAM device
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final RequestOptions defaultOptions = new RequestOptions();
        defaultOptions.format(activityManager.isLowRamDevice() ?
                DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888);
        builder.setDefaultRequestOptions(defaultOptions);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        final List<ImageHeaderParser> imageHeaderParsers = registry.getImageHeaderParsers();
        registry.prepend(InputStream.class, FrameSequenceDrawable.class,
                new InputStreamFrameSequenceGifDecoder(imageHeaderParsers, glide.getArrayPool(),
                        glide.getBitmapPool()));
        registry.prepend(ByteBuffer.class, FrameSequenceDrawable.class,
                new ByteBufferFrameSequenceGifDecoder(imageHeaderParsers, glide.getBitmapPool()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
