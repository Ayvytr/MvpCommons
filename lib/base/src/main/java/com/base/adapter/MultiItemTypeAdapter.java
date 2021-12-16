package com.base.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Jason on 2018/8/14.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener<T> mOnItemClickListener;
    protected OnItemLongClickListener<T> mOnItemLongClickListener;

    public Context getContext() {
        return mContext;
    }

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        if(mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public Resources getResources() {
        return mContext.getResources();
    }

    public int setColor(int color) {
        return getResources().getColor(color);
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    public boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(viewHolder, mDatas.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemLongClickListener
                            .onItemLongClick(viewHolder, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public List<T> getData() {
        return mDatas;
    }

    public void setData(List<T> list) {
        if(list == null) {
            list = new ArrayList<>(0);
        }
        mDatas.clear();
        mDatas = list;
    }

    public boolean isEmpty() {
        return mDatas.isEmpty();
    }

    public void updateList(ArrayList<T> list) {
        if (list == null) {
            return;
        }
        mDatas = list;
        notifyDataSetChanged();
    }

    public void updateList(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<T> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void addFirst(T entity) {
        mDatas.add(0, entity);
        notifyDataSetChanged();
    }

    public void addFirstList(List<T> list) {
        mDatas.addAll(0, list);
        notifyDataSetChanged();
    }

    public void addList(T t) {
        mDatas.add(t);
        notifyDataSetChanged();
    }

    public void removeList(T t) {
        mDatas.remove(t);
        notifyDataSetChanged();
    }

    public void removeList(int t) {
        mDatas.remove(t);
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType,
                                                    ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(RecyclerView.ViewHolder holder, T t, int position);
//        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(RecyclerView.ViewHolder holder, T t, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
