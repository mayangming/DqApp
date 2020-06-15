package com.wd.daquan.chat.group.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.chat.group.adapter.holder.SearchMemberAdapter;
import com.wd.daquan.model.bean.Friend;
import com.da.library.tools.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/4/20 10:34.
 * @description: 搜索组员界面
 */
public class SearchMemberView extends RelativeLayout implements View.OnClickListener {

    private Context mContext = null;
    private ArrayList<GroupMemberBean> mList;

    private EditText mEditText;
    private ImageView mCleanTv;
    private ListView mListView = null;
    private SearchMemberAdapter mAdapter = null;
    private ImageView mSearchBackIv;
    private View mNoDataTv;
    private View mSearchHintTv;

    public SearchMemberView(Context context) {
        super(context);
        init(context);
    }

    public SearchMemberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchMemberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setNickName(String nickName) {
        if (null != mAdapter) {
            mAdapter.setNickName(nickName);
        }
    }

    public void setData(ArrayList<GroupMemberBean> list) {
        this.mList = list;
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.cn_search_member_activity, this);
        setVisibility(View.GONE);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mAdapter = new SearchMemberAdapter(mContext);
        mListView = this.findViewById(R.id.search_member_lsv);
        mEditText = this.findViewById(R.id.search_input_et);
        mCleanTv = this.findViewById(R.id.search_clear_iv);
        mSearchBackIv = this.findViewById(R.id.search_back_iv);
        mNoDataTv = findViewById(R.id.no_data_tv);
        mSearchHintTv = findViewById(R.id.search_hint_tv);

        mListView.setAdapter(mAdapter);
        mEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_DONE);
        mEditText.clearFocus();
        this.setOnClickListener(null);
    }

    private void initData() {

    }

    private void initListener() {
        mCleanTv.setOnClickListener(this);
        mSearchBackIv.setOnClickListener(this);
        mEditText.addTextChangedListener(mTextWatcher);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mSearchBackIv.getId()) {  // 取消
            if(null != mSearchMemberListener) {
                mSearchMemberListener.onFinish();
            }
        } else if (id == mCleanTv.getId()) {    // 清除
            mEditText.setText("");
        }
    }

    public void hideSoftInput() {
        Utils.hideSoftInput(mContext, mEditText);
        setVisibility(View.GONE);
    }

    public void showSoftInput() {
        setVisibility(View.VISIBLE);
        mEditText.requestFocus();
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 筛选数据
     * @return
     *  注：检索排序方式，好友备注、群昵称，个人昵称
     */
    private List<GroupMemberBean> filter(String content){
        List<GroupMemberBean> tmpList = new ArrayList<>();
        if (TextUtils.isEmpty(content)) {
            return tmpList;
        }

        if (null == mList || 0 >= mList.size()) {
            return tmpList;
        }


        for (GroupMemberBean member : mList) {
            if (null == member || TextUtils.isEmpty(member.getUid())) {
                continue;
            }

            String friendRemark = "";
            Friend friend = FriendDbHelper.getInstance().getFriend(member.getUid());
            if (null != friend) {
                friendRemark = friend.getRemarks();
            }

            String memberRemark = member.getRemarks();
            String name = member.getNickname();

            if ((!TextUtils.isEmpty(friendRemark) && friendRemark.contains(content))    // 好友备注
                    || (!TextUtils.isEmpty(memberRemark) && memberRemark.contains(content)) // 群昵称
                    || (!TextUtils.isEmpty(name) && name.contains(content)) ) {     // 个人昵称
                tmpList.add(member);
            }
        }
        return tmpList;
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GroupMemberBean groupMember = mAdapter.getItem(position);
            if (null != mSearchMemberListener && null != groupMember) {
                mSearchMemberListener.onItemClick(groupMember.uid);
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count > 0) {
                mCleanTv.setVisibility(View.VISIBLE);
            } else {
                mCleanTv.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = String.valueOf(s);
            List<GroupMemberBean> list = filter(content);

            mNoDataTv.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);

            if (TextUtils.isEmpty(content)) {
                mSearchHintTv.setVisibility(View.VISIBLE);
                mNoDataTv.setVisibility(View.GONE);
            } else {
                mSearchHintTv.setVisibility(View.GONE);
            }

            mAdapter.replace(list);
        }
    };

    private SearchMemberListener mSearchMemberListener = null;

    public void setSearchMemberListener(SearchMemberListener searchMemberListener) {
        mSearchMemberListener = searchMemberListener;
    }

    public interface SearchMemberListener {
       void  onFinish();

       void onItemClick(String userId);
    }
}
