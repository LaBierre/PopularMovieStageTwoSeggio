package com.example.standard.popularmoviestagetwoseggio.dataFromDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.standard.popularmoviestagetwoseggio.R;

/**
 * Taken from Internet Source: I forgot to copy the source
 */

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH>
{
    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIDColumn;
    private Context context;


    public RecyclerViewCursorAdapter(Cursor cursor, Context context)
    {
        setHasStableIds(true);
        swapCursor(cursor);
        this.context = context;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindViewHolder(VH holder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH holder, int position)
    {
        if(!mDataValid){
            throw new IllegalStateException(context.getString(R.string.illegal_exeption_one));
        }
        if(!mCursor.moveToPosition(position)){
            throw new IllegalStateException(context.getString(R.string.illegal_exeptio_two) + position);
        }
        onBindViewHolder(holder, mCursor);
    }

    @Override
    public long getItemId(int position)
    {
        if(mDataValid && mCursor != null && mCursor.moveToPosition(position)){
            return mCursor.getLong(mRowIDColumn);
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount()
    {
        if(mDataValid && mCursor != null){
            return mCursor.getCount();
        }
        else{
            return 0;
        }
    }

    protected Cursor getCursor()
    {
        return mCursor;
    }

    public void changeCursor(Cursor cursor)
    {
        Cursor old = swapCursor(cursor);
        if(old != null){
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor)
    {
        if(newCursor == mCursor){
            return null;
        }
        Cursor oldCursor = mCursor;
        if(oldCursor != null){
            if(mDataSetObserver != null){
                oldCursor.unregisterDataSetObserver(mDataSetObserver);
            }
        }
        mCursor = newCursor;
        if(newCursor != null){
            if(mDataSetObserver != null){
                newCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIDColumn = newCursor.getColumnIndexOrThrow(context.getString(R.string.illegal_exeption_three));
            mDataValid = true;
            notifyDataSetChanged();
        }
        else{
            mRowIDColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }


    private DataSetObserver mDataSetObserver = new DataSetObserver()
    {
        @Override
        public void onChanged()
        {
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated()
        {
            mDataValid = false;
            notifyDataSetChanged();
        }
    };
}
