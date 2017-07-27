package test.bwie.com.shejiaoapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.bwie.com.shejiaoapp.R;

/**
 * date: 2017/7/5
 * author:陈茹
 * 类的用途:
 */

public class IFragment extends Fragment {

    public IFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_i, container, false);
    }
}
