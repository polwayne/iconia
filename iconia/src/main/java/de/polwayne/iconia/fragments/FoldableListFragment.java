package de.polwayne.iconia.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.UnfoldableView;

import de.polwayne.iconia.R;
import de.polwayne.iconia.adapter.ItemsAdapter;
import de.polwayne.iconia.database.DatabaseTables;
import de.polwayne.iconia.database.IconiaProvider;
import de.polwayne.iconia.database.ItemDAO;
import de.polwayne.iconia.pojo.Item;

/**
 * Created by Paul on 29.05.14.
 */
public class FoldableListFragment extends Fragment implements AdapterView.OnItemClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    private ListView mListView;
    private View mListTouchInterceptor;
    private View mDetailsLayout;
    private ImageView detailsImage;
    private TextView detailsTitle;
    private TextView detailsStory;
    private UnfoldableView mUnfoldableView;
    private ItemDAO itemDAO;
    private ItemsAdapter mAdapter;
    private long lastItemId = -1;
    private int currentCategory = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_foldable_list,container,false);
        mListView = (ListView) v.findViewById(R.id.list_view);
        mListTouchInterceptor = v.findViewById(R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        mDetailsLayout = v.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);
        detailsImage = (ImageView) mDetailsLayout.findViewById(R.id.details_image);
        detailsTitle = (TextView) mDetailsLayout.findViewById(R.id.details_title);
        detailsStory = (TextView) mDetailsLayout.findViewById(R.id.details_text);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"IndieFlower.ttf");
        detailsTitle.setTypeface(tf);
        detailsStory.setTypeface(tf);

        mUnfoldableView = (UnfoldableView) v.findViewById(R.id.unfoldable_view);
        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
                mDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                setHasOptionsMenu(true);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
                setHasOptionsMenu(false);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                mDetailsLayout.setVisibility(View.INVISIBLE);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemDAO = new ItemDAO(getActivity());
        mAdapter = new ItemsAdapter(getActivity(),R.layout.list_item,null,new String[]{DatabaseTables.ItemColumns.TITLE},new int[]{R.id.list_item_title});
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        getLoaderManager().initLoader(currentCategory,null,this);
    }

    public boolean onBackPressed(){
        if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
            mUnfoldableView.foldBack();
            return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete_item,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_delete){
            onBackPressed();
            itemDAO.deleteItem(lastItemId);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lastItemId = id;
        ImageView iv_list = (ImageView) view.findViewById(R.id.list_item_image);
        detailsImage.setImageDrawable(iv_list.getDrawable());
        Item i = itemDAO.loadItem(id);
        detailsTitle.setText(i.title);
        detailsStory.setText(i.details);
        mUnfoldableView.unfold(iv_list,mDetailsLayout);
    }

    public void filterCategory(int category){
        currentCategory = category;
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl;
        if(currentCategory == -1)
         cl = new CursorLoader(getActivity(),IconiaProvider.ITEMS_URI, DatabaseTables.ItemColumns.ALL_COLLUMNS,null,null, DatabaseTables.ItemColumns.TIMESTAMP + " DESC");
        else{
            cl = new CursorLoader(getActivity(),IconiaProvider.ITEMS_URI, DatabaseTables.ItemColumns.ALL_COLLUMNS, DatabaseTables.ItemTbl.CATEGORY + "=?",new String[]{String.valueOf(currentCategory)}, DatabaseTables.ItemColumns.TIMESTAMP + " DESC");
        }
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
