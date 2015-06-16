package ch.heigvd.wordhunt.Interaction.PageSlider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.heigvd.wordhunt.design.R;


public class ScreenSlidePageFragment extends Fragment {

    TextView textContent;
    LinearLayout widget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        View rootView =  inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        textContent = (TextView) rootView.findViewById(R.id.textContent);
        widget = (LinearLayout) rootView.findViewById(R.id.widget);
        return rootView;
    }

}
