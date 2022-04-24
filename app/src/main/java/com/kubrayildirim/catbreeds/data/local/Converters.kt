package com.kubrayildirim.catbreeds.data.local

import androidx.room.TypeConverter
import com.kubrayildirim.catbreeds.data.model.Image

class Converters {
    @TypeConverter
    fun fromImage(image: Image): String? {
        return image.url
    }

    @TypeConverter
    fun toImage(name: String): Image {
        return Image(name, name)
    }
}