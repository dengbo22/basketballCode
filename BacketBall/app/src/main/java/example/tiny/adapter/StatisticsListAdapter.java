package example.tiny.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import java.util.Arrays;
import java.util.Comparator;

import example.tiny.backetball.LiveDetailActivity;
import example.tiny.backetball.R;
import example.tiny.backetball.StatisticsFragment;

/**
 * Created by tiny on 15-9-1.
 */
public class StatisticsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = "StatisticsListAdapter";
    private final Context mContext;
    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private RecyclerView.Adapter mBaseAdapter;
    private SparseArray<Section> mSections = new SparseArray<Section>();
    private RecyclerView mRecyclerView;




    public StatisticsListAdapter(Context context, RecyclerView recyclerView) {

        mContext = context;
        mBaseAdapter = new SimpleAdapter(mContext);
        mRecyclerView = recyclerView;

        final GridLayoutManager layoutManager = (GridLayoutManager)(mRecyclerView.getLayoutManager());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(isSectionHeaderPosition(position))
                    return layoutManager.getSpanCount();
                else {
                    if( position <= SimpleAdapter.mPlayerSpan * SimpleAdapter.SCORE_HEIGHT)
                        return SimpleAdapter.mScoreSpan;

                    return SimpleAdapter.mPlayerSpan;
                }
            }
        });
    }


    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public SectionViewHolder(View view,int mTextResourceId) {
            super(view);
            title = (TextView) view.findViewById(mTextResourceId);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_statistics_header, parent, false);
            return new SectionViewHolder(view,R.id.tv_statistics_header);
        }else{
            return mBaseAdapter.onCreateViewHolder(parent, typeView -1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            ((SectionViewHolder)sectionViewHolder).title.setText(mSections.get(position).title);
        }else{
            Log.e(LOG_TAG, "position" + position + "\titem:" + sectionedPositionToPosition(position));
            mBaseAdapter.onBindViewHolder(sectionViewHolder,sectionedPositionToPosition(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) +1 ;
    }


    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }


    public void setSections(Section[] sections) {
        mSections.clear();

        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section o, Section o1) {
                return (o.firstPosition == o1.firstPosition)
                        ? 0
                        : ((o.firstPosition < o1.firstPosition) ? -1 : 1);
            }
        });

        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            mSections.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).firstPosition > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition >= sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }


    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - mSections.indexOfKey(position)
                : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (mValid ? mBaseAdapter.getItemCount() + mSections.size() : 0);
    }





}
