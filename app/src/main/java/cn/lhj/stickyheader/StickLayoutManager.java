package cn.lhj.stickyheader;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Filedescription.
 *
 * @author lihongjun
 * @date 2020-01-03
 */
public class StickLayoutManager extends LinearLayoutManager {

    private int mStickPosition = -1; // 吸顶position
    private View mStickView; // 吸顶布局

    public StickLayoutManager(Context context) {
        super(context);
    }

    public StickLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public StickLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置吸顶的position
     *
     * @param stickPosition
     */
    public void setStickPosition(int stickPosition) {
        mStickPosition = stickPosition;
    }

    /**
     * 设置吸顶的布局
     *
     * @param stickView
     */
    public void setStickView(View stickView) {
        setStickView(stickView,0);
    }

    /**
     * 设置吸顶的布局
     *
     * @param stickView
     * @param elevation 阴影值 dp
     */
    public void setStickView(View stickView,int elevation) {
        mStickView = stickView;
        if (stickView != null && elevation > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mStickView.getTag() != null) {
                    // 已经设置过阴影了
                    return;
                }
                mStickView.setTag(true);
                mStickView.animate().z(elevation);
            }
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scroll = super.scrollVerticallyBy(dy, recycler, state);
        if (Math.abs(scroll) > 0) {
            if (mStickPosition == -1) { // 如果列表中不存在二级分则返回
                hideStickView();
                return scroll;
            }
            changeFirstVisibilityStatu();
        }
        return scroll;
    }

    /**
     * 根据位置变化检测吸顶布局的显示状态
     */
    private void changeFirstVisibilityStatu() {
        if (findFirstVisibleItemPosition() < mStickPosition) {
            hideStickView();
        } else if (findFirstVisibleItemPosition() == mStickPosition) {
            View view = findViewByPosition(mStickPosition);
            if (view != null && view.getTop() < 0) {
                showStickView();
            } else {
                hideStickView();
            }
        } else {
            showStickView();
        }
    }

    /**
     * 隐藏吸顶布局
     */
    private void hideStickView() {
        if (mStickView != null && mStickView.getVisibility() != View.GONE) {
            mStickView.post(new Runnable() { // 如果父布局是ConstraintLayout会导致吸顶布局状态不对 用主线程队列去更新状态
                @Override
                public void run() {
                    mStickView.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 显示吸顶布局
     */
    private void showStickView() {
        if (mStickView != null && mStickView.getVisibility() != View.VISIBLE) {
            mStickView.post(new Runnable() {// 如果父布局是ConstraintLayout会导致吸顶布局状态不对 用主线程队列去更新状态
                @Override
                public void run() {
                    mStickView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        changeFirstVisibilityStatu();
    }
}
