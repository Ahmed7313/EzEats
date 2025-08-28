package com.easyEats.ezeats.data.remote.auth

import com.easyEats.ezeats.data.remote.auth.Test

class Test private constructor(){
    companion object {
        private var instance: Test? = null

        fun getInstance(): Test {
            if (instance == null) {
                instance = Test()
            }
            return instance!!
        }
    }

    // Add any methods or properties you need for the Test class here
}


class User private constructor(val name: String) {
    companion object {
        fun createGuest() = User("Guest")
        fun createAdmin() = User("Admin")
    }
}

interface Consumer<in T> {
    fun consume(item: T)
}

class StringConsumer : Consumer<Int> {
    override fun consume(item: Int) {
        println("Consumed: $item")
    }
}

class Easy{
    fun getTest(): Test {
        return Test.getInstance()
    }
    fun getUser(): User {
        return User.createGuest()
    }
}