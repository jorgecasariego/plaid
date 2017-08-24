/*
 *  Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package io.plaidapp.util.glide

import android.support.rastermill.FrameSequence
import android.support.rastermill.FrameSequenceDrawable
import com.bumptech.glide.load.ImageHeaderParser
import com.bumptech.glide.load.ImageHeaderParser.ImageType.GIF
import com.bumptech.glide.load.ImageHeaderParserUtils
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.io.InputStream

/**
 * Decodes [InputStream]s into [FrameSequenceDrawable]s.
 */
class InputStreamFrameSequenceGifDecoder(
        private val parsers: List<ImageHeaderParser>,
        private val byteArrayPool: ArrayPool,
        private val bitmapPool: BitmapPool
) : ResourceDecoder<InputStream, FrameSequenceDrawable> {

    override fun handles(source: InputStream, options: Options) =
        ImageHeaderParserUtils.getType(parsers, source, byteArrayPool) == GIF

    override fun decode(source: InputStream, width: Int, height: Int, options: Options) =
        FrameSequenceDrawableResource(FrameSequence.decodeStream(source), bitmapPool)

}
