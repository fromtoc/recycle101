package com.xin.portal.system.util;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



public class ImageUtils {
    private Image img;
    private final static int WIDTH = 147;
    private final static int HEIGHT = 136;

    /**
     * 改变图片的大小到宽为size，然后高随着宽等比例变化
     * @param is 上传的图片的输入流
     * @param os 改变了图片的大小后，把图片的流输出到目标OutputStream
     * @param size 新图片的宽
     * @param format 新图片的格式
     * @throws IOException
     */
    public static OutputStream resizeImage(InputStream is, OutputStream os, int w,int h,double scale, String format) throws IOException {
    	BufferedImage prevImage= ImageIO.read(is);
        double width = prevImage.getWidth();
        double height = prevImage.getHeight();
        int newWidth,newHeight;
        if(h>0&&w>0){
        	 newWidth = (int)(w);
             newHeight = (int)(h);
        }else{
        	newWidth = (int)(width);
            newHeight = (int)(width/scale);
        }
//        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
//        Graphics graphics = image.createGraphics();
//        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
        
//        Color color= new Color(1.0F, 0.75F, 0.0F, 0.45F);
//        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, color, null);
        
        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics(); 
        image = g2d.getDeviceConfiguration().createCompatibleImage(newWidth,newHeight,Transparency.TRANSLUCENT);  
        g2d = image.createGraphics();        
        Image from = prevImage.getScaledInstance(newWidth, newHeight, prevImage.SCALE_AREA_AVERAGING); 
        g2d.drawImage(from, 0, 0, null);
        g2d.dispose(); 
        
        ImageIO.write(image, format, os);
        is.close();
        os.flush();
        os.close();
        //ByteArrayOutputStream b = (ByteArrayOutputStream) os;
        return os;
    }
    
    public static void main(String[] args) {
        try {
        	
            InputStream is = new FileInputStream(new File("C:/apache-tomcat-8.0.24.1/wtpwebapps/chartBg.jpg"));
           /* File  a=new File("C:/apache-tomcat-8.0.24.1/wtpwebapps/chartBg.jpg");
        	if(a.exists()){
        		deleteFile(a);
        	}*/
            OutputStream os = new FileOutputStream(new File("C:/apache-tomcat-8.0.24.1/wtpwebapps/chartBg1.jpg"));
            resizeImage(is, os, 200,100,2, "jpg");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   
    public static String deleteFile(File a){
    	a.delete();
		return null;
    	
    }
}