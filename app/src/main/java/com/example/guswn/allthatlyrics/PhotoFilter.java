package com.example.guswn.allthatlyrics;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class PhotoFilter {
    String filepath_or_url;
    private Context context;
    Integer type;
    ImageView imageView;
    File file;
    boolean isURL;
    public PhotoFilter(boolean isURL,Integer type, String filepath_or_url, File file,Context context,  ImageView imageView) {
        this.isURL = isURL;
        this.filepath_or_url = filepath_or_url;
        if (!isURL){ // url 이 아닐때만
            if (file==null){ // 이펙트 보여주는 사진 부분에서
                this.file = new File(filepath_or_url);
            }else {
                this.file = file;
            }
        }
        this.context = context;
        this.type = type;
        this.imageView = imageView;

    }

    public void photoFilterByType(){
        switch (type){
            case R.string.Photo_Blur:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new BlurTransformation(4, 2)))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else {
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new BlurTransformation(4, 2)))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }

            case R.string.Photo_Sepia:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new SepiaFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new SepiaFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }

            case R.string.Photo_Sketch:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new SketchFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new SketchFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }


            case R.string.Photo_Invert:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new InvertFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new InvertFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;

                }

            case R.string.Photo_Pixel:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new PixelationFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new PixelationFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
            case R.string.Photo_Contrast:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new ContrastFilterTransformation(1.8f)))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new ContrastFilterTransformation(1.8f)))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
            case R.string.Photo_Brightness:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new BrightnessFilterTransformation(0.15f)))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new BrightnessFilterTransformation(0.15f)))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
            case R.string.Photo_Kuwahara:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new KuwaharaFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new KuwaharaFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
            case R.string.Photo_Vignette:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new VignetteFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new VignetteFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
            case R.string.Photo_Gray:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new GrayscaleTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new GrayscaleTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
            case R.string.Photo_Cartoon:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(bitmapTransform(new ToonFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(bitmapTransform(new ToonFilterTransformation()))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
            case R.string.Photo_Normal:
                if (isURL){
                    Glide.with(context).load(filepath_or_url)
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }else{
                    Glide.with(context).load(Uri.fromFile(file))
                            .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                            .into(imageView);
                    break;
                }
                default:
                    if (isURL){
                        Glide.with(context).load(filepath_or_url)
                                .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                                .into(imageView);
                        break;
                    }else {
                        Glide.with(context).load(Uri.fromFile(file))
                                .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load))
                                .into(imageView);
                        break;
                    }
        }
    }

    public static Integer getTypeFromString(String type){
        Integer TYPE = null;
        switch (type){
            case "Normal":
                TYPE = R.string.Photo_Normal;
                break;
            case "Blur":
                TYPE = R.string.Photo_Blur;
                break;
            case "Sepia":
                TYPE = R.string.Photo_Sepia;
                break;
            case "Sketch":
                TYPE = R.string.Photo_Sketch;
                break;
            case "Invert":
                TYPE = R.string.Photo_Invert;
                break;
            case "Pixel":
                TYPE = R.string.Photo_Pixel;
                break;
            case "Contrast":
                TYPE = R.string.Photo_Contrast;
                break;
            case "Brightness":
                TYPE = R.string.Photo_Brightness;
                break;
            case "Kuwahara":
                TYPE = R.string.Photo_Kuwahara;
                break;
            case "Vignette":
                TYPE = R.string.Photo_Vignette;
                break;
            case "Gray":
                TYPE = R.string.Photo_Gray;
                break;
            case "Cartoon":
                TYPE = R.string.Photo_Cartoon;
                break;

        }
        return TYPE;
    }
}
