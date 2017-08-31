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

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.support.rastermill.FrameSequence
import android.support.rastermill.FrameSequenceDrawable
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.drawable.DrawableResource

/**
 * A wrapper for [FrameSequenceDrawable]s
 */
class FrameSequenceDrawableResource(
        framesequence: FrameSequence,
        bitmapPool: BitmapPool
) : DrawableResource<FrameSequenceDrawable>(FrameSequenceDrawable(framesequence,
        object : FrameSequenceDrawable.BitmapProvider {
            override fun acquireBitmap(minWidth: Int, minHeight: Int) =
                    bitmapPool.get(minWidth, minHeight, ARGB_8888)

            override fun releaseBitmap(bitmap: Bitmap) { bitmapPool.put(bitmap) }
        }
)){

    override fun getResourceClass(): Class<FrameSequenceDrawable>? {
        return FrameSequenceDrawable::class.java
    }

    override fun getSize() = drawable.size

    override fun recycle() {
        if (!drawable.isDestroyed) {
            drawable.destroy()
        }
    }

    override fun initialize()  = drawable.firstFrame.prepareToDraw()

}
