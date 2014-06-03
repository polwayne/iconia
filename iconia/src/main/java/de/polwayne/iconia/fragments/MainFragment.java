package de.polwayne.iconia.fragments;

import android.app.Fragment;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.capricorn.ArcMenu;

import de.polwayne.iconia.R;

/**
 * Created by Paul on 30.04.14.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private ArcMenu arcMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main,container,false);
        arcMenu = (ArcMenu) v.findViewById(R.id.arc_menu);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int[] icons = {R.drawable.head, R.drawable.clouds, R.drawable.vintage};
        initArcMenu(icons);
    }

    private void initArcMenu(int[] itemDrawables) {
        ((ImageView) arcMenu.findViewById(R.id.control_hint)).setImageResource(R.drawable.add);
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(getActivity());
            item.setImageResource(itemDrawables[i]);
            arcMenu.addItem(item, this);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
