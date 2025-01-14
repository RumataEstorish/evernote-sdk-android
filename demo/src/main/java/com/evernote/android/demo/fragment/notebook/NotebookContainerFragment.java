package com.evernote.android.demo.fragment.notebook;

import androidx.fragment.app.FragmentTransaction;

import com.evernote.android.demo.fragment.AbstractContainerFragment;
import com.evernote.android.demo.fragment.EmptyFragment;
import com.evernote.android.demo.task.FindNotebooksTask;
import com.evernote.android.demo.util.ViewUtil;
import com.evernote.client.android.tasks.TaskResult;
import com.evernote.demo.R;
import com.evernote.edam.type.Notebook;

import java.util.List;

/**
 * @author rwondratschek
 */
public class NotebookContainerFragment extends AbstractContainerFragment {

    @Override
    protected void loadData() {
        new FindNotebooksTask().start(this, "personal");
    }

    @Override
    public void onFabClick() {
        new CreateNotebookDialogFragment().show(getChildFragmentManager(), CreateNotebookDialogFragment.TAG);
    }

    @TaskResult(id = "personal")
    public void onFindNotebooks(List<Notebook> notebooks) {
        mSwipeRefreshLayout.setRefreshing(false);

        if (notebooks == null || notebooks.isEmpty()) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, EmptyFragment.create("notebooks"))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NotebookListFragment.create(notebooks))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    @TaskResult
    public void onCreateNewNotebook(Notebook notebook) {
        // called from CreateNoteDialogFragment
        if (notebook != null) {
            refresh();
        } else {
            ViewUtil.showSnackbar(mSwipeRefreshLayout, "Create notebook failed");
        }
    }
}
