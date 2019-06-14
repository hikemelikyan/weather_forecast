package weatherforcaster.doit.myweatherforcaster.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.adapter.TodayAdapter;


public class FirstFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "number";
    private static final String ARG_ADDRESS_STRING = "address";
    final String APP_ID = "ead6c284b33ca76b77085fb56365f06d";
    private TextView mPickedLocation;
    private boolean currentConnection = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TodayAdapter mAdapter;

    private RecyclerView mRecycler;

    public static FirstFragment newInstance(int page) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_first, container, false);

        return mView;
    }
}
