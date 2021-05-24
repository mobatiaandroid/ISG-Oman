package com.algubra.permissionmanager;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


@SuppressLint("NewApi")
public class PermissionManager { /*extends AppCompatActivity {
    Context context;
    Activity activity;
     static final int PERMISSION_CALLBACK_CONSTANT_CALENDAR = 1;
     static final int PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2;
     static final int PERMISSION_CALLBACK_CONSTANT_LOCATION = 3;
     static final int REQUEST_PERMISSION_CALENDAR = 101;
     static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 102;
     static final int REQUEST_PERMISSION_LOCATION = 103;
//    String[] permissionsRequiredCalendar = new String[]{Manifest.permission.READ_CALENDAR,
//            Manifest.permission.WRITE_CALENDAR};
//    String[] permissionsRequiredExternalStorage = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    String[] permissionsRequiredLocation = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION};
ArrayList<String> permissionsRequiredCalendar;
     SharedPreferences calendarPermissionStatus;
     SharedPreferences externalStoragePermissionStatus;
     SharedPreferences locationPermissionStatus;
     SharedPreferences permissionStatus;
    PermissionManager(Context mContext)
    {
        this.context=mContext;
    }
    void setPermission(final Activity activity,int permissionStatusConstant,  ArrayList<String> mPermissionsRequired) {
        this.activity = activity;
        this.permissionsRequired=mPermissionsRequired;
        this.permissionStatus = activity.getSharedPreferences("permissionStatus" + String.valueOf(permissionStatusConstant), activity.MODE_PRIVATE);
//        calendarPermissionStatus = activity.getSharedPreferences("calendarPermissionStatus", activity.MODE_PRIVATE);
//        externalStoragePermissionStatus = activity.getSharedPreferences("externalStoragePermissionStatus", activity.MODE_PRIVATE);
//        locationPermissionStatus = activity.getSharedPreferences("locationPermissionStatus", activity.MODE_PRIVATE);


        if (ActivityCompat.checkSelfPermission(activity, permissionsRequiredExternalStorage[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, permissionsRequiredExternalStorage[1]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsRequiredExternalStorage[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsRequiredExternalStorage[1])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This module needs Storage permissions.");

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(activity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                builder.show();
            } else if (externalStoragePermissionStatus.getBoolean(permissionsRequiredExternalStorage[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This module needs Storage permissions.");

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        externalStorageToSettings = true;

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
                        Toast.makeText(activity, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        externalStorageToSettings = false;

                    }
                });
                builder.show();
            } else if (externalStoragePermissionStatus.getBoolean(permissionsRequiredExternalStorage[1], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This module needs Storage permissions.");
                builder.setCancelable(false);

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        externalStorageToSettings = true;

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
                        Toast.makeText(activity, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        externalStorageToSettings = false;

                    }
                });
                builder.show();
            } else {

                //just request the permission
//                        ActivityCompat.requestPermissions(activity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                ActivityCompat.requestPermissions(activity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);

            }
            SharedPreferences.Editor editor = externalStoragePermissionStatus.edit();
            editor.putBoolean(permissionsRequiredExternalStorage[0], true);
            editor.commit();
        } else {
//            replaceFragmentsSelected(position);

        }
    }


        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == PERMISSION_CALLBACK_CONSTANT_CALENDAR) {
                //check if all permissions are granted
                boolean allgranted = false;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        allgranted = true;
                    } else {
                        allgranted = false;
                        break;
                    }
                }

                if (allgranted) {
//                    proceedAfterPermission(tabPositionProceed);
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALENDAR)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Calendar Permissions");
                    builder.setMessage("This module needs calendar permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            calendarToSettings = false;

                            requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            calendarToSettings = false;

                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CALENDAR)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Calendar Permissions");
                    builder.setMessage("This module needs calendar permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            calendarToSettings = false;

                            requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            calendarToSettings = false;

                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
//                Toast.makeText(activity,"Unable to get Permission",Toast.LENGTH_LONG).show();
//                    calendarToSettings = true;
                    System.out.println("Permission4");

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_CALENDAR);
                    Toast.makeText(activity, "Go to settings and grant access to calendar", Toast.LENGTH_LONG).show();

                }
            } else if (requestCode == PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE) {
                //check if all permissions are granted
                boolean allgranted = false;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        allgranted = true;
                    } else {
                        allgranted = false;
                        break;
                    }
                }

                if (allgranted) {
//                    proceedAfterPermission(tabPositionProceed);
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Storage Permissions");
                    builder.setMessage("This module needs storage permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            externalStorageToSettings = false;

                            ActivityCompat.requestPermissions(activity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            externalStorageToSettings = false;

                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CALENDAR)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Storage Permissions");
                    builder.setMessage("This module needs storage permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            externalStorageToSettings = false;

                            ActivityCompat.requestPermissions(activity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            externalStorageToSettings = false;

                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
//                Toast.makeText(activity,"Unable to get Permission",Toast.LENGTH_LONG).show();
//                    externalStorageToSettings = true;

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
                    Toast.makeText(activity, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();

                }
            } else if (requestCode == PERMISSION_CALLBACK_CONSTANT_LOCATION) {
                //check if all permissions are granted
                boolean allgranted = false;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        allgranted = true;
                    } else {
                        allgranted = false;
                        break;
                    }
                }

                if (allgranted) {
//                    proceedAfterPermission(tabPositionProceed);
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Location Permissions");
                    builder.setMessage("This module needs location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            locationToSettings = false;

                            ActivityCompat.requestPermissions(activity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            locationToSettings = false;

                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Location Permissions");
                    builder.setMessage("This module needs location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            locationToSettings = false;

                            ActivityCompat.requestPermissions(activity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            locationToSettings = false;

                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
//                Toast.makeText(activity,"Unable to get Permission",Toast.LENGTH_LONG).show();
//                    locationToSettings = true;

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                    Toast.makeText(activity, "Go to settings and grant access to location", Toast.LENGTH_LONG).show();

                }
            }

        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_PERMISSION_CALENDAR) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    System.out.println("Permission5");

//                    proceedAfterPermission(tabPositionProceed);
                }*//*else  if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
                //DENIAL
                System.out.println("Permission6");

                requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
            }*//*
            } else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
//                    proceedAfterPermission(tabPositionProceed);
                }*//*else  if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
                //DENIAL
                requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
            }*//*
            } else if (requestCode == REQUEST_PERMISSION_LOCATION) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
//                    proceedAfterPermission(tabPositionProceed);
                }*//*else  if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                //DENIAL
                requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

            }*//*
            }
        }*/

}
