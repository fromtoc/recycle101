package com.xin.portal.system.util;

import java.io.*;

import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

public class OfficeUtil {
	public static boolean getLicense(String type) {
		boolean result = false;
		InputStream is = null;
		try {
			is = OfficeUtil.class.getClassLoader().getResourceAsStream("\\license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
			switch(type){
			case "Excel":
				com.aspose.cells.License licenseExcel = new com.aspose.cells.License();
				licenseExcel.setLicense(is);
				break;
			case "Slide":
				com.aspose.slides.License licenseSlide = new com.aspose.slides.License();
				licenseSlide.setLicense(is);
				break;
			case "Word":
				com.aspose.words.License licenseWord = new com.aspose.words.License();
				licenseWord.setLicense(is);
				break;
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void doc2pdf(String filePath, String savePath) {
		FileOutputStream os = null;
		try {
			long old = System.currentTimeMillis();
			File file = new File(savePath); // 新建一个空白pdf文档
			os = new FileOutputStream(file);
			Document doc = new Document(filePath); // Address是将要被转化的word文档
			doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML,
											// OpenDocument, PDF, EPUB, XPS, SWF
											// 相互转换
			long now = System.currentTimeMillis();
			//System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void ppt2pdf(String filePath, String savePath) {
		if (!getLicense("Slide")) { // 验证License 若不验证则转化出的pdf文档会有水印产生
			return;
		}
		FileOutputStream os = null;
		try {
			long old = System.currentTimeMillis();
			File file = new File(savePath); // 新建一个空白pdf文档
			os = new FileOutputStream(file);
			Presentation ppt = new Presentation(filePath);
			ppt.save(os, com.aspose.slides.SaveFormat.Pdf);
			long now = System.currentTimeMillis();
			//System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
			//System.out.println(savePath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void excel2pdf(String filePath, String savePath) {
		if (!getLicense("Excel")) { // 验证License 若不验证则转化出的pdf文档会有水印产生
			return;
		}
		FileOutputStream fileOS = null;
		try {

			long old = System.currentTimeMillis();
			Workbook excel = new Workbook(filePath);
			File file = new File(savePath); // 新建一个空白pdf文档
			fileOS = new FileOutputStream(file);
			excel.save(fileOS, com.aspose.cells.SaveFormat.PDF);
			long now = System.currentTimeMillis();
			//System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
			//System.out.println(savePath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOS != null) {
					fileOS.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
//		doc2pdf("E:/work/德昂portal/portal部署文档_初稿.doc",
//				"D:/Programs/apache-tomcat-8.5.32/webapps/" + Math.random() + ".pdf");
		excel2pdf("E:/work/德昂portal/portal功能大纲_初稿.xls",
				"D:/Programs/apache-tomcat-8.5.32/webapps/" + Math.random() + ".pdf");
//		ppt2Pdf("D:/Programs/apache-tomcat-8.5.32/webapps/test.pptx",
//				"D:/Programs/apache-tomcat-8.5.32/webapps/" + Math.random() + ".pdf");

	}
}
