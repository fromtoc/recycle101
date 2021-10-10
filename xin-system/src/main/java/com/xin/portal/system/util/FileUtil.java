package com.xin.portal.system.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	private static String videoType;
	
	private static long videoSize;
	
	private static String fileType;
	
	private static long fileSize;
	
	private static String imageType;
	
	private static long imageSize;
	
	@Value("${videoType:mp4}")
	public void setVideoType(String type) {  
		FileUtil.videoType = type;  
	} 
	@Value("${videoSize:500}")
	public void setVideoSize(String size) {  
		FileUtil.videoSize = Long.parseLong(size);  
	}
	@Value("${fileType:pdf,ppt,pptx,doc,docx}")
	public void setFileType(String type) {  
		FileUtil.fileType = type;  
	}
	@Value("${fileSize:10}")
	public void setFileSize(String size) {  
		FileUtil.fileSize = Long.parseLong(size);  
	}
	@Value("${imageType:jpg,png,jpeg}")
	public void setImageType(String type) {  
		FileUtil.imageType = type;  
	}
	@Value("${imageSize:2}")
	public void setImageSize(String size) {  
		FileUtil.imageSize = Long.parseLong(size);   
	}
	
	public static String read(String filePath) {
		File file = new File(filePath);// 定义一个file对象，用来初始化FileReader
		FileReader reader;
		StringBuilder sb = new StringBuilder();// 定义一个字符串缓存，将字符串存放缓存中
		try {
			reader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(reader);// new一个BufferedReader对象，将文件内容读取到缓存
			String s = "";
			while ((s = bReader.readLine()) != null) {// 逐行读取文件内容，不读取换行符和末尾的空格
				sb.append(s);// 将读取的字符串添加换行符后累加存放在缓存中
			}
			bReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} // 定义一个fileReader对象，用来初始化BufferedReader
		return sb.toString();
	}

	public static void download(String filePath, String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		File file = new File(filePath);
		// 判断文件父目录是否存在
		if (file.exists()) {
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 文件输入流
			BufferedInputStream bis = null;
			// 输出流
			OutputStream os = null;
			try {
				os = response.getOutputStream();
				bis = new BufferedInputStream(new FileInputStream(filePath));
				byte[] buffer = new byte[bis.available()];
				bis.read(buffer);
				os = new BufferedOutputStream(response.getOutputStream());
	            os.write(buffer);
	            os.flush();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bis.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static File create(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		if (filePath.endsWith(File.separator)) {
			return null;
		}
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return null;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}

	public static boolean create(String filePath, String content) {
		File file = new File(filePath);
		if (file.exists()) {
			return false;
		}
		if (filePath.endsWith(File.separator)) {
			return false;
		}
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (StringUtils.isEmpty(content)) {
			return true;
		}

		logger.info(" file path : {}", file.getAbsolutePath());
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(content.getBytes());
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public static boolean write(String filePath, String content) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		if (filePath.endsWith(File.separator)) {
			return false;
		}
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		if (StringUtils.isEmpty(content)) {
			return true;
		}

		logger.info(" file path : {}", file.getAbsolutePath());
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(content.getBytes());
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public static void processDownload(String filePath, String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		int BUFFER_SIZE = 4096;
		InputStream in = null;
		OutputStream out = null;

		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");

			// String userName = request.getHeader("userName");
			// String passwd = request.getHeader("passwd");
			// String fileName = request.getHeader("fileName");
			//
			// System.out.println("userName:" + userName);
			// System.out.println("passwd:" + passwd);
			// System.out.println("fileName:" + fileName);

			// 可以根据传递来的userName和passwd做进一步处理，比如验证请求是否合法等
			File file = new File(filePath);
			response.setContentLength((int) file.length());
			response.setHeader("Accept-Ranges", "bytes");

			int readLength = 0;

			in = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
			out = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[BUFFER_SIZE];
			while ((readLength = in.read(buffer)) > 0) {
				byte[] bytes = new byte[readLength];
				System.arraycopy(buffer, 0, bytes, 0, readLength);
				out.write(bytes);
			}

			out.flush();

			response.addHeader("result", "success");

		} catch (Exception e) {
			e.printStackTrace();
			response.addHeader("result", "error");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static String getFileEncode(String path) {
		String charset ="asci";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "Unicode";//UTF-16LE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "Unicode";//UTF-16BE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int len = 0;
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) //单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) 
                        //双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) { //也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
                //TextLogger.getLogger().info(loc + " " + Integer.toHexString(read));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                }
            }
        }
        return charset;
	}

	private static String getEncode(int flag1, int flag2, int flag3) {
		String encode="";
		// txt文件的开头会多出几个字节，分别是FF、FE（Unicode）,
		// FE、FF（Unicode big endian）,EF、BB、BF（UTF-8）
		if (flag1 == 255 && flag2 == 254) {
			encode="Unicode";
		}
		else if (flag1 == 254 && flag2 == 255) {
			encode="UTF-16";
		}
		else if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
			encode="UTF8";
		}
		else {
			encode="asci";// ASCII码
		}
		return encode;
	}
	
	public static int checkFileSizeOrType(String fileName, long filesSize , String type){
		if(fileName.contains(".")){
			int index = fileName.lastIndexOf(".");
	        String extName = fileName.substring(index + 1, fileName.length());
	        if("file".equals(type)){//文件或者视屏 课件10M，后缀pdf ，视频 500M, 后缀MP4
	        	if(Arrays.<String> asList(videoType.split(",")).contains(extName)){
	        		if((double)videoSize >= ((double)filesSize)/1024/1024 ){
	        			return 0;
	        		}else{
	        			return 1;
	        		}
	        	}else if (Arrays.<String> asList(fileType.split(",")).contains(extName)){
	        		if((double)fileSize >= ((double)filesSize)/1024/1024 ){
	        			return 0;
	        		}else{
	        			return 1;
	        		}
	        	}
	        }else if("image".equals(type)){//图片  图片：2M，后缀jpg, png
	        	if(Arrays.<String> asList(imageType.split(",")).contains(extName)){
	        		if((double)imageSize >= ((double)filesSize)/1024/1024 ){
	        			return 0;
	        		}else{
	        			return 1;
	        		}
	        	}
	        }
		}
		return 2;
	}
	
	
	
}
