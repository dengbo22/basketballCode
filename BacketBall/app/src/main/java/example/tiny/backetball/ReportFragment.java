package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * Created by tiny on 15-8-30.
 */
public class ReportFragment extends Fragment {
    private String mCompetitionId = null;
    private static final String LOG_TAG = "ReportFragment";
    private String mContent = null;
    private WebView mWebReportShow ;
    private boolean mHasReport = false;
    private ImageView mBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null)
            mCompetitionId = bundle.getString(LiveDetailActivity.BUNDLE_COMPETITION_KEY);
        if(mCompetitionId == null || "".equals(mCompetitionId)) {
            Log.e(LOG_TAG, "获取CompetitionId错误");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        mWebReportShow = (WebView) view.findViewById(R.id.web_report_show);
//        mWebReportShow.getSettings().setJavaScriptEnabled(true);
        mBackground = (ImageView) view.findViewById(R.id.background_image);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RequestReport();
    }

    public void RequestReport() {
        if(mCompetitionId != null) {
            AVQuery<AVObject> query = new AVQuery<>("Competition");
            query.include("reportId");
            query.whereEqualTo("objectId", mCompetitionId);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e == null) {
                        Toast.makeText(getActivity(), "加载数据成功!", Toast.LENGTH_SHORT).show();
                        if(list.size() == 1) {
                            AVObject report = list.get(0).getAVObject("reportId");
                            mContent = report.getString("content");
                            mWebReportShow.loadData(mContent,"text/html;charset=UTF-8",null);
                            mHasReport = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser)
            return;
        if(mHasReport) {
            mBackground.setVisibility(View.GONE);
            mWebReportShow.setVisibility(View.VISIBLE);
        }else {
            mBackground.setVisibility(View.VISIBLE);
            mWebReportShow.setVisibility(View.GONE);
        }


    }
}
