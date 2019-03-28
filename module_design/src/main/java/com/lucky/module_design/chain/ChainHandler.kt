package com.lucky.module_design.chain

/**
 * Create by jzhan on 2019/3/15
 */

abstract class ChainHandler<D> {
    constructor()

    abstract fun handler(cmd:Int,data:D)

    protected var handler:ChainHandler<D>?= null

    fun setNextHandler(handler: ChainHandler<D>){
        this.handler = handler
    }
}