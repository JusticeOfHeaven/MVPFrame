package com.my.mvpframe.bean

/**
 * Created by jzhan on 2018/11/6.
 */
data class DynamicBean(var type:String = "0") {
    var name: String = ""
    var description: String = ""
    var url: String = ""
    var isFinish: Boolean = false
}
