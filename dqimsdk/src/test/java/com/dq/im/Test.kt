package com.dq.im

import org.junit.Test

class Test {
    @Test
    fun addition_isCorrect() {
        val list = arrayListOf<String>("1","2","c")

        val result = list.map {
            User(it)
        }
        println("$result")
    }

    inner class User(var name :String){
        override fun toString(): String {
            return "User(name='$name')"
        }
    }
}