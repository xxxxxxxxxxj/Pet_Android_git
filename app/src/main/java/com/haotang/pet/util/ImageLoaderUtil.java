package com.haotang.pet.util;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoaderUtil {
	public static android.graphics.BitmapFactory.Options decodingOptions;
	public static ImageLoader instance;

	public static void loadImg(String headImg, ImageView iv_store_pic,
			int drawable, ImageLoadingListener listener) {
		decodingOptions = new Options();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(drawable) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(drawable)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(drawable) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.decodingOptions(decodingOptions)// 设置图片的解码配置
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				// .displayer(new RoundedBitmapDisplayer(80))//
				// 是否设置为圆角，弧度为多少
				// .displayer(new
				// FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
				.build();// 构建完成
		if (instance == null) {
			instance = ImageLoader.getInstance();
		}
		if (headImg != null && !TextUtils.isEmpty(headImg)) {
			if (headImg.startsWith("drawable://")) {
				instance.displayImage(headImg, iv_store_pic, options, listener);
			} else if (headImg.startsWith("file://")) {
				instance.displayImage(headImg, iv_store_pic, options, listener);
			} else {
				if (!headImg.startsWith("http://")
						&& !headImg.startsWith("https://")) {
					instance.displayImage(CommUtil.getServiceNobacklash()
							+ headImg, iv_store_pic, options, listener);
				} else {
					instance.displayImage(headImg, iv_store_pic, options,
							listener);
				}
			}
		}
	}

	public static void loadImgLocal(String headImg, ImageView iv_store_pic,
			int drawable, ImageLoadingListener listener) {
		decodingOptions = new Options();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(drawable) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(drawable)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(drawable) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.decodingOptions(decodingOptions)// 设置图片的解码配置
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				// .displayer(new RoundedBitmapDisplayer(80))//
				// 是否设置为圆角，弧度为多少
				// .displayer(new
				// FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
				.build();// 构建完成
		if (instance == null) {
			instance = ImageLoader.getInstance();
		}
		if (headImg != null && !TextUtils.isEmpty(headImg)) {
			if (headImg.startsWith("drawable://")) {
				instance.displayImage(headImg, iv_store_pic, options, listener);
			} else if (headImg.startsWith("file://")) {
				instance.displayImage(headImg, iv_store_pic, options, listener);
			} else {
				if (!headImg.startsWith("http://")
						&& !headImg.startsWith("https://")) {
					instance.displayImage(CommUtil.getServiceNobacklash()
							+ headImg, iv_store_pic, options, listener);
				} else {
					instance.displayImage(headImg, iv_store_pic, options,
							listener);
				}
			}
		}
	}

	public static void setImageWithTag(ImageView iv, String path,
			final int defaultimgid) {
		if (path != null && !"".equals(path.trim())) {
			if (path.startsWith("drawable://")) {
			} else if (path.startsWith("file://")) {
			} else if (!path.startsWith("http://")
					&& !path.startsWith("https://")) {
				path = CommUtil.getServiceNobacklash() + path;
			}
			if (instance == null) {
				instance = ImageLoader.getInstance();
			}
			instance.displayImage(path, iv, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View view) {
					try {
						if (defaultimgid > 0) {
							ImageView iv = (ImageView) view;
							iv.setImageResource(defaultimgid);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onLoadingFailed(String arg0, View view,
						FailReason arg2) {
					try {
						if (defaultimgid > 0) {
							ImageView iv = (ImageView) view;
							iv.setImageResource(defaultimgid);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onLoadingComplete(String path, View view,
						Bitmap bitmap) {
					if (view != null && bitmap != null) {
						ImageView iv = (ImageView) view;
						String imagetag = (String) iv.getTag();
						if (path != null && path.equals(imagetag)) {
							iv.setImageBitmap(bitmap);
						}
					}
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			});
		}
	}

	public static void setImage(ImageView iv, String path,
			final int defaultimgid) {
		if (path != null && !"".equals(path.trim())) {
			if (instance == null) {
				instance = ImageLoader.getInstance();
			}
			instance.displayImage(path, iv, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View view) {
					try {
						if (defaultimgid > 0) {
							ImageView iv = (ImageView) view;
							iv.setImageResource(defaultimgid);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onLoadingFailed(String arg0, View view,
						FailReason arg2) {
					try {
						if (defaultimgid > 0) {
							ImageView iv = (ImageView) view;
							iv.setImageResource(defaultimgid);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onLoadingComplete(String path, View view,
						Bitmap bitmap) {
					if (view != null && bitmap != null) {
						ImageView iv = (ImageView) view;
						// String imagetag = (String) iv.getTag();
						// if (path != null && path.equals(imagetag)) {
						iv.setImageBitmap(bitmap);
						// }
					}
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			});
		}
	}

	public static void cancel(ImageView imageView) {
		if (instance == null) {
			instance = ImageLoader.getInstance();
		}
		instance.cancelDisplayTask(imageView);
	}

	public static void distroy() {
		if (instance == null) {
			instance = ImageLoader.getInstance();
		}
		instance.clearMemoryCache();
		instance.destroy();
	}

	public static File getDiskCacheFile(String path) {
		if (instance == null) {
			instance = ImageLoader.getInstance();
		}
		return instance.getDiskCache().get(path);
	}

	public static boolean removeDiskCacheFile(String path) {
		if (instance == null) {
			instance = ImageLoader.getInstance();
		}
		instance.clearMemoryCache();
		return instance.getDiskCache().remove(path);
	}

	public static void loadImg(String imgUrl, ImageView img, int drawable,
			int Visible, int defaultVisible, ImageLoadingListener listener) {
		if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
			img.setVisibility(Visible);
			decodingOptions = new Options();
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(drawable) // 设置图片在下载期间显示的图片
					.showImageForEmptyUri(drawable)// 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(drawable) // 设置图片加载/解码过程中错误时候显示的图片
					.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
					.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
					.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
					.decodingOptions(decodingOptions)// 设置图片的解码配置
					// .delayBeforeLoading(int delayInMillis)//int
					// delayInMillis为你设置的下载前的延迟时间
					// 设置图片加入缓存前，对bitmap进行设置
					// .preProcessor(BitmapProcessor preProcessor)
					.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
					// .displayer(new RoundedBitmapDisplayer(80))//
					// 是否设置为圆角，弧度为多少
					// .displayer(new
					// FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
					.build();// 构建完成
			if (instance == null) {
				instance = ImageLoader.getInstance();
			}
			if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
				if (!imgUrl.startsWith("http://")
						&& !imgUrl.startsWith("https://")) {
					instance.displayImage(CommUtil.getServiceNobacklash()
							+ imgUrl, img, options, listener);
				} else {
					instance.displayImage(imgUrl, img, options, listener);
				}
			}
		} else {
			img.setVisibility(defaultVisible);
		}
	}
}
