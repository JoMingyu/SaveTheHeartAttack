package com.example.jmg.savetheheartattack;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity {
    Location currentLocation = null;
    ImageButton reportbutton;
    ProgressBar progress;
    //
    // 버튼 터치 시 동작 순서 -> 다이얼로그 창 -> geoCoder -> sendSMS

    // 다이얼로그 창
    private void areYouReal() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

        alt_bld.setMessage("119로 현재 위치 정보가 전송됩니다.").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Action for 'YES' Button
                progress.setVisibility(progress.VISIBLE);
                if(currentLocation != null) {
                    sendSMS("01027640415", "" + findAddress(currentLocation.getLatitude(), currentLocation.getLongitude()) + "\n\n빠른 출동 부탁드립니다.");
                    Intent intent = new Intent(getApplicationContext(), StepOne.class); // stepOne 클래스로 인텐트
                    progress.setVisibility(progress.GONE);
                    startActivity(intent);
                } // if
            } // onClick
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Action for 'NO' Button
                dialog.cancel();
            } // onClick
        }); // setNegativeButton

        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("정말로 신고하시겠습니까?");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.warning);
        alert.show();
    } // areYouReal

    // SMS 전송
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "119에 신고 완료, 신속하게 진행해주세요", Toast.LENGTH_SHORT).show();
                        break;
                } // switch
            } // onReceive
        }, new IntentFilter(SENT)); // registerReceiver

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    } // sendSMS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        reportbutton = (ImageButton) findViewById(R.id.reportbutton);
        progress = (ProgressBar) findViewById(R.id.progressbar);

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria(); // Criteria 객체 생성
        criteria.setAccuracy(Criteria.NO_REQUIREMENT); // 배터리에 따라서 안 꺼지도록 설정
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT); // 동일
        final String provider = locationManager.getBestProvider(criteria, true); // 위치 데이터를 받기 위한 스트링 프로바이더
        try {
            Location mLoc = locationManager.getLastKnownLocation(provider); // 위치 데이터 스트링을 파싱해서 위치 알아냄
        }catch(SecurityException e){
            e.printStackTrace();
        } // try-catch

        final LocationListener locationListener = new LocationListener() { // 이벤트 리스너
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location; // 위치가 바뀌었을 때 새로 저장
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Location mLoc = locationManager.getLastKnownLocation(provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Location mLoc = locationManager.getLastKnownLocation(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }; // LocationListener

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); // caution
        } catch (Exception e) {
            e.printStackTrace();
        }

        reportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areYouReal(); // 신고 버튼이 클릭되었을 때 다이얼로그 창으로 이동
            }
        });
    } // onCreate

    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        String currentLocationAddress;
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress).append("#");
                    bf.append(lat).append("#");
                    bf.append(lng);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } // try-catch
        return bf.toString();
    } // findAddress
} // Main