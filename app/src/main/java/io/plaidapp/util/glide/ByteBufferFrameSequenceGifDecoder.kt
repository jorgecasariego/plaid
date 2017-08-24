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
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.nio.ByteBuffer

/**
 * Decodes [ByteBuffer]s into [FrameSequenceDrawable]s.
 */
class ByteBufferFrameSequenceGifDecoder(
        private val parsers: List<ImageHeaderParser>,
        private val bitmapPool: BitmapPool
) : ResourceDecoder<ByteBuffer, FrameSequenceDrawable> {

    override fun handles(source: ByteBuffer, options: Options) =
        ImageHeaderParserUtils.getType(parsers, source) == GIF

    override fun decode(source: ByteBuffer, width: Int, height: Int, options: Options): Resource<FrameSequenceDrawable>? {
        // framesequence can't decode gifs directly from ByteBuffers, so read into a ByteArray
        val arr = ByteArray(source.remaining())
        source.get(arr)
        return FrameSequenceDrawableResource(FrameSequence.decodeByteArray(arr), bitmapPool)
    }

}
