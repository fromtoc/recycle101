package com.xin.portal.system.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFToPicture {
	
	private static Logger logger = LoggerFactory.getLogger(PDFToPicture.class);
	
	/**
	 * 获取PDF总页数
	 * 
	 * @throws IOException
	 */
	public static int getPDFNum(String fileUrl) {
		PDDocument pdDocument = null;
		int pages = 0;
		try {
			pdDocument = getPDDocument(fileUrl);
			pages = pdDocument.getNumberOfPages();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pdDocument != null) {
				try {
					pdDocument.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return pages;
	}

	/**
	 * PDF转图片 指定转第几页
	 * @param imagePaht-图片存放路径（不包含文件名称）
	 * 		  fileUrl-pdf的文件路径（包含文件名称）
	 * 		  page-指定转化第几页
	 * 		  imgType-转化为图片的类型（支持jpg,png）
	 *        dpi-dpi越大转换后越清晰，相对转换速度越慢(推荐100-300之间)
	 *        //经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
	 * 		  fileNewName 图片名称
	 * @throws IOException
	 *             imgType:转换后的图片类型 jpg,png
	 * @return 生成图片的URL
	 */
	public static List<String> PDFToImg(String imagePath, String fileUrl, Integer page, String imgType, int dpi, String fileNewName) {
		int pages = getPDFNum(fileUrl);
		File dstFile = new File(fileUrl);
		String imgPDFPath = dstFile.getParent();
		String imgFolderPath = createOrGetFilePaht(imagePath, imgPDFPath);
		int dot = dstFile.getName().lastIndexOf('.');
		String imagePDFName = dstFile.getName().substring(0, dot); // 获取图片文件名
		List<String> list = new ArrayList<String>();
		if(page == null){//全部转换
			if(pages > 0){
				for (int i = 0; i < pages; i++) {
					if (imgFolderPath != null) {
						String imageUrl = transformAndWrite(fileUrl, imgFolderPath, imagePDFName, i, imgType, dpi, fileNewName);
						list.add(imageUrl);
					}else{
						logger.error("文件夹路径创建失败. fileUrl: {}; imagePath: {} ", fileUrl, imagePath);
					}
				}
			}
		}else if (page <= pages && page > 0) {//转化页码是从0 开始的。所以页面数要大于0，最小为1 转化固定页面
			//通过PDF的文件路径获取到名称
			if (imgFolderPath != null) {
				String imageUrl = transformAndWrite(fileUrl, imgFolderPath, imagePDFName, page-1, imgType, dpi, fileNewName);
				list.add(imageUrl);
			}else{
				logger.error("文件夹路径创建失败. fileUrl: {}; imagePath: {} ", fileUrl, imagePath);
			}
		}else{
			logger.error("转化页码值超过了PDF的最大页码或者最小页码，无法转换！");
		}
		return list;
	}
	
	/**
	 * 在PDF文件同目录级别创建一个 image目录用于存放转换的图片
	 * 或者在指定的路径中创建目录
	 * @param imagePath   指定路径
	 * @param parentPaht  pdf 的父目录
	 * @return
	 */
	public static String createOrGetFilePaht(String imagePath,String parentPaht){
		String imgFolderPath = null;//图片路径含名称
		if (imagePath == null || "".equals(imagePath) || "null".equals(imagePath)) {
			imgFolderPath = parentPaht + File.separator + "images";// 获取图片存放的文件夹路径
		} else {
			imgFolderPath = imagePath + File.separator + "images";
		}
		if(createDirectory(imgFolderPath)){
			return imgFolderPath;
		}
		return null;
	}
	
	/**
	 * 
	 * @param fileUrl pdf文件路径
	 * @param imgFolderPath 图片文件夹路径
	 * @param imagePDFName 图片文件名称
	 * @param page 转换页码
	 * @param imgType 转换图片类型
	 * @param dpi 清晰度
	 * @return
	 */
	public static String transformAndWrite(String fileUrl, String imgFolderPath, String imagePDFName,
			int page, String imgType, int dpi, String fileNewName){
		PDDocument pdDocument = null;
		try {
			String imgFilePath = imgFolderPath + File.separator + imagePDFName 
					+ "_"+ (page+1) +"." +imgType;
			if(fileNewName != null && fileNewName.trim().length() > 0){
				imgFilePath = imgFolderPath + File.separator + fileNewName 
						+ "_"+ (page+1) +"." +imgType;
			}
			pdDocument = getPDDocument(fileUrl);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			File imgFile = new File(imgFilePath);
			BufferedImage image = renderer.renderImageWithDPI(page, dpi);
			ImageIO.write(image, imgType, imgFile);
			logger.info("PDF文档转PNG图片成功！");
			return imgFilePath;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PDF文档转PNG图片失败！ error: {}" , e.fillInStackTrace());
		} finally {
			if (pdDocument != null) {
				try {
					pdDocument.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * PDF转图片 指定转第几页
	 * @param imagePaht-图片存放路径（不包含文件名称）
	 * 		  fileUrl-pdf的文件路径（包含文件名称）
	 * 		  page-指定转化第几页
	 * 		  imgType-转化为图片的类型（支持jpg,png）
	 *        dpi-dpi越大转换后越清晰，相对转换速度越慢(推荐100-300之间)
	 * @throws IOException
	 *             imgType:转换后的图片类型 jpg,png
	 * @return 生成图片的URL
	 */
	public static String PDFToImg(String imagePath, String fileUrl, int page, String imgType, int dpi) throws IOException {
		int pages = getPDFNum(fileUrl);
		if (page <= pages && page >= 0) {
			PDDocument pdDocument = null;
			//通过PDF的文件路径获取到名称
			File dstFile = new File(fileUrl.toString());
			String imgPDFPath = dstFile.getParent();
			int dot = dstFile.getName().lastIndexOf('.');
			String imagePDFName = dstFile.getName().substring(0, dot); // 获取图片文件名
			String imgFolderPath = null;//图片路径含名称
			if (imagePath.equals("")) {
				imgFolderPath = imgPDFPath + File.separator + "images";// 获取图片存放的文件夹路径
			} else {
				imgFolderPath = imagePath + File.separator + "images";
			}
			if (createDirectory(imgFolderPath)) {
				try {
					pdDocument = getPDDocument(fileUrl);
					PDFRenderer renderer = new PDFRenderer(pdDocument);
					String imgFilePath = imgFolderPath + File.separator + imagePDFName+ "_"+ page +"." +imgType;
					File imgFile = new File(imgFilePath.toString());
					BufferedImage image = renderer.renderImageWithDPI(page, dpi);
					ImageIO.write(image, imgType, imgFile);
					System.out.println("PDF文档转PNG图片成功！");
					return imgFilePath;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("PDF文档转PNG图片失败！");
				} finally {
					if (pdDocument != null) {
						pdDocument.close();
					}
				}
			}else{
				System.out.println("文件夹路径创建失败"+imgFolderPath);
			}
		}
		return null;
	}

	private static PDDocument getPDDocument(String fileUrl) throws IOException {
		File file = new File(fileUrl);
		FileInputStream inputStream = new FileInputStream(file);
		return PDDocument.load(inputStream);
	}
	
	private static boolean createDirectory(String folder) {
		File dir = new File(folder);
		if (dir.exists()) {
			return true;
		} else {
			return dir.mkdirs();
		}
	}
	
	public static void main(String[] args) {
		//String imagePath = "C:\\Users\\Administrator\\Desktop\\TestFile";
		String fileUrl = "C:\\Users\\Administrator\\Desktop\\TestFile\\ZSDJGRB_ALL_2020-05-06-10-02-49.pdf";
//		try {
//			long start = System.currentTimeMillis();
//			PDFToPicture.PDFToImg(imagePath, fileUrl, 0, "png", 900);
//			PDFToPicture.PDFToImg(imagePath, fileUrl, 1, "png", 900);
//			PDFToPicture.PDFToImg(imagePath, fileUrl, 2, "png", 900);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		File file = new File("");
//		try {
//			String filePath = file.getCanonicalPath();
//			System.out.println(filePath);
//			System.out.println(System.getProperty("user.dir"));
//			String a = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//			System.out.println(a);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		List<String> aa = PDFToImg(null, fileUrl, null, "jpg", 300,null);
		System.out.println(aa.toString());
	}
	
}
