1、RecyclerView 里面类的介绍

![RecyclerView](../screenshot/recyclerview.jpg)

| 内部类  ||
|---|---|
|  LayoutManager |负责Item视图的布局的显示管理|
|  ItemDecoration |给每一项Item视图添加修饰的View|
|  Adapter |为每一项Item创建视图|
|  ViewHolder |承载Item视图的子布局|
|  ItemAnimator |负责处理数据添加或者删除时候的动画效果|
|  Cache |Recycler/RecycledViewPool/ViewCacheExtension|

**存**

    public void recycleView(@NonNull View view) {
        // 根据View获取对应的ViewHolder
        RecyclerView.ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
        // 如果Holder被已经被打上了移除的标记，那么就从移除此View
        if (holder.isTmpDetached()) {
            RecyclerView.this.removeDetachedView(view, false);
        }

        if (holder.isScrap()) {
            // 如果此Holder是来自缓存的可见的ViewHolder数组，清除缓存
            holder.unScrap();
        } else if (holder.wasReturnedFromScrap()) {
            // 如果此Holder是来自缓存的不可见的ViewHolder数组，清除缓存
            holder.clearReturnedFromScrapFlag();
        }
        // 开始缓存，继续追源码
        this.recycleViewHolderInternal(holder);
    }
    
    void recycleViewHolderInternal(RecyclerView.ViewHolder holder) {
        
        if (!holder.isScrap() && holder.itemView.getParent() == null) {
            if (holder.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + holder + RecyclerView.this.exceptionLabel());
            } else if (holder.shouldIgnore()) {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle." + RecyclerView.this.exceptionLabel());
            } else {
                boolean transientStatePreventsRecycling = holder.doesTransientStatePreventRecycling();
                boolean forceRecycle = RecyclerView.this.mAdapter != null && transientStatePreventsRecycling && RecyclerView.this.mAdapter.onFailedToRecycleView(holder);
                boolean cached = false;
                boolean recycled = false;
                if (forceRecycle || holder.isRecyclable()) {
                    // 如果缓存数量大于0，并且ViewHolder的Flag标志是有效的
                    // 且非REMOVED跟UPDATE，进行缓存，
                    if (this.mViewCacheMax > 0 && !holder.hasAnyOfTheFlags(526)) {
                        int cachedViewSize = this.mCachedViews.size();
                        if (cachedViewSize >= this.mViewCacheMax && cachedViewSize > 0) {
                            // 移除cachedViews中的第一个View，也就是第一个
                            this.recycleCachedViewAt(0);
                            --cachedViewSize;
                        }
                        // 获取添加元素的在缓存集合中的下标
                        int targetCacheIndex = cachedViewSize;
                        if (RecyclerView.ALLOW_THREAD_GAP_WORK && cachedViewSize > 0 && !RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(holder.mPosition)) {
                            int cacheIndex;
                            for(cacheIndex = cachedViewSize - 1; cacheIndex >= 0; --cacheIndex) {
                                int cachedPos = ((RecyclerView.ViewHolder)this.mCachedViews.get(cacheIndex)).mPosition;
                                //缓存的时候不能覆盖最近经常被使用到缓存
                                if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(cachedPos)) {
                                    break;
                                }
                            }

                            targetCacheIndex = cacheIndex + 1;
                        }
                        //添加缓存
                        this.mCachedViews.add(targetCacheIndex, holder);
                        cached = true;
                    }
                    //如果没有缓存的话就添加进RecycledViewPool
                    if (!cached) {
                        this.addViewHolderToRecycledViewPool(holder, true);
                        recycled = true;
                    }
                }
                //mViewInfoStore中移除这条记录
                RecyclerView.this.mViewInfoStore.removeViewHolder(holder);
                if (!cached && !recycled && transientStatePreventsRecycling) {
                    holder.mOwnerRecyclerView = null;
                }

            }
        } else {
            throw new IllegalArgumentException("Scrapped or attached views may not be recycled. isScrap:" + holder.isScrap() + " isAttached:" + (holder.itemView.getParent() != null) + RecyclerView.this.exceptionLabel());
        }
    }

