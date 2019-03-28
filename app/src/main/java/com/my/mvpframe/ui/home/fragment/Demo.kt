package com.my.mvpframe.ui.home.fragment

/**
 * Create by jzhan on 2019/3/14
 */
class Demo {

    val ss:String

    private constructor(){

    }

    init {
        println("init")
        ss = "haha"
    }

    companion object {
        fun newInstance():Demo{
            return Demo()
        }
    }
}