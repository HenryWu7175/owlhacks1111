    package edu.temple.myapplication

    import android.content.Context
    import android.graphics.Bitmap
    import android.graphics.BitmapFactory
    import android.net.Uri
    import kotlin.math.abs
    import kotlin.math.roundToInt

    object Scoring {
        fun loadBitmap(ctx: Context, uri: Uri): Bitmap {
            ctx.contentResolver.openInputStream(uri).use { ins ->
                return BitmapFactory.decodeStream(ins!!)
            }
        }

        // Downscale, grayscale-ish (use green channel), mean absolute pixel diff → 0..100
        fun score(reference: Bitmap, current: Bitmap, size: Int = 128): Int {
            val ref = Bitmap.createScaledBitmap(reference, size, size, true)
            val cur = Bitmap.createScaledBitmap(current, size, size, true)

            var sum = 0L
            val n = size * size
            val pixelsRef = IntArray(n)
            val pixelsCur = IntArray(n)
            ref.getPixels(pixelsRef, 0, size, 0, 0, size, size)
            cur.getPixels(pixelsCur, 0, size, 0, 0, size, size)

            for (i in 0 until n) {
                val r1 = (pixelsRef[i] shr 16) and 0xFF
                val g1 = (pixelsRef[i] shr 8) and 0xFF
                val b1 = pixelsRef[i] and 0xFF
                val r2 = (pixelsCur[i] shr 16) and 0xFF
                val g2 = (pixelsCur[i] shr 8) and 0xFF
                val b2 = pixelsCur[i] and 0xFF

                // cheap luminance-ish value
                val l1 = (r1 + 2*g1 + b1) / 4
                val l2 = (r2 + 2*g2 + b2) / 4
                sum += abs(l1 - l2)
            }

            // max diff per pixel = 255; mean diff normalized, invert to similarity
            val mean = sum.toDouble() / n.toDouble()        // 0..255
            val similarity = 1.0 - (mean / 255.0)           // 1..0
            return (similarity * 100.0).coerceIn(0.0, 100.0).roundToInt()
        }
    }