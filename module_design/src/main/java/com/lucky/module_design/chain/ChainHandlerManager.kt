package com.lucky.module_design.chain

/**
 * Create by jzhan on 2019/3/15
 * 责任链模式
 * 用法：
 * ChainHandlerManager.Build
 *          .addHandler(ChainHandler1)
 *          .addHandler(ChainHandler2)
 *          .build
 *
 */
class ChainHandlerManager<D> {
    private var h: ChainHandler<D>? = null

    private constructor(header: ChainHandler<D>?) {
        this.h = header
    }

    class Build<D> {
        private var header: ChainHandler<D>? = null
        private var tail: ChainHandler<D>? = null

        fun addHandler(handler: ChainHandler<D>): Build<D> {
            if (header == null) {
                this.header = handler
                this.tail = handler
            } else {
                this.tail?.setNextHandler(handler)
                this.tail = handler
            }
            return this
        }

        fun build(): ChainHandlerManager<D> {
            return ChainHandlerManager(header)
        }
    }
}