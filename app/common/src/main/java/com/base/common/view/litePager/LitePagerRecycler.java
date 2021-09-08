package com.base.common.view.litePager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.common.R;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.LogUtil;
import com.base.common.view.adapter.ada.BaseRVAdapter;
import com.base.common.view.adapter.connector.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class LitePagerRecycler extends LitePager {


    public LitePagerRecycler(Context context) {
        super(context);
    }

    public LitePagerRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LitePagerRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private BaseRVAdapter baseRVAdapter;
    private List list = new ArrayList();//数据源


    public void setDataList(List list) {
        if (this.list == list) return;

        if (list != null) {
            this.list = list;
        }
        if (baseRVAdapter != null) {
            baseRVAdapter.setDataList(list);

            int cou = baseRVAdapter.getChildCount();
            if (cou == 0) return;
            removeAllViews();
            if (cou < 3) {
                if (cou == 1) {
                    setSlide(false);
                    addView(createView(0, false));
                    addView(createView(0, false));
                    addView(createView(0, true));
                } else {
                    setSlide(true);
                    addView(createView(0, true));
                    addView(createView(0, false));
                    addView(createView(1, true));
                }
                if (getOnScrollListener() == onScrollListener)
                    removeOnScrollListener();
            } else if (cou == 3) {
                setSlide(true);
                addView(createView(0, true));
                addView(createView(2, true));
                addView(createView(1, true));
                if (getOnScrollListener() == onScrollListener)
                    removeOnScrollListener();
            } else {
                setSlide(true);
                int last = baseRVAdapter.getLastPosition();
                addView(createView(last, true));

                addView(createView(0, true));
                addView(createView(last - 1, true));
                addView(createView(last - 2, true));

                addView(createView(last - 3, true));
                addView(createView(3, true));
                addView(createView(2, true));

                addView(createView(1, true));

                setOnScrollListener(onScrollListener);

            }

            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(getChildAt(getChildCount() - 1));
            }

//            if (getChildCount() > 2) setSelection(getChildCount() - 2);
        }
    }

    public void setAdapterBaseRV(BaseRVAdapter baseRVAdapter) {
        this.baseRVAdapter = baseRVAdapter;
        baseRVAdapter.setDataList(list);
    }


    public <T> void update(int position, T bean) {
        if (bean == null) return;
        if (position >= 0 && position < list.size()) {
            list.set(position, bean);
            if (baseRVAdapter != null) {
                baseRVAdapter.setDataList(list);
            }
        }

        for (int i = 0; i < getChildCount(); i++) {
            Object tag = getChildAt(i).getTag(R.id.tag_first);
            if (tag == null) continue;
            int pos = JavaMethod.getInt(tag);
            if (pos == position) {
                BaseViewHolder viewHolder = (BaseViewHolder) getChildAt(i).getTag();
                if (viewHolder == null) return;
                viewHolder.itemView.setTag(R.id.tag_first, pos);
                viewHolder.onBindViewHolder(pos, baseRVAdapter.getItemBean(pos));
            }
        }


    }

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onStateChanged(int newState) {

            if (newState == STATE_IDLE) {
                if (getChildCount() == 0) return;
                View lastView = getChildAt(getChildCount() - 1);
                if (lastView.getLayoutParams() instanceof LayoutParams) {
                    LayoutParams lastParams = (LayoutParams) lastView.getLayoutParams();
                    if (lastParams.scale > mMiddleScale) {
                        //通过验证，直接返回
                        return;
                    }
                }
                //未通过验证 挨个检查
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view.getLayoutParams() instanceof LayoutParams) {
                        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                        if (layoutParams.scale <= mTopScale && layoutParams.scale > mMiddleScale) {
//                            setTopOrder(i);
                            LogUtil.d("tag_first  ", "第" + (i) + "个状态不对，进行调整" + layoutParams.from + "  " + layoutParams.to);
                            break;
                        }
                    }
                }
            }

            if (getChildCount() != 8) return;
            if (baseRVAdapter == null) return;
            int lastPos = baseRVAdapter.getLastPosition();

            if (newState == STATE_SETTLING_LEFT || newState == STATE_SETTLING_TOP) {
                View view = getChildAt(5);
                Object tag = view.getTag(R.id.tag_first);
                if (tag == null) return;
                int pos = JavaMethod.getInt(tag);
                if (pos + 1 <= lastPos) {
                    pos = pos + 1;
                } else {
                    pos = 0;
                }
                LogUtil.d("tag_first 向左 ", String.valueOf(pos) + "  " + newState);


                //第五个
                BaseViewHolder viewHolder4 = (BaseViewHolder) getChildAt(4).getTag();
                if (viewHolder4 == null) return;
                viewHolder4.itemView.setTag(R.id.tag_first, pos);
                viewHolder4.onBindViewHolder(pos, baseRVAdapter.getItemBean(pos));

                //第四个
                int posOther = pos;
                if (posOther + 1 <= lastPos) {
                    posOther = posOther + 1;
                } else {
                    posOther = 0;
                }

                View view1 = getChildAt(3);
                BaseViewHolder viewHolder3 = (BaseViewHolder) view1.getTag();
                if (viewHolder3 == null) return;
                viewHolder3.itemView.setTag(R.id.tag_first, posOther);
                viewHolder3.onBindViewHolder(posOther, baseRVAdapter.getItemBean(posOther));


            } else if (newState == STATE_SETTLING_RIGHT || newState == STATE_SETTLING_BOTTOM) {
                View view = getChildAt(2);
                Object tag = view.getTag(R.id.tag_first);
                if (tag == null) return;
                int pos = JavaMethod.getInt(tag);
                if (pos - 1 >= 0) {
                    pos = pos - 1;
                } else {
                    pos = lastPos;
                }
                LogUtil.d("tag_first 向右 ", String.valueOf(pos));

                BaseViewHolder viewHolder2 = (BaseViewHolder) getChildAt(3).getTag();
                if (viewHolder2 == null) return;
                viewHolder2.itemView.setTag(R.id.tag_first, pos);
                viewHolder2.onBindViewHolder(pos, baseRVAdapter.getItemBean(pos));


                int posOther = pos;
                if (posOther - 1 >= 0) {
                    posOther = posOther - 1;
                } else {
                    posOther = lastPos;
                }
                //要改变的是第三个
                View view1 = getChildAt(4);
                BaseViewHolder viewHolder3 = (BaseViewHolder) view1.getTag();
                if (viewHolder3 == null) return;
                viewHolder3.itemView.setTag(R.id.tag_first, posOther);
                viewHolder3.onBindViewHolder(posOther, baseRVAdapter.getItemBean(posOther));


            }


        }
    };


    public BaseRVAdapter getBaseRVAdapter() {
        return baseRVAdapter;
    }

    private View createView(int position, boolean isShow) {
        BaseViewHolder viewHolder = baseRVAdapter.onCreateViewHolder(this, baseRVAdapter.getItemViewType(position));
        viewHolder.onBindViewHolder(position, baseRVAdapter.getItemBean(position));
        viewHolder.itemView.setTag(R.id.tag_first, position);
        viewHolder.itemView.setTag(viewHolder);
        if (isShow) {
            viewHolder.itemView.setVisibility(VISIBLE);
        } else {
            viewHolder.itemView.setVisibility(INVISIBLE);
        }

        return viewHolder.itemView;
    }


}
