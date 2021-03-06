package com.incool.meski_android.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.incool.meski_android.MainActivity;
import com.incool.meski_android.R;
import com.incool.meski_android.view.activity.EstateDetailActivity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jacketjie.common.libray.adapter.CommonAdapter;
import jacketjie.common.libray.adapter.ViewHolder;
import jacketjie.common.libray.custom.view.animatedlayout.AnimatedLayoutListener;
import jacketjie.common.libray.custom.view.animatedlayout.DrawableLinearLayout;
import jacketjie.common.libray.others.ToastUtils;


public class SecondFragment extends Fragment {
    private View view;
    private ListView secondListView;
    private DrawableLinearLayout drawableLinearLayout;
    private GridView mSelections;
    private ImageView bg;
    private View mSelectedOne, mSelectedTwo, mSelectedThree, mSelectedFour;
    private List<TopTab> topTabs;

    private int TAB_SELECTED_COLOR = Color.RED;
    private int TAB_DEFAULT_COLOR = Color.parseColor("#666666");

    private String[] titles = {"优美散文", "短篇小说", "美文日赏", "青春碎碎念", "左岸阅读", "慢文艺", "诗歌精选", "经典语录"};
    private int lastItemClickPos = -1;
    private int lastItemClickPos1 = -1;
    private int lastItemClickPos2 = -1;
    private int lastItemClickPos3 = -1;
    private int lastItemClickPos4 = -1;

    private List<String> mSelectionList;
    private String[] mDatas1 = {"全部区域", "长宁区", "宝山区", "杨浦区", "闸北区", "虹口区", "浦东新区"};
    private String[] mDatas2 = {"不限价格", "500以下", "500—2000", "2000—5000", "5000以上"};
    private String[] mDatas3 = {"所有类型", "官方推荐", "类型一", "类型二", "类型三", "类型四", "类型五"};
    private String[] mDatas4 = {"推荐排序", "智能排序", "按距离", "按价格", "按习惯"};
    private GridViewAdapter gridViewAdapter;
    //    private View top1;
    private SwipeRefreshLayout pullToRefresh;
    private int currentTabClickPos;
    private MyHandler handler;

    private static final int ANIMATION_CODE = 0x147;
    private View tabs;

    public interface OnSecondFragmentTouchEventListener {
        void setFragmentInstance(SecondFragment fragment);
    }

    protected OnSecondFragmentTouchEventListener onSecondFragmentTouchEventListener;


    private class MyHandler extends Handler {
        private SoftReference<SecondFragment> reference;

        public MyHandler(SecondFragment secondFragment) {
            reference = new SoftReference<SecondFragment>(secondFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SecondFragment fragment = reference.get();
            if (fragment != null) {
                switch (msg.what) {
                    case ANIMATION_CODE:
                        int pos = msg.arg1;
                        displayOrHidden(topTabs.get(pos));
                        break;

                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if ((getActivity() instanceof OnSecondFragmentTouchEventListener)) {
            onSecondFragmentTouchEventListener = (OnSecondFragmentTouchEventListener) getActivity();
            onSecondFragmentTouchEventListener.setFragmentInstance(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.second_fragment_layout, container, false);
            initView();
            initData();
            setEventListener();
        }
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        secondListView = (ListView) view.findViewById(R.id.id_firstListView);
        drawableLinearLayout = (DrawableLinearLayout) view.findViewById(R.id.id_drawer_layout);
        mSelections = (GridView) view.findViewById(R.id.id_mSelections);
        bg = (ImageView) view.findViewById(R.id.id_bg);
//        top1 = view.findViewById(R.id.id_top1);
        tabs = ((MainActivity) getActivity()).getSecondTopTabs();
        TopTab tabOne = new TopTab(R.id.id_mSelectedOneText, R.id.id_mSelectedOneImage, R.id.id_mSelectedOne, tabs);
        tabOne.setTag(MainActivity.FIRST_FRAGMENT);
        TopTab tabTwo = new TopTab(R.id.id_mSelectedTwoText, R.id.id_mSelectedTwoImage, R.id.id_mSelectedTwo, tabs);
        tabTwo.setTag(MainActivity.SECOND_FRAGMENT);
        TopTab tabThree = new TopTab(R.id.id_mSelectedThreeText, R.id.id_mSelectedThreeImage, R.id.id_mSelectedThree, tabs);
        tabThree.setTag(MainActivity.THIRD_FRAGMENT);
        TopTab tabFour = new TopTab(R.id.id_mSelectedFourText, R.id.id_mSelectedFourImage, R.id.id_mSelectedFour, tabs);
        tabFour.setTag(MainActivity.FORTH_FRAGMENT);
        topTabs = new ArrayList<>();
        topTabs.add(tabOne);
        topTabs.add(tabTwo);
        topTabs.add(tabThree);
        topTabs.add(tabFour);

        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.id_pullToRefresh);
        pullToRefresh.setColorSchemeColors(R.color.colorPrimary);
    }

    private void initData() {
        mSelectionList = new ArrayList<>();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, Arrays.asList(titles));
//        secondListView.setAdapter(adapter);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add("s");
        }
        ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), list, R.layout.second_list_item);
        secondListView.setAdapter(listViewAdapter);

        gridViewAdapter = new GridViewAdapter(getActivity(), mSelectionList, R.layout.grid_item);
        mSelections.setAdapter(gridViewAdapter);

        handler = new MyHandler(this);

    }

    private void setEventListener() {
        /**
         * 顶部Tab点击事件
         */
        tabs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float eventX = (Float) event.getRawX();
                        float eventY = (Float) event.getRawY();
                        for (int i = 0; i < topTabs.size(); i++) {
                            Rect rect = new Rect();
                            TopTab tab = topTabs.get(i);
                            tab.getFrameLayout().getGlobalVisibleRect(rect);
                            if (rect.contains((int) eventX, (int) eventY)) {
                                currentTabClickPos = i;
                                createTopTabAnimation(tab);
                                return true;
                            }
                        }
                        break;
                }
                return true;
            }
        });
        /**
         * 下拉选框点击事件
         */
        mSelections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopTab tab = topTabs.get(currentTabClickPos);
                tab.setLastSelectedPos(position);
                gridViewAdapter.setLastSelectedPos(position);
                displayOrHidden(tab);
            }
        });
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        pullToRefresh.setRefreshing(false);
                    }
                }.execute("");
            }
        });

        secondListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EstateDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_no_move);
            }
        });

    }

    /**
     * 为Tab设置下拉动画
     *
     * @param tab
     */
    private void createTopTabAnimation(TopTab tab) {
        Message msg = Message.obtain();
        msg.what = ANIMATION_CODE;
        if (currentTabClickPos == lastItemClickPos) {
            msg.arg1 = (Integer) tab.getTag();
            handler.sendMessage(msg);
        } else {
            if (-1 != lastItemClickPos && drawableLinearLayout.isExpandableStatus()) {
                msg.arg1 = (Integer) topTabs.get(lastItemClickPos).getTag();
                handler.sendMessage(msg);
                msg.arg1 = (Integer) topTabs.get(currentTabClickPos).getTag();
                resetTab();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetData();
                        Message message = Message.obtain();
                        message.what = ANIMATION_CODE;
                        message.arg1 = currentTabClickPos;
                        handler.sendMessage(message);
                    }
                }, drawableLinearLayout.getDuration());
            } else {
                resetTab();
                resetData();
                msg.arg1 = currentTabClickPos;
                handler.sendMessage(msg);
            }
        }


        lastItemClickPos = currentTabClickPos;
    }

    private void resetTab(){
        mSelectionList.clear();
        switch (currentTabClickPos) {
            case MainActivity.FIRST_FRAGMENT:
                mSelectionList.addAll(Arrays.asList(mDatas1));
                break;
            case MainActivity.SECOND_FRAGMENT:
                mSelectionList.addAll(Arrays.asList(mDatas2));
                break;
            case MainActivity.THIRD_FRAGMENT:
                mSelectionList.addAll(Arrays.asList(mDatas3));
                break;
            case MainActivity.FORTH_FRAGMENT:
                mSelectionList.addAll(Arrays.asList(mDatas4));
                break;
        }
    }


    /**
     * 重置数据
     */
    private void resetData() {
        drawableLinearLayout.requestLayoutByAnim();
//        mSelectionList.clear();
//        switch (currentTabClickPos) {
//            case MainActivity.FIRST_FRAGMENT:
//                mSelectionList.addAll(Arrays.asList(mDatas1));
//                break;
//            case MainActivity.SECOND_FRAGMENT:
//                mSelectionList.addAll(Arrays.asList(mDatas2));
//                break;
//            case MainActivity.THIRD_FRAGMENT:
//                mSelectionList.addAll(Arrays.asList(mDatas3));
//                break;
//            case MainActivity.FORTH_FRAGMENT:
//                mSelectionList.addAll(Arrays.asList(mDatas4));
//                break;
//        }
        gridViewAdapter.setLastSelectedPos(topTabs.get(currentTabClickPos).getLastSelectedPos());
    }

//    /**
//     * 为Tab设置下拉动画
//     *
//     * @param tab
//     */
//    private void setAnimByForTab(final TopTab tab) {
//        if (pullToRefresh.isRefreshing()) {
//            return;
//        }
////        int pos = (int) tab.getTag();
//        if (currentTabClickPos == lastItemClickPos) {
//            displayOrHidden(tab);
//        } else {
//            if (drawableLinearLayout.isExpandableStatus()) {
//                displayOrHidden(topTabs.get(lastItemClickPos));
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        reBindData(tab);
//                    }
//                }, drawableLinearLayout.getDuration() + 50);
//            } else {
//                reBindData(tab);
//            }
//        }
//        lastItemClickPos = currentTabClickPos;
//    }
//
//    private void reBindData(TopTab tab) {
//        drawableLinearLayout.requestLayoutByAnim();
//        int pos = (int) tab.getTag();
//        mSelectionList.clear();
//
//        switch (pos) {
//            case 0:
//                mSelectionList.addAll(Arrays.asList(mDatas1));
//                break;
//            case 1:
//                mSelectionList.addAll(Arrays.asList(mDatas2));
//                break;
//            case 2:
//                mSelectionList.addAll(Arrays.asList(mDatas3));
//                break;
//            case 3:
//                mSelectionList.addAll(Arrays.asList(mDatas4));
//                break;
//        }
//        gridViewAdapter.setLastSelectedPos(tab.getLastSelectedPos());
//        displayOrHidden(tab);
//    }

    /**
     * 设置动画
     */
    private void displayOrHidden(TopTab tab) {
//        final TopTab tab = topTabs.get(currentTabClickPos);
        if (!drawableLinearLayout.isExpandableStatus()) {
            setShadowsAnimation(drawableLinearLayout.getDuration(), 0, 104);
            setAngleAnim(true, tab, drawableLinearLayout.getDuration(), 0, 180);
        } else {
            setShadowsAnimation(drawableLinearLayout.getDuration(), 104, 0);
            setAngleAnim(true, tab, drawableLinearLayout.getDuration(), 180, 0);
        }
        drawableLinearLayout.displayOrHidden();
        drawableLinearLayout.setAnimatedLayoutListener(new AnimatedLayoutListener() {
            @Override
            public void animationCreated() {
            }

            @Override
            public void animationEnded() {
                checkTabStatus();
            }
        });
        int pos = tab.getLastSelectedPos();
        if (-1 != pos) {
            if (pos < mSelectionList.size())
                tab.setText(mSelectionList.get(pos));
        }
    }

    /**
     * 防止状态出错
     */
    private void checkTabStatus() {
        for (int i = 0; i < topTabs.size(); i++) {
            TopTab tab = topTabs.get(i);
            if (drawableLinearLayout.isExpandableStatus() ) {
                bg.setBackgroundColor(Color.parseColor("#67000000"));
                if (currentTabClickPos != (Integer) tab.getTag()) {
                    tab.getTextView().setTextColor(TAB_DEFAULT_COLOR);
                    tab.setImageRes(R.drawable.icon_angle_light_black);
                    tab.getImageView().setRotation(0);
                } else {
                    tab.getTextView().setTextColor(TAB_SELECTED_COLOR);
                    tab.setImageRes(R.drawable.icon_angle_red);
                    tab.getImageView().setRotation(180);
                }
            }else{
                bg.setBackgroundColor(Color.TRANSPARENT);
                tab.getTextView().setTextColor(TAB_DEFAULT_COLOR);
                tab.setImageRes(R.drawable.icon_angle_light_black);
                tab.getImageView().setRotation(0);
            }

        }
    }

    /**
     * 设置背景
     *
     * @param duration
     * @param startAlpha
     * @param endAlpaha
     */
    private void setShadowsAnimation(int duration, final int startAlpha, final int endAlpaha) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startAlpha, endAlpaha);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                String alpha = Integer.toHexString(value);
                String color = String.format("#%s000000", alpha.length() < 2 ? String.format("0%s", alpha) : alpha);
//                Log.d("SelectorLayout", "color:" + color);
                bg.setBackgroundColor(Color.parseColor(color));
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
//                bg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (endAlpaha == 0) {
//                    bg.setVisibility(View.GONE);
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 设置三角形的动画
     */
    private void setAngleAnim(boolean moveToUp, final TopTab tab, int duration, int start, int stop) {
        ValueAnimator animator = null;

        animator = ObjectAnimator.ofFloat(tab.getImageView(), View.ROTATION, start, stop);
        animator.setDuration(duration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (drawableLinearLayout.isExpandableStatus()) {
                    tab.getImageView().setImageResource(R.drawable.icon_angle_red);
                    tab.getTextView().setTextColor(TAB_SELECTED_COLOR);
                } else {
                    tab.getImageView().setImageResource(R.drawable.icon_angle_light_black);
                    tab.getTextView().setTextColor(TAB_DEFAULT_COLOR);
                }
            }
        });
        animator.start();
    }

    /**
     * \
     * 处理点击事件
     *
     * @param ev
     * @return
     */
    public boolean onTouchEvent(MotionEvent ev) {
        int eX = (int) ev.getX();
        int eY = (int) ev.getY();
        Rect rect = new Rect();
        drawableLinearLayout.getLocalVisibleRect(rect);
        int[] position = new int[2];
        drawableLinearLayout.getLocationOnScreen(position);
        rect.left = rect.left + position[0];
        rect.right = rect.right + position[0];
        rect.top = rect.top + position[1];
        rect.bottom = rect.bottom + position[1];

        Rect rect1 = new Rect();
        View tabs = ((MainActivity) getActivity()).getSecondTopTabs();
        tabs.getLocalVisibleRect(rect1);
        int[] position1 = new int[2];
        tabs.getLocationOnScreen(position1);
        rect1.left = rect1.left + position1[0];
        rect1.right = rect1.right + position1[0];
        rect1.top = rect1.top + position1[1];
        rect1.bottom = rect1.bottom + position1[1];

//
//        if (drawableLinearLayout.isExpandableStatus() && eY > rect.bottom) {
//            displayOrHidden();
//            return true;
//        }
        if (!rect.contains(eX, eY) && !rect1.contains(eX, eY) && drawableLinearLayout.isExpandableStatus()) {
            displayOrHidden(topTabs.get(currentTabClickPos));
            return true;
        }

        return false;
    }

    /**
     * 拦截返回事件
     *
     * @return
     */
    public boolean onBackPressed() {
        if (drawableLinearLayout.isExpandableStatus()) {
            displayOrHidden(topTabs.get(currentTabClickPos));
            return true;
        }
        return false;
    }


    /**
     * GridView的适配器
     */
    class GridViewAdapter extends CommonAdapter<String> {
        private int lastSelectedPos = -1;

        public GridViewAdapter(Context context, List<String> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, String item) {
            TextView tv = helper.getView(R.id.grid_item);
            tv.setText(item);
            if (lastSelectedPos == helper.getPosition()) {
                tv.setBackgroundResource(R.drawable.grid_item_selected);
                tv.setTextColor(Color.WHITE);
            } else {
                tv.setBackgroundResource(R.drawable.grid_item_default);
                tv.setTextColor(TAB_DEFAULT_COLOR);
            }
        }

        public void setLastSelectedPos(int lastSelectedPos) {
            this.lastSelectedPos = lastSelectedPos;
            notifyDataSetChanged();
        }

    }

    /**
     * listView的适配器
     */
    class ListViewAdapter extends CommonAdapter<String> {

        public ListViewAdapter(Context context, List<String> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, String item) {

        }
    }


    class TopTab {
        private TextView textView;
        private ImageView imageView;
        private FrameLayout frameLayout;
        private Object tag;
        private int lastSelectedPos = -1;

        public TopTab() {
        }

        public TopTab(int tvRes, int ivRes, int framRes, View view) {
            this.textView = (TextView) view.findViewById(tvRes);
            this.imageView = (ImageView) view.findViewById(ivRes);
            this.frameLayout = (FrameLayout) view.findViewById(framRes);
        }

        public void setText(String text) {
            this.textView.setText(text);
        }

        public void setImageRes(int res) {
            this.imageView.setImageResource(res);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public FrameLayout getFrameLayout() {
            return frameLayout;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public Object getTag() {
            return tag;
        }

        public int getLastSelectedPos() {
            return lastSelectedPos;
        }

        public void setLastSelectedPos(int lastSelectedPos) {
            this.lastSelectedPos = lastSelectedPos;
        }
    }

}
