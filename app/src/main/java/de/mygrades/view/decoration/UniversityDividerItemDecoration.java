package de.mygrades.view.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.mygrades.view.adapter.UniversitiesRecyclerViewAdapter;

public class UniversityDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;

    /**
     * Default divider will be used
     */
    public UniversityDividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public UniversityDividerItemDecoration(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            // do not show divider beneath header view
            if (parent.getChildViewHolder(child) instanceof UniversitiesRecyclerViewAdapter.HeaderViewHolder) {
                continue;
            }

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin + (int) child.getTranslationY();
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.setAlpha((int) (child.getAlpha() * 255));
            mDivider.draw(c);
        }
    }
}