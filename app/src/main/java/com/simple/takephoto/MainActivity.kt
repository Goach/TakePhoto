package com.simple.takephoto

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.goach.takephoto.TakePhoto
import com.goach.takephoto.callback.CropBitmapListener
import com.goach.takephoto.callback.TakePhotoResultListener
import com.goach.takephoto.manager.CropConfig
import com.goach.takephoto.manager.MultipleConfig
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_capture.setOnClickListener {
            TakePhoto.with(this).asCaptureBuilder()
                .setTakePhotoResultListener(object : TakePhotoResultListener {
                    override fun onTakeCancel() {
                        Toast.makeText(this@MainActivity, "取消选择", Toast.LENGTH_SHORT).show()
                    }

                    override fun onTakeSuccess(takePhotoPath: List<String>) {
                        Toast.makeText(this@MainActivity, "选择成功", Toast.LENGTH_SHORT).show()
                        Glide.with(this@MainActivity).load(File(takePhotoPath[0])).into(iv_take)
                    }

                    override fun onTakeFailure() {
                        Toast.makeText(this@MainActivity, "选择失败", Toast.LENGTH_SHORT).show()
                    }

                }).startPickFromCapture()
        }
        button_capture_crop.setOnClickListener {
            TakePhoto.with(this).asCaptureBuilder().setCrop(true)
                .setCropConfig(
                    CropConfig().setAspectX(1).setAspectY(1).setOutputX(500).setOutputY(500)
                        .setCropBitmapListener(object : CropBitmapListener {
                            override fun onCropBitmap(bitmap: Bitmap) {
                                Log.d("zgx", "bitmap_width：" + bitmap.width)
                                Log.d("zgx", "bitmap_height：" + bitmap.height)
                            }

                            override fun onCropBitmapFailure() {
                                Log.d("zgx", "bitmap_height：failure")
                            }
                        })
                )
                .setTakePhotoResultListener(object : TakePhotoResultListener {
                    override fun onTakeCancel() {
                        Toast.makeText(this@MainActivity, "  取消裁剪", Toast.LENGTH_SHORT).show()
                    }

                    override fun onTakeSuccess(takePhotoPath: List<String>) {
                        Toast.makeText(this@MainActivity, "选择成功", Toast.LENGTH_SHORT).show()
                        Glide.with(this@MainActivity).load(File(takePhotoPath[0])).into(iv_take)
                    }

                    override fun onTakeFailure() {
                        Toast.makeText(this@MainActivity, "选择失败", Toast.LENGTH_SHORT).show()
                    }

                }).startPickFromCapture()
        }
        button_photo.setOnClickListener {
            TakePhoto.with(this).asGalleryBuilder()
                .setTakePhotoResultListener(object : TakePhotoResultListener {
                    override fun onTakeCancel() {
                        Toast.makeText(this@MainActivity, "取消图库选择照片", Toast.LENGTH_SHORT).show()
                    }

                    override fun onTakeSuccess(takePhotoPath: List<String>) {
                        Toast.makeText(this@MainActivity, "图库选择照片成功", Toast.LENGTH_SHORT).show()
                        Glide.with(this@MainActivity).load(File(takePhotoPath[0])).into(iv_take)
                    }

                    override fun onTakeFailure() {
                        Toast.makeText(this@MainActivity, "图库选择照片失败", Toast.LENGTH_SHORT).show()
                    }

                }).startPickFromGallery()
        }
        button_photo_crop.setOnClickListener {
            TakePhoto.with(this).asGalleryBuilder().setCrop(true)
                .setCropConfig(
                    CropConfig().setAspectX(1).setAspectY(1).setOutputX(500).setOutputY(500)
                        .setCropBitmapListener(object : CropBitmapListener {
                            override fun onCropBitmap(bitmap: Bitmap) {
                                Log.d("zgx", "bitmap_width：" + bitmap.width)
                                Log.d("zgx", "bitmap_height：" + bitmap.height)
                            }

                            override fun onCropBitmapFailure() {
                                Log.d("zgx", "bitmap_height：failure")
                            }
                        })
                )
                .setTakePhotoResultListener(object : TakePhotoResultListener {
                    override fun onTakeCancel() {
                        Toast.makeText(this@MainActivity, "  取消图库照片裁剪", Toast.LENGTH_SHORT).show()
                    }

                    override fun onTakeSuccess(takePhotoPath: List<String>) {
                        Toast.makeText(this@MainActivity, "裁剪图库照片成功", Toast.LENGTH_SHORT).show()
                        Glide.with(this@MainActivity).load(File(takePhotoPath[0])).into(iv_take)
                    }

                    override fun onTakeFailure() {
                        Toast.makeText(this@MainActivity, "裁剪图库照片失败", Toast.LENGTH_SHORT).show()
                    }

                }).startPickFromGallery()
        }

        button_photo_multiple.setOnClickListener {
            TakePhoto.with(this).asGalleryBuilder()
                .setMultipleConfig(MultipleConfig().setMaxNum(7))
                .setTakePhotoResultListener(object : TakePhotoResultListener {
                    override fun onTakeCancel() {
                        Toast.makeText(this@MainActivity, "  取消图库选择多张照片", Toast.LENGTH_SHORT).show()
                    }

                    override fun onTakeSuccess(takePhotoPath: List<String>) {
                        Toast.makeText(this@MainActivity, "图库选择多张照片成功${takePhotoPath.size}", Toast.LENGTH_SHORT).show()
                        Glide.with(this@MainActivity).load(File(takePhotoPath[0])).into(iv_take)
                    }

                    override fun onTakeFailure() {
                        Toast.makeText(this@MainActivity, "图库选择多张照片失败", Toast.LENGTH_SHORT).show()
                    }

                }).startPickFromGallery()
        }
        button_photo_multiple_crop.setOnClickListener {
            TakePhoto.with(this).asGalleryBuilder()
                .setMultipleConfig(MultipleConfig().setMaxNum(7))
                .setCrop(true)
                .setCropConfig(
                    CropConfig().setAspectX(1).setAspectY(1).setOutputX(500).setOutputY(500)
                        .setCropBitmapListener(object : CropBitmapListener {
                            override fun onCropBitmap(bitmap: Bitmap) {
                                Log.d("zgx", "bitmap_width：" + bitmap.width)
                                Log.d("zgx", "bitmap_height：" + bitmap.height)
                            }

                            override fun onCropBitmapFailure() {
                                Log.d("zgx", "bitmap_height：failure")
                            }
                        })
                )
                .setTakePhotoResultListener(object : TakePhotoResultListener {
                    override fun onTakeCancel() {
                        Toast.makeText(this@MainActivity, "裁剪取消图库选择多张照片", Toast.LENGTH_SHORT).show()
                    }

                    override fun onTakeSuccess(takePhotoPath: List<String>) {
                        Toast.makeText(this@MainActivity, "裁剪图库选择多张照片成功${takePhotoPath.size}", Toast.LENGTH_SHORT).show()
                        Glide.with(this@MainActivity).load(File(takePhotoPath[0])).into(iv_take)
                        takePhotoPath.forEachIndexed { index, s ->
                            Log.d("zgx","裁剪结果：$index 路径：$s")
                        }
                    }

                    override fun onTakeFailure() {
                        Toast.makeText(this@MainActivity, "裁剪图库选择多张照片失败", Toast.LENGTH_SHORT).show()
                    }

                }).startPickFromGallery()
        }
    }
}