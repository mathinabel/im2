package com.quyuanjin.im2.test;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quyuanjin.im2.R;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.xutilsDB.sqlite.SQLiteHelper;
import com.quyuanjin.im2.netty.xutilsDB.sqlite.ShareData;

public class SqliteTestAc extends AppCompatActivity implements View.OnClickListener {
    private final static String SWORD = "SWORD";
    //声明五个控件对象
    Button createDatabase = null;
    Button updateDatabase = null;
    Button insert = null;
    Button update = null;
    Button query = null;
    Button delete = null;


    private SharedPreferences sPreferences;
    private SharedPreferences.Editor editor;
    ShareData sData;
    private EditText creatTableText;
    private SQLiteHelper sqLiteHelper;
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source,
                                   int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) {
                Toast.makeText(SqliteTestAc.this, "表名不允许出现空格", Toast.LENGTH_SHORT).show();
                return "";
            } else if (source.toString().contentEquals("\n")) {
                Toast.makeText(SqliteTestAc.this, "表名不允许出现回车", Toast.LENGTH_SHORT).show();
                return "";
            } else return null;
        }
    };


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlite_ac);

        findViews();


    }

    private void findViews() {
        //根据控件ID得到控件
        creatTableText = this.findViewById(R.id.editText2);
        createDatabase = this.findViewById(R.id.createDatabase);
        updateDatabase = (Button) this.findViewById(R.id.updateDatabase);
        insert = (Button) this.findViewById(R.id.insert);
        update = (Button) this.findViewById(R.id.update);
        query = (Button) this.findViewById(R.id.query);
        delete = (Button) this.findViewById(R.id.delete);
        //添加监听器
        createDatabase.setOnClickListener(this);
        updateDatabase.setOnClickListener(this);
        insert.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);
        delete.setOnClickListener(this);

        //从ShareData中设置想要新建的表名
        sData = new ShareData();
        //实例化一个shared来保存数据
        sPreferences = (SharedPreferences) getSharedPreferences("MySQLiteVersion", MODE_PRIVATE);
        //使用Editor类来存放数据到SharedPreferences中
        editor = sPreferences.edit();
        /*
         * 禁止editText输入空格或者回车，因为数据库建表时不允许空格或者回车等特殊字符
         */
        creatTableText.setFilters(new InputFilter[]{filter});

    }

    SQLiteDatabase sqLiteDatabase;

    @Override
    public void onClick(View v) {
        //判断所触发的被监听控件，并执行命令
        switch (v.getId()) {
            //创建数据库
            case R.id.createDatabase:
                if (!creatTableText.getText().toString().isEmpty()) {   //如果表名非空，则建表，否则提示
                    //获取sharedPreferences中当前数据库版本号，并加1赋值给MySQLite，这样才能保证MySQLite会执行onUpgrade
                    //    int version=sPreferences.getInt("MySQLiteVersion",0);
                    //     version++;
                    sqLiteHelper = new SQLiteHelper(this, "SQLite", null
                            , 14);
                    //      Log.i("MainVersion",""+version);
                    sData.set(creatTableText.getText().toString());
                    sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                    //将数据库当前版本号存放到sharedPreferences中
                    //     editor.putInt("MySQLiteVersion",version);
                    //     editor.apply();
                } else {
                    Toast.makeText(this, "表名不能为空", Toast.LENGTH_SHORT).show();
                }
                break;


            //更新数据库
            case R.id.updateDatabase:
                sqLiteHelper = new SQLiteHelper(this, "QiuRuibo_SQLite", null
                        , 15);


                break;
            //插入数据
            case R.id.insert:
                sqLiteHelper.instertDate(sqLiteDatabase, sData.get(), "asdq1", "reeaedt3", "iuthrt4");
                ToastUtils.show(this, "插入成功");
                break;
            //更新数据信息
            case R.id.update:

                break;
            //查询信息
            case R.id.query:
             //   String s=sqLiteHelper.queryDateFromTable(sqLiteDatabase, "Name");
            //    creatTableText.setText(s);
                break;
            //删除记录
            case R.id.delete:
                sqLiteHelper.delete(sqLiteDatabase,"Name");
                ToastUtils.show(this, "删除成功");
                break;
            default:

                break;
        }
    }


}
