package com.songwenju.aptproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.AutoCreat;
import com.example.Autowired;
import com.example.ZyaoAnnotation;
import com.songwenju.aptproject.bean.Author;
import com.songwenju.aptproject.pizza.Meal;
import com.zyao89.demoprocessor.auto.Zyao$$ZYAO;
import com.songwenju.aptproject.pizza.MealFactory;

@AutoCreat
@ZyaoAnnotation(
        name = "Zyao",
        text = "Hello welcome to China!!"
)
public class MainActivity extends AppCompatActivity {

    private Meal meal;
    private int sum = 10;

    @Autowired
    String bookName;

    @Autowired
    Author author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Zyao$$ZYAO zyao$$ZYAO = new Zyao$$ZYAO();
                String message = zyao$$ZYAO.getMessage();
                ((TextView)view).setText(message);
            }
        });

        //通过注解生成工厂类
        Meal meal = MealFactory.create("Tiramisu");
    }

    private void test1() {

    }

    public int test2(int sum2) {

        return sum2+10;
    }
}
