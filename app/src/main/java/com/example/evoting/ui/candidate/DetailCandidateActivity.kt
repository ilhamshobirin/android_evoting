package com.example.evoting.ui.candidate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.DataLogin
import com.example.evoting.model.candidate.AddCandidateRequest
import com.example.evoting.model.candidate.DataItemAllCandidate
import com.example.evoting.retrofit.ApiService
import com.example.evoting.ui.HomeActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DetailCandidateActivity : Activity() {
    companion object {
        const val EXTRA_DETAIL = "candidate Detail"
    }

    private var imageUri: Uri? = null
    lateinit var imageView: ImageView

    var SELECT_PICTURE = 200
    var photo_path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_candidate)

        imageView = findViewById(R.id.iv_photo)
        imageView.setOnClickListener{
            imageChooser()
        }

        val detailCandidate = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL, DataItemAllCandidate::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DETAIL)
        }

        val editMode: Boolean = detailCandidate != null
        printLog("is edit mode? $editMode")

        val etName: EditText = findViewById(R.id.et_name)
        val etDetail: EditText = findViewById(R.id.et_detail)

        val btnAddEditCandidate: Button = findViewById(R.id.btn_add_edit_candidate)
        val btnDeleteCandidate: Button = findViewById(R.id.btn_delete_candidate)
        if(editMode){
            Glide
                .with(this)
                .load(detailCandidate?.image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .into(imageView)

            etName.setText(detailCandidate?.name)
            etDetail.setText(detailCandidate?.detail)
            btnAddEditCandidate.text = "Edit"
            btnDeleteCandidate.visibility = View.VISIBLE
        }else{
            btnAddEditCandidate.text = "Tambah"
            btnDeleteCandidate.visibility = View.GONE
        }

        btnAddEditCandidate.setOnClickListener {
            val name = if(editMode){
                detailCandidate?.name
            }else{
                etName.text.toString()
            }

            val detail = if(editMode){
                detailCandidate?.detail
            }else{
                etDetail.text.toString()
            }

            if(name?.isEmpty()!!){
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if(detail?.isEmpty()!!){
                Toast.makeText(this, "Info Profil Kandidat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if(photo_path?.isEmpty()!!){
                Toast.makeText(this, "Foto Kandidat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else{
                if(editMode){
                    //TODO belum bisa
                    //edit candidate
                    putCandidate(this, detailCandidate?.id!!, AddCandidateRequest(name, detail))
                }else{
                    //Add Request
                    postCandidate(this, AddCandidateRequest(name, detail))
                }
            }
        }

        btnDeleteCandidate.setOnClickListener {
            deleteCandidate(this, detailCandidate?.id!!)
        }


    }

    private fun postCandidate(context: Context, addCandidateRequest: AddCandidateRequest) {
        val params = HashMap<String, @JvmSuppressWildcards RequestBody>()
        params["name"] = addCandidateRequest.name.toRequestBody()
        params["detail"] = addCandidateRequest.detail.toRequestBody()
        val file = File(photo_path!!)

        val requestBody : RequestBody = file.asRequestBody("image/*".toMediaType())
        val parts : MultipartBody.Part  = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val sessionManager = SessionManager(this)

        ApiService.endpoint.postCandidate("Bearer ${sessionManager.getDataLogin()?.token}", parts, params)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        context,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    printLog(t.toString())
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Toast.makeText(
                        context,
                        "Berhasil menambahkan data kandidat",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@DetailCandidateActivity, CandidateActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    printLog(response.toString())
                }
            })
    }

    private fun putCandidate(context: Context, id: Int,  addCandidateRequest: AddCandidateRequest) {
        val params = HashMap<String, @JvmSuppressWildcards RequestBody>()
        params["name"] = addCandidateRequest.name.toRequestBody()
        params["detail"] = addCandidateRequest.detail.toRequestBody()
        val file = File(photo_path!!)

        val requestBody : RequestBody = file.asRequestBody("image/*".toMediaType())
        val parts : MultipartBody.Part  = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val sessionManager = SessionManager(this)

        ApiService.endpoint.putCandidate("Bearer ${sessionManager.getDataLogin()?.token}", id, parts, params)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        context,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    printLog(t.toString())
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Toast.makeText(
                        context,
                        "Berhasil menambahkan data kandidat",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@DetailCandidateActivity, CandidateActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    printLog(response.toString())
                }
            })
    }

    private fun deleteCandidate(context: Context, id: Int) {
        val sessionManager = SessionManager(this)

        ApiService.endpoint.deleteCandidate("Bearer ${sessionManager.getDataLogin()?.token}", id)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        context,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    printLog(t.toString())
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Toast.makeText(
                        context,
                        "Berhasil Menghapus kandidat",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@DetailCandidateActivity, CandidateActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    printLog(response.toString())
                }
            })
    }

    // this function is triggered when
    // the Select Image Button is clicked
    fun imageChooser() {
        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                imageUri = data.data
                if (null != imageUri) {
                    // update the preview image in the layout
                    imageView.setImageURI(imageUri)
                    photo_path = getRealPathFromURI(imageUri!!, this)
                    printLog("Photo Path $photo_path")
                }
            }
        }
    }

    fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream?.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)

        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }


    private fun printLog(message: String){
        Log.d("Detail Cand Activity", message)
    }
}