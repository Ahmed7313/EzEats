package com.easyEats.ezeats.data.maping

interface Mapper<F, T> {
    fun map(from: F): T
}