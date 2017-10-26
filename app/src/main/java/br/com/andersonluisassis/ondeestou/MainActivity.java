package br.com.andersonluisassis.ondeestou;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnGps,pararGps;
    TextView txtLatitude, txtLongitude;
    private LocationManager locationManager;
    LocationListener locationListener;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        btnGps = (Button) findViewById(R.id.btnGps);
        pararGps = (Button) findViewById(R.id.btnGpsParar);

       btnGps.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pedirPermissoes();
           }
       });



        pararGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parar();
            }
        });


    }//fim do oncreate

    private void pedirPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
            configurarServico();
    }//fim pedirPermissoes

    private void configurarServico() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                   // atualizar(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) { }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            if (locationManager!=null)
                {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            if (location!= null)
               {
                atualizar(location);
               }



        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }//fim configurarServico


    // metodo para permissao android acima da versão 6
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }//fim onRequestPermissionsResult

     // aqui pega as posições
    public void atualizar(Location location)
    {
        Double latPoint = location.getLatitude();
        Double lngPoint = location.getLongitude();

        txtLatitude.setText(latPoint.toString());
        txtLongitude.setText(lngPoint.toString());
    }


        //parar o gps
         public void parar (){
             if (locationListener!= null) {
                 locationManager.removeUpdates(locationListener);
                 txtLatitude.setText("");
                 txtLongitude.setText("");
             }
             else{
                 Toast.makeText(this, "GPS NÃO FOI LIGADO", Toast.LENGTH_LONG).show();
             }
         }


}
