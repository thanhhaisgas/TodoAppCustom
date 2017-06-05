package com.example.haint.todocustom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.haint.data.DataTask;
import com.example.haint.model.TaskModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddingNewAndEditTask extends Fragment {

    View view;

    EditText edTaskName, edTaskNote;
    Spinner spPLevel, spStatus;
    DatePicker dueDate;

    ArrayAdapter<String> adapterPLevel, adapterStatus;
    ArrayList<String> listPLevel, listStatus;

    DataTask dataTask;

    int ID = -1;
    int checkEventAddOrEdit = -1;

    public AddingNewAndEditTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ID = bundle.getInt("idTask");
            checkEventAddOrEdit = 0;
        } else checkEventAddOrEdit = 1;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menuItemSearch).setVisible(false);
        menu.findItem(R.id.menuItemSorting).setVisible(false);
        menu.findItem(R.id.menuItemAdd).setVisible(false);
        menu.findItem(R.id.menuItemSave).setVisible(true);
        HomeTask.actionBar.setDisplayHomeAsUpEnabled(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemSave:
                if (checkEventAddOrEdit == 1){
                    if(edTaskName.getText().toString().isEmpty()){
                        Toast.makeText(view.getContext(),"Task name is not empty",Toast.LENGTH_SHORT).show();
                    }else {
                        saveTasktoDB();
                        goFragmentListTask();
                    }
                }else {
                    if(edTaskName.getText().toString().isEmpty()){
                        Toast.makeText(view.getContext(),"Task name is not empty",Toast.LENGTH_SHORT).show();
                    }else {
                        saveTasktoDB();
                        goFragmentListTask();
                    }
                }
                return true;
            case android.R.id.home:
                goFragmentListTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_adding_new_and_edit_task, container, false);
        dataTask = new DataTask(view.getContext());
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {

    }

    private void addControls() {
        edTaskName = (EditText) view.findViewById(R.id.edName);
        edTaskNote = (EditText) view.findViewById(R.id.edContent);
        spPLevel = (Spinner) view.findViewById(R.id.spPLevel);
        spStatus = (Spinner) view.findViewById(R.id.spStatus);
        dueDate = (DatePicker) view.findViewById(R.id.dueDate);

        listPLevel = new ArrayList<>();
        listPLevel.add("HIGH");
        listPLevel.add("MEDIUM");
        listPLevel.add("LOW");

        listStatus = new ArrayList<>();
        listStatus.add("TO-DO");
        listStatus.add("DONE");

        adapterPLevel = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, listPLevel);
        spPLevel.setAdapter(adapterPLevel);

        adapterStatus = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, listStatus);
        spStatus.setAdapter(adapterStatus);

        doEdit();
    }

    private void goFragmentListTask(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fmLinearlayout, new ListTask());
        transaction.commit();
    }

    private void saveTasktoDB(){
        String nameTask = "";
        String contentTask = "";
        int mDay = -1;
        int mMonth = -1;
        int mYear = -1;
        Integer priorityLevel = -1;
        Integer statusTask = -1;

        nameTask = edTaskName.getText().toString();
        contentTask = edTaskNote.getText().toString();
        mDay = dueDate.getDayOfMonth();
        mMonth = dueDate.getMonth();
        mYear = dueDate.getYear();
        if (spPLevel.getSelectedItem().toString().compareTo("HIGH")==0){
            priorityLevel = 0;
        }else {
            if (spPLevel.getSelectedItem().toString().compareTo("MEDIUM")==0){
                priorityLevel = 1;
            } else {
                priorityLevel = 2;
            }
        }
        if (spStatus.getSelectedItem().toString().compareTo("TO-DO")==0){
            statusTask = 0;
        } else  statusTask = 1;

        if(checkEventAddOrEdit == 1) {
            TaskModel temp = new TaskModel(0, nameTask, contentTask, mDay, mMonth, mYear, priorityLevel, statusTask);
            dataTask.addTask(temp);
        }
        else {
            TaskModel temp = new TaskModel(ID, nameTask, contentTask, mDay, mMonth, mYear, priorityLevel, statusTask);
            dataTask.updateTeask(temp);
        }
        //Toast.makeText(view.getContext(),nameTask+contentTask+mDay+mMonth+mYear+priorityLevel+statusTask,Toast.LENGTH_SHORT).show();
    }

    private void doEdit() {
        if (checkEventAddOrEdit == 0 ){
            TaskModel temp = new TaskModel();
            temp = dataTask.getTask(temp, ID);
            edTaskName.setText(temp.getNameTask());
            edTaskNote.setText(temp.getContentTask());
            dueDate.updateDate(temp.getmYear(),temp.getmMonth(),temp.getmDay());
            spPLevel.setSelection(temp.getPriorityLevel());
            spStatus.setSelection(temp.getStatusTask());
        }
    }
}
