package com.quyuanjin.im2.greendao.greendaoacTest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quyuanjin.im2.R;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.greendao.table.Student;

import java.util.List;

public class GreenDaoAcTest extends AppCompatActivity {

    private TextView mText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greendao_test);
        Button mInsertBtn = findViewById(R.id.greendaoinsertdata);
        Button mCheckBtn = findViewById(R.id.greendaocheckdata);
        mText = findViewById(R.id.greendaotextview);


        mInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student = new Student();
                student.setAddress("address");
                student.setAge(14);
                student.setGrade("grad3");
                student.setStudentNo(13);
                student.setName("xiaoming");

                App.getDaoSession().getStudentDao().insert(student);
            }
        });
      /*  mCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Student> list = App.getDaoSession().getStudentDao().queryBuilder().list();

              Student s=null;
              String a =null;
                for (int i = 0; i < list.size(); i++) {
                     s=  list.get(i);
                    a= s.getGrade();
                }

                mText.setText(a);
            }
        });
*/

    }
}
