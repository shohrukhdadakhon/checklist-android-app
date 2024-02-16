package com.example.lagarichecklist;

import androidx.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Calendar;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.Manifest;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class checklist_pence extends AppCompatActivity implements MaddeEkleDialog.OnInputListener{
    private static final String TAG = "MainActivity";

    CheckedTextView text;
    EditText txt_input;

    @Override
    public void sendInput(String input, String inputNumara) {
        Log.d(TAG, "sendInput: got the input: " + input);

//        mInputDisplay.setText(input);
        checklist_list = addX(checklist_list.length, checklist_list, input);
        text = new CheckedTextView(this);
        text.setText((checklist_list.length) + ") " + checklist_list[checklist_list.length-1]);
        text.setChecked(false);
        text.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        text.setTextSize(20);
        text.setGravity(Gravity.LEFT);
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ch_text = addChTV(ch_text.length, ch_text, text);

        txt_input = new EditText(this);
        txt_input.setGravity(Gravity.LEFT);
        txt_input.setTextSize(15);
        txt_input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ch_input_txt = addEditTxt(ch_input_txt.length, ch_input_txt, txt_input);

        checked = addBool(checked.length, checked, false);


        addView(ch_text[ch_text.length-1], txt_input);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(text.isChecked()){
                    checked[checklist_list.length-1] = true;
                    text.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                    text.setChecked(false);
                }
                else {
                    checked[checklist_list.length-1] = false;
                    text.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                    text.setChecked(true);
                };
            }
        });


    }

    String[] checklist_list = {"Mekanik Check-List yapıldı", "Avyonik kutu takıldı", "Kanatlar takıldı", "GPS, Lidar, Pitot takıldı",
            "Soketler takıldı", "GPS1 Soketi", "Lidar Soketi", "Pitot Soketi", "Kanatlar, Kuyruk Soketi", "Power Module", "Servorail ve Payload Besleme",
            "Telemetri Anten", "Kamera Soketi", "Piller yerleştirildi. Kullanılan pillerin voltajları kontrol edildi", "Ağırlık Merkezi Kontrol edildi",
            "Kalkış öncesi son uçak ağırlığı", "Payload-Box sistemleri çalıştırıldı", "Pixhawk-Raspberry bağlantısı yapıldı", "Pixhawk ve Servorail beslemesi yapıldı",
            "Görev Yüklendi", "Sensor verileri kontrol edildi", "Telemetri", "UDP Haberleşme", "PİTOT", "Baş Açısı", "LİDAR", "SATCOUNT", "Kumanda test edildi",
            "Manuel mod kontol yüzeyleri test edildi", "FBWA mod tepkileri kontrol edildi", "Auto-Follow 'a giriş çıkışlar kontrol edildi", "Auto-Follow 'da kontrol yüzeyleri test edildi",
            "Parametreler kontrol edildi", "Sunucu ile bağlantı kuruldu", "Sunucu ile bağlantı kuruldu", "Görüntü işleme bilgisayarı ile bağlantı kuruldu",
            "Görüntü işleme bilgisayarında model kontrol edildi", "Görüntü işleme bilgisayarından gelen açı verisi kontrol  edildi", "Görüntü kaydı başlatıldı",
            "Pervane takıldı", "Motor - ESC takıldı", "ESC bağlantısı yapıldı", "Mission Planner üzerinden pil voltaj kontrolü yapıldı", "5 saniye tam gaz testi yapıldı",
            "Hava Aracı pist başına alındı", "Rüzgar açıısı ve hızı", "Telemetri sinyal gücü :", "GPS konumu kontrol edildi", "Gyro, Compass kontrol edildi",
            "Mission Planner ekranı kontrol edildi", "Gaz", "Elevator", "Aileron", "Rudder", "MOD Switch", "Otonom kalkış", "Otonom uçuş", "Otonom iniş",
            "Genel Notlar"};

    final int checklist_ln = checklist_list.length;
    boolean [] checked = new boolean[checklist_ln];
    CheckedTextView[] ch_text;
    EditText[] ch_input_txt;

    Button kaydetPDFbtn;
    Button maddeEklebtn;
    private String pdfText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_pence);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F9AA33"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<font color=\"#344955\">" + getString(R.string.app_name) + "</font>"));

        //LinearLayout MainLL = (LinearLayout) findViewById(R.id.linear_lay1);
        ch_text = new CheckedTextView[checklist_ln];
        ch_input_txt = new EditText[checklist_ln];

        for(int i=0; i<checklist_list.length; i++){
            ch_text[i] = new CheckedTextView(this);
            ch_text[i].setText((i+1) + ") " + (checklist_list[i]));
            ch_text[i].setChecked(false);
            ch_text[i].setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
            ch_input_txt[i] = new EditText(this);
            ch_input_txt[i].setGravity(Gravity.LEFT);
            ch_input_txt[i].setTextSize(15);

            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFFFF);
            border.setStroke(2, 0xFF000000);

            ch_text[i].setBackgroundDrawable(border);
            ch_text[i].setTextSize(20);
            ch_text[i].setGravity(Gravity.LEFT);

            ch_text[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ch_input_txt[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            //MainLL.addView(ch_text[i]);
            //MainLL.addView(ch_input_txt[i]);
            addView(ch_text[i], ch_input_txt[i]);

        }

        for(int x = 0; x< ch_text.length; x++){
            int finalX = x;
            ch_text[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(ch_text[finalX].isChecked()){
                        checked[finalX] = true;
                        ch_text[finalX].setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                        ch_text[finalX].setChecked(false);
                    }
                    else {
                        checked[finalX] = false;
                        ch_text[finalX].setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                        ch_text[finalX].setChecked(true);
                    };
                }
            });
        }

        kaydetPDFbtn = findViewById(R.id.kaydet_btn);
        kaydetPDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });

        maddeEklebtn = findViewById(R.id.madde_ekle_btn);
        maddeEklebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening dialog.");

                MaddeEkleDialog dialog = new MaddeEkleDialog();
                dialog.show(getSupportFragmentManager(), "MyCustomDialog");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static String[] addX(int nOfElements, String[] arr, String addedTxt){
        int i;
        String [] newArr = new String[nOfElements+1];

        for(i =0; i<nOfElements; i++){
            newArr[i] = arr[i];
        }
        newArr[nOfElements] = addedTxt;
        return newArr;
    }

    public static boolean[] addBool(int nOfElements, boolean[] arr, boolean addedBool){
        int i;
        boolean [] newBoolean = new boolean[nOfElements+1];

        for(i=0; i<nOfElements; i++){
            newBoolean[i]=arr[i];
        }
        newBoolean[nOfElements] = addedBool;
        return newBoolean;
    }

    public static CheckedTextView[] addChTV(int nOfElements, CheckedTextView[] arr, CheckedTextView addedChTV){
        int i;
        CheckedTextView[] newChTV = new CheckedTextView[nOfElements+1];

        for(i=0; i<nOfElements; i++){
            newChTV[i]=arr[i];
        }
        newChTV[nOfElements] = addedChTV;
        return newChTV;
    }

    public static EditText[] addEditTxt(int nOfElements, EditText[] arr, EditText addedEditTxt){
        int i;
        EditText[] newEditTxt = new EditText[nOfElements+1];

        for(i=0; i<nOfElements; i++){
            newEditTxt[i]=arr[i];
        }
        newEditTxt[nOfElements] = addedEditTxt;
        return newEditTxt;
    }

    public void addView(CheckedTextView ch_text, EditText ch_input_txt){
        LinearLayout MainLL = (LinearLayout) findViewById(R.id.linear_lay1);
        MainLL.addView(ch_text);
        MainLL.addView(ch_input_txt);
    }

    private void createPdf(){
        Date currentTime = Calendar.getInstance().getTime();
        pdfText = "CHECKLIST - " + currentTime.toString() + "\n\n";

        for (int x = 0; x < checklist_list.length; x++){
            pdfText += (x+1) + ") " + checklist_list[x] + ": " + checked[x] + ": " + ch_input_txt[x].getText().toString() + "\n";
        }

        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1000, 2000, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        TextPaint mTextPaint = new TextPaint();
        StaticLayout mTextLayout = new StaticLayout(pdfText, mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(100,100);
        mTextLayout.draw(canvas);
        canvas.restore();

        //canvas.drawText(pdfText, 80, 50, mTextPaint);

        // finish the page
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/lagari/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"checklist-" + currentTime + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }



}