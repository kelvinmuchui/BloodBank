package com.example.kelvin.blooddonation.Utills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kelvin.blooddonation.Model.User;
import com.example.kelvin.blooddonation.Model.UserAccountSetting;
import com.example.kelvin.blooddonation.R;
import com.example.kelvin.blooddonation.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseMethods {


        private static final String TAG = "FirebaseMethods";
        //firebase
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference mRef;
        private StorageReference mStorageReference;
        private String userID;

        private FirebaseFirestore firebaseFirestore;

        //vars
        private Context mContext;
        private double mPhotoUploadProgress =0;


    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();

        }

    }

//        public void uploadNewPhoto(String photoType,final String caption, final int count, final String imgUrl,
//        Bitmap bm){
//        Log.d(TAG, "uploadNewPhoto: uploading");
//
//        FilePaths filePaths = new FilePaths();
//        //case1) new Photo
//        if (photoType.equals(mContext.getString(R.string.new_photo))){
//
//            String user_id =FirebaseAuth.getInstance().getCurrentUser().getUid();
//            StorageReference storageReference =mStorageReference
//                    .child(filePaths.FIREBASE_IMAGE_STROGE +"/" + user_id + "/photo" +(count +1));
//            //convert image url to bitmap
//            if(bm == null){
//                bm = ImageManager.getBitmap(imgUrl);
//            }
//
//            byte[] bytes = ImageManager.getBytesFromBitmap(bm,100);
//            UploadTask uploadTask = null;
//            uploadTask = storageReference.putBytes(bytes);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Uri firebaseUrl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(mContext, "Photo upload successful: " , Toast.LENGTH_SHORT).show();
//
//                    //add the new photo to photo node and user_photo node
//
//                    addPhotoToDatabase(caption,firebaseUrl.toString());
//
//                    //navigate to the main feed so the user can their photo
//
//                    Intent intent = new Intent(mContext, HomeActivity.class);
//                    mContext.startActivity(intent);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    Toast.makeText(mContext, "Photo upload Failed: ", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
//                    if (progress -15 > mPhotoUploadProgress){
//
//                        Toast.makeText(mContext, "Photo upload progess: " + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
//
//                        mPhotoUploadProgress = progress;
//                    }
//                }
//            });
//
//
//        }
//        //case2) new profile photo
//        else if (photoType.equals(mContext.getString(R.string.profile_photo))){
//
//
//            String user_id =FirebaseAuth.getInstance().getCurrentUser().getUid();
//            StorageReference storageReference =mStorageReference
//                    .child(filePaths.FIREBASE_IMAGE_STROGE +"/" + user_id + "/profile_photo");
//            //convert image url to bitmap
//            if(bm == null){
//                bm = ImageManager.getBitmap(imgUrl);
//            }            byte[] bytes = ImageManager.getBytesFromBitmap(bm,100);
//            UploadTask uploadTask = null;
//            uploadTask = storageReference.putBytes(bytes);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Uri firebaseUrl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(mContext, "Photo upload successful: " , Toast.LENGTH_SHORT).show();
//
//
//
//                    //insert into 'user_account_settings' node
//                    setProfilePhoto(firebaseUrl.toString());
//
//                    ((AccountSettingsActivity)mContext).setViewPager(
//                            ((AccountSettingsActivity)mContext).pagerAdapter
//                                    .getFragmentNumber(mContext.getString(R.string.edit_profile_fragment))
//                    );
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    Toast.makeText(mContext, "Photo upload Failed: ", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
//                    if (progress -15 > mPhotoUploadProgress){
//
//                        Toast.makeText(mContext, "Photo upload progess: " + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
//
//                        mPhotoUploadProgress = progress;
//                    }
//                }
//            });
//        }
//
//
//
//    }
//        private void setProfilePhoto(String url){
//        mRef.child(mContext.getString(R.string.dbname_user_account_settings))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child(mContext.getString(R.string.profile_photo))
//                .setValue(url);
//    }
//        private String getTimeStamp(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'z'", Locale.UK);
//        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
//        return sdf.format(new Date());
//    }
//        private void addPhotoToDatabase(String caption,String url){
//        String tags = StringManipulation.getTags(caption);
//        String newPhotoKey = mRef.child(mContext.getString(R.string.dbname_photos)).push().getKey();
//        Photo photo = new Photo();
//        photo.setCaption(caption);
//        photo.setDate_created(getTimeStamp());
//        photo.setImage_path(url);
//        photo.setTags(tags);
//        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        photo.setPhoto_id(newPhotoKey);
//
//        //insert into database
//        mRef.child(mContext.getString(R.string.dbname_user_photos))
//                .child(FirebaseAuth.getInstance()
//                        .getCurrentUser().getUid() ).child(newPhotoKey).setValue(photo);
//
//        mRef.child(mContext.getString(R.string.dbname_photos)).child(newPhotoKey).setValue(photo);
//
//
//    }
//
//        public int getImageCount(DataSnapshot dataSnapshot){
//        int count = 0;
//
//        for(DataSnapshot ds: dataSnapshot
//                .child(mContext.getString(R.string.dbname_user_photos))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .getChildren()){
//
//            count ++;
//        }
//        return count;
//    }

        /***
         * updating user_accout_settings for current user
         * @param displayName
         * @param website
         * @param description
         */

//        public void updateUserAccountSettings(String displayName, String website, String description){
//
//
//        Log.d(TAG, "updateUserAccountSettings:  updating user account settings");
//
//        if(displayName != null) {
//            mRef.child(mContext.getString(R.string.dbname_user_account_settings))
//                    .child(userID)
//                    .child(mContext.getString(R.string.field_display_name))
//                    .setValue(displayName);
//        }
//        if(website != null) {
//            mRef.child(mContext.getString(R.string.dbname_user_account_settings))
//                    .child(userID)
//                    .child(mContext.getString(R.string.field_website))
//                    .setValue(website);
//        }
//        if(description != null) {
//            mRef.child(mContext.getString(R.string.dbname_user_account_settings))
//                    .child(userID)
//                    .child(mContext.getString(R.string.field_description))
//                    .setValue(description);
//        }
////        mRef.child(mContext.getString(R.string.dbname_user_account_settings))
////                .child(userID)
////                .child(mContext.getString(R.string.field_phone_number))
////                .setValue();
//
//
//
//    }

        /**
         * update username in the users and user_account_settings node
         * @param username
         */



        /**
         * Update email in the users node
    //     * @param email
         */
//        public void updateEmail(String email ){
//        Log.d(TAG, "updateEmail: updating Email to: "+email);
//        mRef.child(mContext.getString(R.string.dbname_users))
//                .child(userID)
//                .child(mContext.getString(R.string.field_email))
//                .setValue(email);
//
//    }

    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot){
        Log.d(TAG, "checkIfUsernameExists:  checking if " + username + "already exist");
        User user = new User();

        for(DataSnapshot ds : datasnapshot.child(userID).getChildren()){

            Log.d(TAG, "checkIfUsernameExists:  datasnapshot" + ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            if(user.getUsername().equals(username)){
                Log.d(TAG, "checkIfUsernameExists:  FOUND A MATCH" +user.getUsername());
                return  true;
            }
        }

        return false;

    }

        /**
         * Register a new email and password to Firebase Authentication
         * @param email
         * @param password
         * @param username
         */
        public void registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "Failed",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful()){
                            //send verification email
                            sendVerificationEmail();
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }

        public  void  sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }else {
                                Toast.makeText(mContext,"couldnt send verification email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

        /**
         * Add information to th users node
         * add information to the user_account_settings node
         * @param email
         * @param username
         * @param phone_number
         * @param location
         */

        public void addNewUser(String email,String username, long phone_number, String location){
            User user = new User( userID, 1, email ,username,location);
            mRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .setValue(user);



            UserAccountSetting settings = new UserAccountSetting(
                    username,
                    phone_number,
                    email,
                    "",
                    "",
                    ""

            );

            mRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .setValue(settings);
        }

        /**
         * retrives the account settings for the user currently logged in
         * Database: user_account_settings node
         * @param dataSnapshot
         * @return
         */

//        public UserSettings getUserSettings (DataSnapshot dataSnapshot){
//        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase");
//
//
//        UserAccountSetting settings = new UserAccountSetting();
//
//        User user = new User();
//        for (DataSnapshot ds:dataSnapshot.getChildren()){
//            //user_account_settings node
//            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))){
//                Log.d(TAG, "getUserAccountSettings: datasnapshot" + ds);
//
//                try {
//                    settings.setDisplay_name(
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getDisplay_name()
//                    );
//                    settings.setUsername(
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getUsername()
//                    );
//                    settings.setWebsite(
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getWebsite()
//                    );
//
//                    settings.setDescription(
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getDescription()
//                    );
//                    settings.setProfile_photo (
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getDisplay_name()
//                    );
//                    settings.setPosts(
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getPosts()
//                    );
//                    settings.setFollowers(
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getFollowers()
//                    );
//                    settings.setFollowing(
//                            ds.child(userID)
//                                    .getValue(UserAccountSettings.class)
//                                    .getFollowing()
//                    );
//
//                }catch (NullPointerException e){
//                    Log.d(TAG, "getUserAccountSettings:  NulePointerException" + e.getMessage());
//                }
//
//            }
//            if(ds.getKey().equals(mContext.getString(R.string.dbname_users))){
//                Log.d(TAG, "getUserAccountSettings: datasnapshot" + ds);
//
//                user.setUsername(
//                        ds.child(userID)
//                                .getValue(User.class)
//                                .getUsername()
//                );
//
//                user.setEmail(
//                        ds.child(userID)
//                                .getValue(User.class)
//                                .getEmail()
//                );
//
//                user.setPhone_number(
//                        ds.child(userID)
//                                .getValue(User.class)
//                                .getPhone_number()
//                );
//
//                user.setUser_id(
//                        ds.child(userID)
//                                .getValue(User.class)
//                                .getUser_id()
//                );
//                Log.d(TAG, "getUserSettings: retrieved user info" +user.toString());
//
//            }
//        }
//        return new UserSettings(user, settings);
//    }


    }


