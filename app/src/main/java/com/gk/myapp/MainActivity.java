package com.gk.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.gk.myapp.mydemo.DropDownAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> mList;
    List<String> filtered;
    EditText ed;
    ListView lvDropDown;
    DropDownAdapter dropDownAdapter;
//    PopupWindow popup;
//    TextView txtpopUp;
//    ScrollView popUp;
//    private TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIds();
        setListeners();
    }

    private void setListeners() {
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = ed.getText().toString();
                int n = a.lastIndexOf("@");
                if (a.contains("@")) {
                    getFileredList(a.substring(n + 1, a.length()));
                }else{
                    filtered.clear();
                    dropDownAdapter.notifyList(filtered);
                }
            }
        });
    }




    private void getIds(){
        ed= (EditText) findViewById(R.id.ed);
        mList=new ArrayList<>();
        filtered=new ArrayList<>();
        lvDropDown= (ListView) findViewById(R.id.lv_drop_down);
        dropDownAdapter=new DropDownAdapter(this,filtered);
        lvDropDown.setAdapter(dropDownAdapter);

        lvDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str=ed.getText().toString();
                ed.setText(str.substring(0,str.lastIndexOf("@")+1)+filtered.get(position));
                ed.setSelection(ed.getText().length());
                filtered.clear();
                dropDownAdapter.notifyList(filtered);
            }
        });

//        popUp= (ScrollView) findViewById(R.id.dropdown);
//        txt = (TextView)findViewById(R.id.txt);



//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, filtered);
//        ed.setThreshold(1);
//        ed.setAdapter(adapter);

        mList.add("January");
        mList.add("February");
        mList.add("March");
        mList.add("April");
        mList.add("May");
        mList.add("June");
        mList.add("July");
        mList.add("August");
        mList.add("September");
        mList.add("October");
        mList.add("November");
        mList.add("December");

    }



//    public void popupInit() {
//        // Inflate the popup_layout.xml
//        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.lin_popup);
//        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
//
//        // Creating the PopupWindow
//        popup = new PopupWindow(this);
//        popup.setContentView(layout);
//        popup.setWidth(200);
//        popup.setHeight(400);
//        popup.setFocusable(true);
//        txtpopUp= (TextView) layout.findViewById(R.id.txt);
//        String s="";
//        for(String o:filtered)
//            s=s+o+"\n";
//        txtpopUp.setText(s);
//
//
//        popup.showAtLocation(layout, Gravity.NO_GRAVITY,10,200);
//    }


//    private void setPopUpValues(){
//        String s="";
//        for(String o:filtered)
//            s=s+o+"\n";
//        txt.setText(s);
//        if(s.length()>0)
//            popUp.setVisibility(View.VISIBLE);
//        else
//            popUp.setVisibility(View.INVISIBLE);
//    }



    private void getFileredList(String substring) {
        filtered.clear();
        for(String obj:mList){
            if(obj.toLowerCase().contains(substring.toLowerCase())){
                filtered.add(obj);
            }
        }

//        if (popup==null)
//            popupInit();
//        else
//            setPopUpValues();

        dropDownAdapter.notifyList(filtered);
        for(String o:filtered)
            Log.e("mylist",o );
    }
}
