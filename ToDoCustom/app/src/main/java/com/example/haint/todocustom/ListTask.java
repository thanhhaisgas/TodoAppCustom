package com.example.haint.todocustom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.haint.adapter.TaskAdapter;
import com.example.haint.data.DataTask;
import com.example.haint.model.TaskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListTask extends Fragment {

    ListView lvTasks;
    TaskAdapter taskAdapter;
    ArrayList<TaskModel> taskModelArrayList;
    DataTask dataTask;

    AddingNewAndEditTask addingNewAndEditTask;

    View view;

    public ListTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addingNewAndEditTask = new AddingNewAndEditTask();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem searchViewItem = menu.findItem(R.id.menuItemSearch);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.findItem(R.id.menuItemAdd).setVisible(false);
                menu.findItem(R.id.menuItemSorting).setVisible(false);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                menu.findItem(R.id.menuItemAdd).setVisible(true);
                menu.findItem(R.id.menuItemSorting).setVisible(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    taskAdapter.getFilter().filter("");
                    lvTasks.clearTextFilter();
                }else {
                    taskAdapter.getFilter().filter(newText.toString());
                }
                return false;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menuItemSave).setVisible(false);
        HomeTask.actionBar.setDisplayHomeAsUpEnabled(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemSearch:
                return true;
            case R.id.menuItemAdd:
                goFragmentAdd();
                return true;
            case R.id.menuSortName:
                Collections.sort(taskModelArrayList, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel o1, TaskModel o2) {
                        return o1.getNameTask().toLowerCase().toString().compareTo(o2.getNameTask().toLowerCase().toString());
                    }
                });
                taskAdapter.notifyDataSetChanged();
                return true;
            case R.id.menuSortPLevel:
                Collections.sort(taskModelArrayList, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel o1, TaskModel o2) {
                        return o1.getPriorityLevel().compareTo(o2.getPriorityLevel());
                    }
                });
                taskAdapter.notifyDataSetChanged();
                return true;
            case R.id.menuSortStatus:
                Collections.sort(taskModelArrayList, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel o1, TaskModel o2) {
                        return o1.getStatusTask().compareTo(o2.getStatusTask());
                    }
                });
                taskAdapter.notifyDataSetChanged();
                return true;
            case R.id.menuSortDate:
                Collections.sort(taskModelArrayList, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel o1, TaskModel o2) {
                        if(String.valueOf(o1.getmYear()).compareTo(String.valueOf(o2.getmYear())) == 0){
                            if(String.valueOf(o1.getmMonth()).compareTo(String.valueOf(o2.getmMonth())) == 0){
                                return String.valueOf(o1.getmDay()).compareTo(String.valueOf(o2.getmDay()));
                            } else {
                                return String.valueOf(o1.getmMonth()).compareTo(String.valueOf(o2.getmMonth()));
                            }
                        } else return String.valueOf(o1.getmYear()).compareTo(String.valueOf(o2.getmYear()));
                    }
                });
                taskAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goFragmentAdd(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fmLinearlayout, new AddingNewAndEditTask());
        transaction.commit();
    }

    private void goFragmentEdit(int ID){
        Bundle bundle = new Bundle();
        bundle.putInt("idTask", ID);
        addingNewAndEditTask.setArguments(bundle);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fmLinearlayout, addingNewAndEditTask);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_task, container, false);
        dataTask = new DataTask(view.getContext());
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int ID = taskModelArrayList.get(position).getID();
                dataTask.deleteTask(ID);
                taskModelArrayList.remove(position);
                taskAdapter.notifyDataSetChanged();
                return false;
            }
        });
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goFragmentEdit(taskModelArrayList.get(position).getID());
            }
        });
    }

    private void addControls() {
        lvTasks = (ListView) view.findViewById(R.id.lvTasks);
        taskModelArrayList = new ArrayList<>();
        taskModelArrayList = dataTask.getTasks(taskModelArrayList);
        taskAdapter = new TaskAdapter(getActivity(), view.getContext(), taskModelArrayList);
        lvTasks.setAdapter(taskAdapter);
    }

}
